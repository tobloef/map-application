package bfst19.osmdrawing.model;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import static javax.xml.stream.XMLStreamConstants.*;

public class OSMParser {
	private float lonFactor = 1.0f;
	private LongMap<OSMNode> idToNode = new LongMap<OSMNode>();
	private LongMap<OSMWay> idToWay = new LongMap<OSMWay>();
	private List<OSMWay> coastLines = new ArrayList<>(); //coastlines need extra work, which is why we have a list for them
	private OSMWay currentWay = null;
	private OSMRelation currentRelation = null;
	private WayType currentType = null;
	private DrawableModel drawableModel;
	private Rectangle bounds = new Rectangle(); //the outer bounds of our data in terms of coordinates

	private Map<String, String> tags = new HashMap<>();
	private Map<String, Integer> speedLimits = new HashMap<>();
	private List<OSMRoadNode> roadNodes = new ArrayList<>();
	private NavigationGraph navigationGraph;

	public OSMParser(String filename, DrawableModel drawableModel) throws IOException, XMLStreamException {
		InputStream osmSource;
		initSpeedLimits();
		this.drawableModel = drawableModel;
		if (filename.endsWith(".zip")) {
			osmSource = getZipFile(filename);
		}
		else if (filename.endsWith(".osm")){
			osmSource = getOsmFile(filename);
		}
		else {
			throw new IOException();
		}
		parseOSM(osmSource);
		drawableModel.doneAdding();
	}

	private void initSpeedLimits() {
		speedLimits.put("residential", 50);
		speedLimits.put("unclassified", 50);
		speedLimits.put("tertiary", 65);
		speedLimits.put("secondary", 80);
		speedLimits.put("trunk", 90);
		speedLimits.put("motorway", 130);
	}

	private BufferedInputStream getOsmFile(String filename) throws FileNotFoundException {
		return new BufferedInputStream(new FileInputStream(filename));
	}

	private InputStream getZipFile(String filename) throws IOException {
		ZipInputStream zip = new ZipInputStream(new BufferedInputStream(new FileInputStream(filename)));
		zip.getNextEntry();
		return zip;
	}



	private void parseOSM(InputStream osmSource) throws XMLStreamException {
		XMLStreamReader reader = XMLInputFactory
				.newInstance()
				.createXMLStreamReader(osmSource);

		while (reader.hasNext()) {
			switch (reader.next()) {
				case START_ELEMENT:
					handleStartElementTag(reader);
					break;
				case END_ELEMENT:
					handleEndElementTag(reader);
					break;
				case END_DOCUMENT:
					handleEndDocumentTag();
					break;
			}
		}
	}

	private void handleStartElementTag(XMLStreamReader reader) {
		// Delegates the task out to methods depending on the tag
		switch (reader.getLocalName()) {
			case "bounds":
				handleStartBounds(reader);
				break;
			case "node":
				handleStartNode(reader);
				long id;
				break;
			case "way":
				handleStartWay(reader);
				break;
			case "nd":
				handleStartND(reader);
				long ref;
				break;
			case "tag":
				handleStartTag(reader);
				break;
			case "relation":
				handleStartRelation();
				break;
			case "member":
				handleStartMember(reader);
				break;

		}
	}

	private void handleEndElementTag(XMLStreamReader reader) {
		// Delegates the task out to methods depending on the tag
		switch (reader.getLocalName()) {
			case "way":
				handleEndWay();
				break;
			case "relation":
				handleEndRelation();
				break;
		}
	}

	private void handleStartWay(XMLStreamReader reader) {
		long id = Long.parseLong(reader.getAttributeValue(null, "id"));
		currentType = WayType.UNKNOWN;
		currentWay = new OSMWay(id);
		idToWay.add(currentWay);
		tags = new HashMap<>();
	}

	private void handleEndWay() {
		if (tags.containsKey("highway")) {
			convertWayToRoadNodes(currentWay); //Since currentWay is a list of nodes,
		}
		if (currentType == WayType.COASTLINE) {
			coastLines.add(currentWay);
		} else {
			drawableModel.add(currentType, new Polyline(currentWay));
		}
		currentWay = null;
	}

	private void handleStartRelation() {
		currentType = WayType.UNKNOWN;
		currentRelation = new OSMRelation();
	}

	private void handleEndRelation() {
		if (currentType == WayType.WATER) {
			drawableModel.add(currentType, new MultiPolyline(currentRelation));
			currentWay = null;
		}
	}

	private void handleStartMember(XMLStreamReader reader) { // adds members to the current relation
		long ref = Long.parseLong(reader.getAttributeValue(null, "ref"));
		OSMWay member = idToWay.get(ref);
		if (member != null) currentRelation.add(member);
	}

	private void handleStartTag(XMLStreamReader reader) { // assigns waytype the current way, based on key and value
		String k = reader.getAttributeValue(null, "k");
		String v = reader.getAttributeValue(null, "v");
		tags.put(k, v);
		if (currentWay != null || currentRelation != null) {
			WayType type = WayTypeFactory.getWayType(k, v);
			if (type != null){
				this.currentType = type;
			}
		}
	}

