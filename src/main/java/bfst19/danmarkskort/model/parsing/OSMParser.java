package bfst19.danmarkskort.model.parsing;

import bfst19.danmarkskort.model.*;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.util.*;
import java.util.zip.ZipInputStream;

import static javax.xml.stream.XMLStreamConstants.*;

public class OSMParser {
	private float lonFactor = 1.0f;
	private LongMap<OSMNode> idToNode = new LongMap<OSMNode>();
	private LongMap<OSMWay> idToWay = new LongMap<OSMWay>();
	private OSMWay currentWay = null;
	private OSMRelation currentRelation = null;
	private WayType currentType = null;
	private DrawableModel drawableModel;
	private Rectangle bounds = new Rectangle(); //the outer bounds of our data in terms of coordinates

	private Map<String, String> tags = new HashMap<>();
	private Map<String, Integer> speedLimits = new HashMap<>();
	private List<OSMRoadNode> roadNodes = new ArrayList<>();
	private Set<OSMRoadWay> OSMRoads = new HashSet<>();
	private Map<OSMRoadWay, PolyRoad> roadWaysToPolyRoads = new HashMap<>();
	private Map<PolyRoad, Integer> polyRoadToIntegers = new HashMap<>();

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
		initPolyRoadConnections();
		drawableModel.doneAdding();
	}

	private void initPolyRoadConnections() {
		for (OSMRoadNode node : roadNodes) {
			OSMRoads.addAll(node.getConnections());
		}
		OSMRoads.addAll(splitWays());
		createPolyRoadsFromOSMRoads();
		fillPolyRoadsIntoArray();
		initializeConnections();
	}

	private void initializeConnections() {
		for (OSMRoadWay way : roadWaysToPolyRoads.keySet()) {
			PolyRoad road = roadWaysToPolyRoads.get(way);
			OSMRoadNode first = (OSMRoadNode) way.getFirst();
			OSMRoadNode last = (OSMRoadNode) way.getLast();
			for (OSMRoadWay connectedWay : first.getConnections()) {
				PolyRoad otherRoad = roadWaysToPolyRoads.get(connectedWay);
				if (road != otherRoad){
					road.addConnectionToFirst(otherRoad);
				}
			}
			for (OSMRoadWay connectedWay : last.getConnections()) {
				PolyRoad otherRoad = roadWaysToPolyRoads.get(connectedWay);
				if (road != otherRoad){
					road.addConnectionToLast(otherRoad);
				}
			}
		}
	}

	private void fillPolyRoadsIntoArray() {
		PolyRoad.allPolyRoads = new PolyRoad[roadWaysToPolyRoads.values().size()];
		int i = 0;
		for (PolyRoad road : roadWaysToPolyRoads.values()) {
			polyRoadToIntegers.put(road, i);
			PolyRoad.allPolyRoads[i] = road;
			road.setIndex(i);
			i++;
		}
	}

	private void createPolyRoadsFromOSMRoads() {
		for (OSMRoadWay way : OSMRoads) {
			PolyRoad newRoad = new PolyRoad(way);
			roadWaysToPolyRoads.put(way, newRoad);
			drawableModel.add(way.getType(), newRoad);
		}
	}

	private List<OSMRoadWay> splitWays() {
		List<OSMRoadWay> toBeAdded = new ArrayList<>();
		for (OSMRoadWay way : OSMRoads) {
			OSMRoadWay newWay = way;
			while (newWay != null) {
				newWay = newWay.splitIfNeeded();
				if (newWay != null) {
					toBeAdded.add(newWay);
				}
			}
		}
		return toBeAdded;
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
			}
		}
	}

	// Delegates the task out to methods depending on the tag
	private void handleStartElementTag(XMLStreamReader reader) {
		switch (reader.getLocalName()) {
			case "bounds":
				handleStartBounds(reader);
				break;
			case "node":
				handleStartNode(reader);
				break;
			case "way":
				handleStartWay(reader);
				break;
			case "nd":
				handleStartND(reader);
				break;
			case "tag":
				handleStartTag(reader);
				break;
			case "relation":
				handleStartRelation(reader);
				break;
			case "member":
				handleStartMember(reader);
				break;

		}
	}

	// Delegates the task out to methods depending on the tag
	private void handleEndElementTag(XMLStreamReader reader) {
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
		if (currentWayIsRoad()) {
			int nodeAmount = currentWay.getNodes().size();
			if (nodeAmount <= 0) {
				throw new RuntimeException("Way consists of zero nodes and is not a way");
			}
			convertWayToRoadNodes(currentWay);
			if (nodeAmount != currentWay.getNodes().size()) {
				throw new RuntimeException("Road conversion removes nodes somehow");
			}
		}
		if (currentType != WayType.UNKNOWN) {
			if (currentWay instanceof OSMRoadWay) {
				OSMRoads.add((OSMRoadWay) currentWay);
			}
			else {
				drawableModel.add(currentType, new Polyline(currentWay));
			}
		}
		currentWay = null;
	}

	private boolean currentWayIsRoad() {
		//fixme this should not include footpaths
		return tags.containsKey("highway");
	}

	private void handleStartRelation(XMLStreamReader reader) {
		long id = Long.parseLong(reader.getAttributeValue(null, "id"));
		currentType = WayType.UNKNOWN;
		currentRelation = new OSMRelation(id);
	}

	private void handleEndRelation() {
		if (currentRelation.hasMembers() && currentType != WayType.UNKNOWN) {
			drawableModel.add(currentType, new MultiPolyline(currentRelation));
		}
		currentRelation = null;
	}

	// adds members to the current relation
	private void handleStartMember(XMLStreamReader reader) {
		long ref = Long.parseLong(reader.getAttributeValue(null, "ref"));
		OSMWay member = idToWay.get(ref);
		if (member != null) currentRelation.add(member);
	}

	// assigns waytype the current way, based on key and value
	private void handleStartTag(XMLStreamReader reader) {
		String k = reader.getAttributeValue(null, "k");
		String v = reader.getAttributeValue(null, "v");
		tags.put(k, v);
		if (currentRelation != null && k.equals("name")){
			currentRelation.debugName = v;
		}
		if (currentWay != null || currentRelation != null) {
			WayType type = WayTypeFactory.getWayType(k, v);
			if (type != null) {
				this.currentType = type;
			}
		}
	}

	private void convertWayToRoadNodes(OSMWay currentWay) {
		List<OSMRoadNode> newNodes = new ArrayList<>();
		for (OSMNode node : currentWay.getNodes()) {
			OSMRoadNode newNode;
			if (node instanceof OSMRoadNode) {
				newNode = (OSMRoadNode) node;
			}
			else {
				newNode = convertNodeToRoadNode(node);
			}
			newNodes.add(newNode);
		}
		this.currentWay = convertWayToRoad(currentWay, newNodes);
	}

	private OSMRoadWay convertWayToRoad(OSMWay way, List<OSMRoadNode> newNodes) {
		Set<RoadRestriction> restrictions = new HashSet<>();
		if (tags.get("oneway") != null) {
			restrictions.add(RoadRestriction.ONE_WAY);
		}
		OSMRoadWay road = new OSMRoadWay(way, newNodes, getMaxSpeed(), currentType, restrictions);
		idToWay.replace(road);
		return road;
	}


	private OSMRoadNode convertNodeToRoadNode(OSMNode node) {
		OSMRoadNode newNode = new OSMRoadNode(node);
		idToNode.replace(newNode);
		roadNodes.add(newNode);
		return newNode;
	}

	private int getMaxSpeed() {
		int maxSpeed;
		if (tags.containsKey("maxspeed")) {
			try {
				maxSpeed = Integer.parseInt(tags.get("maxspeed"));
			}
			catch (NumberFormatException e){
				maxSpeed = 30;
			}
		}
		else {
			if (speedLimits.get(tags.get("highway")) != null){
				try {
					maxSpeed = Integer.parseInt(tags.get("maxspeed"));
				}
				catch (NumberFormatException e){
					maxSpeed = 30;
				}
			}
			else {
				maxSpeed = 30; //If we have absolutely no idea how fast we can drive on a road, we just give it the speed 30. //todo handle this better
			}
		}
		return maxSpeed;
	}



	private void handleStartND(XMLStreamReader reader) { //TODO find out what ND stands for and change the name to something readable
		long ref = Long.parseLong(reader.getAttributeValue(null, "ref"));
		currentWay.add(idToNode.get(ref));
	}

	private void handleStartNode(XMLStreamReader reader) {
		long id = Long.parseLong(reader.getAttributeValue(null, "id"));
		float lat = Float.parseFloat(reader.getAttributeValue(null, "lat"));
		float lon = lonFactor * Float.parseFloat(reader.getAttributeValue(null, "lon"));
		idToNode.add(new OSMNode(id, lon, lat));
	}

	private void handleStartBounds(XMLStreamReader reader) {
		bounds.yMin = Float.parseFloat(reader.getAttributeValue(null, "minlat"));
		bounds.xMin = Float.parseFloat(reader.getAttributeValue(null, "minlon"));
		bounds.yMax = Float.parseFloat(reader.getAttributeValue(null, "maxlat"));
		bounds.xMax = Float.parseFloat(reader.getAttributeValue(null, "maxlon"));
		lonFactor = (float) Math.cos((bounds.yMax + bounds.yMin) / 2 * Math.PI / 180);
		bounds.xMin *= lonFactor;
		bounds.xMax *= lonFactor;
		drawableModel.setModelBounds(bounds);
	}

	public Rectangle getModelBounds() {
		return bounds;
	}
}
