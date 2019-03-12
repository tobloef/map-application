package bfst19.osmdrawing.model;

import bfst19.osmdrawing.view.WayType;

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
	float lonFactor = 1.0f;
	LongIndex<OSMNode> idToNode = new LongIndex<OSMNode>();
	LongIndex<OSMWay> idToWay = new LongIndex<OSMWay>();
	List<OSMWay> coastLines = new ArrayList<>();
	OSMWay currentWay = null;
	OSMRelation currentRelation = null;
	WayType currentType = null;
	DrawableModel drawableModel;
	Rectangle bounds = new Rectangle();

	public OSMParser(String filename, DrawableModel drawableModel) throws IOException, XMLStreamException {
		InputStream osmsource;
		this.drawableModel = drawableModel;
		if (filename.endsWith(".zip")) {
			osmsource = getZipFile(filename);
		} else {
			osmsource = getOsmFile(filename);
		}
		parseOSM(osmsource);
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



	private void parseOSM(InputStream osmsource) throws XMLStreamException {
		XMLStreamReader reader = XMLInputFactory
				.newInstance()
				.createXMLStreamReader(osmsource);

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

	private void handleEndDocumentTag() {
		//Get a list of merged coastlines.
		for (OSMWay coast : merge(coastLines)) {
			drawableModel.add(WayType.COASTLINE, new Polyline(coast));
		}
	}

	private void handleEndElementTag(XMLStreamReader reader) {
		switch (reader.getLocalName()) {
			case "way":
				endElementWay();
				break;
			case "relation":
				endElementRelation();
				break;
		}
	}

	private void endElementRelation() {
		if (currentType == WayType.WATER) {
			drawableModel.add(currentType, new MultiPolyline(currentRelation));
			currentWay = null;
		}
	}

	private void endElementWay() {
		if (currentType == WayType.COASTLINE) {
			coastLines.add(currentWay);
		} else {
			drawableModel.add(currentType, new Polyline(currentWay));
		}
		currentWay = null;
	}

	private void handleStartElementTag(XMLStreamReader reader) {
		switch (reader.getLocalName()) {
			case "bounds":
				startElementBounds(reader);
				break;
			case "node":
				startElementNode(reader);
				long id;
				break;
			case "way":
				startElementWay(reader);
				break;
			case "nd":
				startElementNd(reader);
				long ref;
				break;
			case "tag":
				startElementTag(reader);
				break;
			case "relation":
				startElementRelation();
				break;
			case "member":
				startElementMember(reader);
				break;

		}
	}

	private void startElementMember(XMLStreamReader reader) {
		long ref;
		ref = Long.parseLong(reader.getAttributeValue(null, "ref"));
		OSMWay member = idToWay.get(ref);
		if (member != null) currentRelation.add(member);
	}

	private void startElementRelation() {
		currentType = WayType.UNKNOWN;
		currentRelation = new OSMRelation();
	}

	private void startElementTag(XMLStreamReader reader) {
		String k = reader.getAttributeValue(null, "k");
		String v = reader.getAttributeValue(null, "v");
		if (currentWay != null || currentRelation != null) {
			WayType type = WayTypeFactory.getType(k, v);
			if (type != null){
				this.currentType = type;
			}
		}
	}

	private void startElementNd(XMLStreamReader reader) {
		long ref = Long.parseLong(reader.getAttributeValue(null, "ref"));
		currentWay.add(idToNode.get(ref));
	}

	private void startElementWay(XMLStreamReader reader) {
		long id;
		id = Long.parseLong(reader.getAttributeValue(null, "id"));
		currentType = WayType.UNKNOWN;
		currentWay = new OSMWay(id);
		idToWay.add(currentWay);
	}

	private void startElementNode(XMLStreamReader reader) {
		long id = Long.parseLong(reader.getAttributeValue(null, "id"));
		float lat = Float.parseFloat(reader.getAttributeValue(null, "lat"));
		float lon = lonFactor * Float.parseFloat(reader.getAttributeValue(null, "lon"));
		idToNode.add(new OSMNode(id,lon, lat));
	}

	private void startElementBounds(XMLStreamReader reader) {
		bounds.ymin = Float.parseFloat(reader.getAttributeValue(null, "minlat"));
		bounds.xmin = Float.parseFloat(reader.getAttributeValue(null, "minlon"));
		bounds.ymax = Float.parseFloat(reader.getAttributeValue(null, "maxlat"));
		bounds.xmax = Float.parseFloat(reader.getAttributeValue(null, "maxlon"));
		lonFactor = (float) Math.cos((bounds.ymax+bounds.ymin)/2*Math.PI/180);
		bounds.xmin *= lonFactor;
		bounds.xmax *= lonFactor;
		drawableModel.setModelBounds(bounds);
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
}