	private void convertWayToRoadNodes(OSMWay currentWay) {
		OSMRoadNode lastNode = null;
		for (OSMNode node : currentWay) {
			OSMRoadNode newNode;
			if (node instanceof OSMRoadNode) {
				newNode = (OSMRoadNode) node;
			}
			else {
				newNode = convertNodeToRoadNode(node);
			}
			addConnection(lastNode, newNode);
			lastNode = newNode;
		}
	}

	private void addConnection(OSMRoadNode lastNode, OSMRoadNode newNode) {
		if (lastNode != null) {
			double distance = findDistanceBetween(lastNode, newNode);
			int maxSpeed = getMaxSpeed();
			Connection connection = new Connection(lastNode, newNode, distance, maxSpeed);
			lastNode.addConnection(connection);
			newNode.addConnection(connection);
		}
	}

	private OSMRoadNode convertNodeToRoadNode(OSMNode node) {
		OSMRoadNode newNode;
		newNode = new OSMRoadNode(node);
		idToNode.replace(newNode);
		roadNodes.add(newNode);
		return newNode;
	}

	private int getMaxSpeed() {
		int maxSpeed;
		if (tags.containsKey("maxspeed")) {
			maxSpeed = Integer.parseInt(tags.get("maxspeed"));
		}
		else {
			if (speedLimits.get(tags.get("highway")) != null){
				maxSpeed = speedLimits.get(tags.get("highway"));
			}
			else {
				maxSpeed = 30; //If we have absolutely no idea how fast we can drive on a road, we just give it the speed 30. //todo handle this better
			}
		}
		return maxSpeed;
	}

	private double findDistanceBetween(OSMRoadNode lastNode, OSMRoadNode newNode) {
		double deltaX = lastNode.getLon() - newNode.getLon();
		double deltaY = lastNode.getLat() - newNode.getLat();
		return Math.sqrt(deltaX*deltaX + deltaY*deltaY);
	}

	private void handleStartND(XMLStreamReader reader) { //TODO find out what ND stands for and change the name to something readable
		long ref = Long.parseLong(reader.getAttributeValue(null, "ref"));
		currentWay.add(idToNode.get(ref));

	}

	private void handleStartNode(XMLStreamReader reader) {
		long id = Long.parseLong(reader.getAttributeValue(null, "id"));
		float lat = Float.parseFloat(reader.getAttributeValue(null, "lat"));
		float lon = lonFactor * Float.parseFloat(reader.getAttributeValue(null, "lon"));
		idToNode.add(new OSMNode(id,lon, lat));
	}

	private void handleStartBounds(XMLStreamReader reader) {
		bounds.yMin = Float.parseFloat(reader.getAttributeValue(null, "minlat"));
		bounds.xMin = Float.parseFloat(reader.getAttributeValue(null, "minlon"));
		bounds.yMax = Float.parseFloat(reader.getAttributeValue(null, "maxlat"));
		bounds.xMax = Float.parseFloat(reader.getAttributeValue(null, "maxlon"));
		lonFactor = (float) Math.cos((bounds.yMax +bounds.yMin)/2*Math.PI/180);
		bounds.xMin *= lonFactor;
		bounds.xMax *= lonFactor;
		drawableModel.setModelBounds(bounds);
	}

	private void handleEndDocumentTag() {
		//Get a list of merged coastlines.
		for (OSMWay coast : merge(coastLines)) {
			drawableModel.add(WayType.COASTLINE, new Polyline(coast));
		}
	}

	private static Iterable<OSMWay> merge(List<OSMWay> coast) {
		Map<OSMNode,OSMWay> pieces = new HashMap<>();
		for (OSMWay way : coast) {
			OSMWay res = new OSMWay(0);
			OSMWay before = pieces.remove(way.getFirst());
			if (before != null) {
				pieces.remove(before.getFirst());
				for (int i = 0 ; i < before.size() - 1 ; i++) {
					res.add(before.get(i));
				}
			}
			res.addAll(way);
			OSMWay after = pieces.remove(way.getLast());
			if (after != null) {
				pieces.remove(after.getLast());
				for (int i = 1 ; i < after.size() ; i++) {
					res.add(after.get(i));
				}
			}
			pieces.put(res.getFirst(), res);
			pieces.put(res.getLast(), res);
		}
		return pieces.values();
	}

	public Rectangle getModelBounds() {
		return bounds;
	}

	public NavigationGraph getNavigationGraph() {
		return navigationGraph;
	}

	public List<OSMRoadNode> getRoadNodes() {
		return roadNodes;
	}

	public OSMNode getNodeFromID(long ref) {
		return idToNode.get(ref);
	}
	public LongMap<OSMNode> getLongMap() {return idToNode;}

	public NavigationGraph makeNavigationGraph() {
		return new NavigationGraph(roadNodes);
	}
}
