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

	private OSMWay currentWay = null;
	private OSMRelation currentRelation = null;
	private WayType currentType = null;
	private DrawableModel drawableModel;
	private Rectangle bounds = new Rectangle(); //the outer bounds of our data in terms of coordinates

	private Map<Long, OSMWay> idToWay = new HashMap<>();
	NodeGraphCreator nodeGraphCreator;

	private Map<String, String> tags = new HashMap<>();

	public OSMParser(String filename, DrawableModel drawableModel) throws IOException, XMLStreamException {
		InputStream osmSource;
		this.drawableModel = drawableModel;
		nodeGraphCreator = new NodeGraphCreator(this.drawableModel);
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
		doneParsing();
	}

	private void doneParsing() {
		idToNode = null;
		idToWay = null;
		System.gc();
		System.runFinalization();
		nodeGraphCreator.initPolyRoadConnections();
		drawableModel.doneAdding();
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
		tags = new HashMap<>();
	}

	private void handleEndWay() {
		if (currentWayIsRoad()) {
			int nodeAmount = currentWay.getNodes().size();
			if (nodeAmount <= 0) {
				throw new RuntimeException("Road consists of zero nodes and is not a way");
			}
			convertWayToRoadNodes(currentWay);
			if (nodeAmount != currentWay.getNodes().size()) {
				throw new RuntimeException("Road conversion resulted in removal of nodes.");
			}
		}
		idToWay.put(currentWay.getAsLong(), currentWay);
		if (currentType != WayType.UNKNOWN) {
			if (currentWay instanceof OSMRoadWay) {
				nodeGraphCreator.addRoad((OSMRoadWay) currentWay);
			}
			else {
				drawableModel.add(currentType, new Polyline(currentWay));
			}
		}
		currentWay = null;
	}

	private boolean currentWayIsRoad() {
		if (!RoadInformation.roadTypes.contains(currentType)){
			return false;
		}
		if (!tags.containsKey("highway") || tags.get("highway") == null){
			return false;
		}
		String highWayType = tags.get("highway");
		if (highWayType.equals("construction") || highWayType.equals("proposed")){
			return false;
		}
		//If none of the cases makes it false, return true.
		return true;
	}

	private void handleStartRelation(XMLStreamReader reader) {
		long id = Long.parseLong(reader.getAttributeValue(null, "id"));
		currentType = WayType.UNKNOWN;
		currentRelation = new OSMRelation(id);
		tags = new HashMap<>();
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
		EnumSet<RoadRestriction> restrictions = getRestrictionsOfRoad(way);
		return new OSMRoadWay(way, newNodes, getMaxSpeed(), currentType, restrictions);
	}

	private EnumSet<RoadRestriction> getRestrictionsOfRoad(OSMWay way) {
		EnumSet<RoadRestriction> restrictions =EnumSet.noneOf(RoadRestriction.class);
		if (tags.containsKey("highway")){
			RoadRestriction vehicleRestriction = getVehicleRestriction();
			if (vehicleRestriction != null){
				restrictions.add(vehicleRestriction);
			}
		}
		if(tags.containsKey("oneway") || tags.containsKey("junction")){
			RoadRestriction oneWayType = getOneWayType();
			if (oneWayType != null){
				restrictions.add(oneWayType);
			}
		}
		return restrictions;
	}

	private RoadRestriction getVehicleRestriction() {
		switch (tags.get("highway")){
			case "motorway": return checkIfBikeAllowed();
			case "trunk": return checkIfBikeAllowed();
			case "primary": return checkIfBikeAllowed();
			case "motorway_link": return checkIfBikeAllowed();
			case "trunk_link": return checkIfBikeAllowed();
			case "primary_link": return checkIfBikeAllowed();
			case "pedestrian": return RoadRestriction.NO_CAR;
			case "track": return RoadRestriction.NO_CAR;
			case "escape": return RoadRestriction.NO_CAR;
			case "footway": return RoadRestriction.NO_CAR;
			case "bridleway": return RoadRestriction.NO_CAR;
			case "steps": return RoadRestriction.NO_CAR;
			case "path": return RoadRestriction.NO_CAR;
			case "cycleway": return RoadRestriction.NO_CAR;
			case "crossing": return RoadRestriction.NO_CAR;
			default: return null;
		}
	}

	private RoadRestriction checkIfBikeAllowed() {
		if (tags.containsKey("bicycle")){
			switch (tags.get("bicycle")){
				case "yes": return null;
				case "no": return RoadRestriction.CAR_ONLY;
				default: return RoadRestriction.CAR_ONLY;
			}
		}
		return RoadRestriction.CAR_ONLY;
	}

	private RoadRestriction getOneWayType() {
		if (tags.containsKey("junction") && tags.get("junction").equals("roundabout")){
			return RoadRestriction.ONE_WAY;
		}
		if (tags.get("oneway") != null) {
			switch (tags.get("oneway")) {
				case "yes":
					return RoadRestriction.ONE_WAY;
				case "true":
					return RoadRestriction.ONE_WAY;
				case "1":
					return RoadRestriction.ONE_WAY;
				case "-1":
					return RoadRestriction.ONE_WAY_REVERSED;
				case "reversible":
					return RoadRestriction.ONE_WAY_REVERSED;
				default:
					return null;
			}
		}
		return null;
	}


	private OSMRoadNode convertNodeToRoadNode(OSMNode node) {
		OSMRoadNode newNode = new OSMRoadNode(node);
		idToNode.replace(newNode);
		nodeGraphCreator.addRoadNode(newNode);
		return newNode;
	}


	//TODO: Write this function, it seems incorrect.
	private int getMaxSpeed() {
		int maxSpeed = 50;
		if (tags.containsKey("maxspeed") && isInteger(tags.get("maxSpeed"))) {
			try {
				maxSpeed = Integer.parseInt(tags.get("maxspeed"));
			}
			catch (NumberFormatException e){
				e.printStackTrace();
				maxSpeed = 50;
			}
		}
		else {
			maxSpeed = RoadInformation.speedLimitsFromTags.getOrDefault(tags.get("highway"), 50);
		}
		return maxSpeed;
	}

	private boolean isInteger(String string) {
		return string != null && string.matches("-?\\d+");
	}

	private void handleStartND(XMLStreamReader reader) {
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
