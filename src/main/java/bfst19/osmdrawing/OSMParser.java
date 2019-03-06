package bfst19.osmdrawing;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.util.*;
import java.util.zip.ZipInputStream;

import static javax.xml.stream.XMLStreamConstants.*;

public class OSMParser {
	float lonfactor = 1.0f;
	LongIndex<OSMNode> idToNode = new LongIndex<OSMNode>();
	LongIndex<OSMWay> idToWay = new LongIndex<OSMWay>();
	List<OSMWay> coast = new ArrayList<>();
	OSMWay way = null;
	OSMRelation rel = null;
	WayType type = null;
	Map<WayType,List<Drawable>> ways;
	ModelBounds bounds = new ModelBounds();

	public OSMParser(String filename, Map<WayType, List<Drawable>> ways) throws IOException, XMLStreamException {
		InputStream osmsource;
		this.ways = ways;
		if (filename.endsWith(".zip")) {
			osmsource = getZipFile(filename);
		} else {
			osmsource = getOsmFile(filename);
		}
		parseOSM(osmsource);
	}


	private BufferedInputStream getOsmFile(String filename) throws FileNotFoundException {
		return new BufferedInputStream(new FileInputStream(filename));
	}

	private InputStream getZipFile(String filename) throws IOException {
		InputStream osmsource;
		ZipInputStream zip = new ZipInputStream(new BufferedInputStream(new FileInputStream(filename)));
		zip.getNextEntry();
		osmsource = zip;
		return osmsource;
	}



	private void parseOSM(InputStream osmsource) throws XMLStreamException {
		XMLStreamReader reader = XMLInputFactory
				.newInstance()
				.createXMLStreamReader(osmsource);

		while (reader.hasNext()) {
			switch (reader.next()) {
				case START_ELEMENT:
					startElement(reader);
					break;
				case END_ELEMENT:
					endElement(reader);
					break;
				case END_DOCUMENT:
					endDocument();
					break;
			}
		}
	}

	private void endDocument() {
		//Get a list of merged coastlines.
		for (OSMWay c : merge(coast)) {
			ways.get(WayType.COASTLINE).add(new Polyline(c));
		}
	}

	private void endElement(XMLStreamReader reader) {
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
		if (type == WayType.WATER) {
			ways.get(type).add(new MultiPolyline(rel));
			way = null;
		}
	}

	private void endElementWay() {
		if (type == WayType.COASTLINE) {
			coast.add(way);
		} else {
			ways.get(type).add(new Polyline(way));
		}
		way = null;
	}

	private void startElement(XMLStreamReader reader) {
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
		if (member != null) rel.add(member);
	}

	private void startElementRelation() {
		type = WayType.UNKNOWN;
		rel = new OSMRelation();
	}

	private void startElementTag(XMLStreamReader reader) {
		String k = reader.getAttributeValue(null, "k");
		String v = reader.getAttributeValue(null, "v");
		if (way != null || rel != null) {
			WayType type = WayTypeFactory.getType(k, v);
			if (type != null){
				this.type = type;
			}
		}
	}

	private void startElementNd(XMLStreamReader reader) {
		long ref = Long.parseLong(reader.getAttributeValue(null, "ref"));
		way.add(idToNode.get(ref));
	}

	private void startElementWay(XMLStreamReader reader) {
		long id;
		id = Long.parseLong(reader.getAttributeValue(null, "id"));
		type = WayType.UNKNOWN;
		way = new OSMWay(id);
		idToWay.add(way);
	}

	private void startElementNode(XMLStreamReader reader) {
		long id = Long.parseLong(reader.getAttributeValue(null, "id"));
		float lat = Float.parseFloat(reader.getAttributeValue(null, "lat"));
		float lon = lonfactor * Float.parseFloat(reader.getAttributeValue(null, "lon"));
		idToNode.add(new OSMNode(id,lon, lat));
	}

	private void startElementBounds(XMLStreamReader reader) {
		bounds.ymin = Double.parseDouble(reader.getAttributeValue(null, "minlat"));
		bounds.xmin = Double.parseDouble(reader.getAttributeValue(null, "minlon"));
		bounds.ymax = Double.parseDouble(reader.getAttributeValue(null, "maxlat"));
		bounds.xmax = Double.parseDouble(reader.getAttributeValue(null, "maxlon"));
		lonfactor = (float) Math.cos((bounds.ymax+bounds.ymin)/2*Math.PI/180);
		bounds.xmin *= lonfactor;
		bounds.xmax *= lonfactor;
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

	public ModelBounds getBounds() {
		return bounds;
	}
}
