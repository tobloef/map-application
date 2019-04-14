package bfst19.danmarkskort.model;

import bfst19.danmarkskort.model.parsing.OSMNode;
import bfst19.danmarkskort.model.parsing.OSMParser;
import bfst19.danmarkskort.model.parsing.OSMRoadNode;
import org.junit.jupiter.api.Test;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class OSMParserTest {

	@Test
	void getBoundsOSM() throws IOException, XMLStreamException {
		//Small osm bounds :  <bounds minlat="55.6631" minlon="7.090 " maxlat="55.6804" maxlon="7.107"/>
		DrawableModel drawableModel = new BasicDrawableModel();
		String filePath = this.getClass().getResource("small.osm").getPath();
		OSMParser osmParser = new OSMParser(filePath, drawableModel);
		assertEquals(55.6631 ,osmParser.getModelBounds().yMin, 0.02);
		assertEquals(7.090 ,osmParser.getModelBounds().xMin, 0.02);
		assertEquals(55.6804 ,osmParser.getModelBounds().yMax, 0.02);
		assertEquals(7.1073 ,osmParser.getModelBounds().xMax, 0.02);
	}

	@Test
	void getBoundsZIP() throws IOException, XMLStreamException {
		//Small osm bounds :  <bounds minlat="55.6631" minlon="7.090 " maxlat="55.6804" maxlon="7.107"/>
		DrawableModel drawableModel = new BasicDrawableModel();
		String filePath = this.getClass().getResource("small.zip").getPath();
		OSMParser osmParser = new OSMParser(filePath, drawableModel);
		assertEquals(55.6631 ,osmParser.getModelBounds().yMin, 0.02);
		assertEquals(7.090 ,osmParser.getModelBounds().xMin, 0.02);
		assertEquals(55.6804 ,osmParser.getModelBounds().yMax, 0.02);
		assertEquals(7.1073 ,osmParser.getModelBounds().xMax, 0.02);
	}

	@Test
	void testGraph() throws IOException, XMLStreamException {
		boolean test = true;
		DrawableModel drawableModel = new KDTreeDrawableModel();
		String filePath = this.getClass().getResource("small.osm").getPath();
		OSMParser osmParser = new OSMParser(filePath, drawableModel);
		PolyRoad a = (PolyRoad) drawableModel.getNearestNeighbor(WayType.RESIDENTIAL_ROAD, 205, 408);
		Set<PolyRoad> aConnections = new HashSet<>(a.getFirstConnections());
		aConnections.addAll(a.getLastConnections());
		for (PolyRoad road : aConnections){
			Set<PolyRoad> otherRoads = new HashSet<>(road.getFirstConnections());
			otherRoads.addAll(road.getLastConnections());
			if (!otherRoads.contains(a)) {
				test = false;
			}
		}
		assertTrue(test);
	}
}