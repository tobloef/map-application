package bfst19.osmdrawing.model.parsing;

import bfst19.osmdrawing.model.*;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.util.zip.ZipInputStream;

import static javax.xml.stream.XMLStreamConstants.*;

public class OSMParser {
	private float lonFactor = 1.0f;
	private LongIndex<OSMNode> idToNode = new LongIndex<OSMNode>();
	private LongIndex<OSMWay> idToWay = new LongIndex<OSMWay>();
	private OSMWay currentWay = null;
	private OSMRelation currentRelation = null;
	private WayType currentType = null;
	private DrawableModel drawableModel;
	private Rectangle bounds = new Rectangle(); //the outer bounds of our data in terms of coordinates

	public OSMParser(String filename, DrawableModel drawableModel) throws IOException, XMLStreamException {
		InputStream osmSource;
		this.drawableModel = drawableModel;
		if (filename.endsWith(".zip")) {
			osmSource = getZipFile(filename);
		} else if (filename.endsWith(".osm")) {
			osmSource = getOsmFile(filename);
		} else {
			throw new IOException();
		}
		parseOSM(osmSource);
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
	}

	private void handleEndWay() {
		drawableModel.add(currentType, new Polyline(currentWay));
		currentWay = null;
	}

	private void handleStartRelation() {
		currentType = WayType.UNKNOWN;
		currentRelation = new OSMRelation();
	}

	private void handleEndRelation() {
		if (currentRelation.hasMembers()) {
			drawableModel.add(currentType, new MultiPolyline(currentRelation));
		}
		currentRelation = null;
	}

	private void handleStartMember(XMLStreamReader reader) { // adds members to the current relation
		long ref = Long.parseLong(reader.getAttributeValue(null, "ref"));
		OSMWay member = idToWay.get(ref);
		if (member != null) currentRelation.add(member);
	}

	private void handleStartTag(XMLStreamReader reader) { // assigns waytype the current way, based on key and value
		String k = reader.getAttributeValue(null, "k");
		String v = reader.getAttributeValue(null, "v");
		if (currentWay != null){
			currentWay.addTag(k, v);
		}
		else if (currentRelation != null){
			currentRelation.addTag(k, v);
		}
		if (k.equals("name") && v.equals("Amager")){
			System.out.println(currentRelation);
		}
		if (currentWay != null || currentRelation != null) {
			WayType type = WayTypeFactory.getWayType(k, v);
			if (type != null) {
				this.currentType = type;
			}
		}
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
