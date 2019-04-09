package bfst19.osmdrawing.model;

import org.junit.jupiter.api.Test;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;

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
	void testRoadnodesCreation() throws IOException, XMLStreamException {
		DrawableModel drawableModel = new BasicDrawableModel();
		String filePath = this.getClass().getResource("small.osm").getPath();
		OSMParser osmParser = new OSMParser(filePath, drawableModel);
		OSMNode connectedA = osmParser.getNodeFromID(1434961346); //Christianshavns Voldgade
		OSMNode connectedB = osmParser.getNodeFromID(1027406335);
		OSMNode connectedC = osmParser.getNodeFromID(1235730161);
		OSMNode unconnected = osmParser.getNodeFromID(444999894);
		assertTrue(connectedA instanceof OSMRoadNode);
		assertTrue(connectedB instanceof OSMRoadNode);
		assertTrue(((OSMRoadNode) connectedA).isConnected((OSMRoadNode) connectedB));
		assertTrue(((OSMRoadNode) connectedB).isConnected((OSMRoadNode) connectedA));
		assertTrue(((OSMRoadNode) connectedA).isConnected((OSMRoadNode) connectedA));
		assertFalse(((OSMRoadNode) connectedA).isConnected((OSMRoadNode) unconnected));
		assertFalse(((OSMRoadNode) connectedA).isConnected((OSMRoadNode) unconnected, 8, new HashSet<>()));
		assertTrue(((OSMRoadNode) connectedA).isConnected((OSMRoadNode) connectedC, 2, new HashSet<>()));
		assertFalse(((OSMRoadNode) connectedA).isConnected((OSMRoadNode) connectedC, 1, new HashSet<>()));
	}

	@Test
	void testNavigationGraph() throws IOException, XMLStreamException {
		DrawableModel drawableModel = new BasicDrawableModel();
		String filePath = this.getClass().getResource("small.osm").getPath();
		OSMParser osmParser = new OSMParser(filePath, drawableModel);

		//fixme we have a bug with cyclical ways, see voldboligerne (18811922)

		OSMRoadNode connectedA = (OSMRoadNode) osmParser.getNodeFromID(8099319);//Christianshavns Voldgade. a <=> b, b<=> c, a !<=> c
		OSMRoadNode connectedB = (OSMRoadNode) osmParser.getNodeFromID(1235730161);
		OSMRoadNode connectedC = (OSMRoadNode) osmParser.getNodeFromID(1027406335);
		OSMRoadNode removedNode = (OSMRoadNode) osmParser.getNodeFromID(1015286457);
		System.out.println("Step 1");
		NavigationGraph navigationGraph = osmParser.makeNavigationGraph();
		System.out.println("Step 2");
		assertTrue(connectedA.isConnected(connectedB));
		assertTrue(connectedB.isConnected(connectedA));
		assertTrue(connectedB.isConnected(connectedC));
		assertTrue(connectedC.isConnected(connectedB));
		assertFalse(connectedA.isConnected(connectedC));
		assertFalse(connectedC.isConnected(connectedA));
		System.out.println("Step 3");
		assertTrue(connectedA.isConnected(connectedC, 2, new HashSet<>()));
		System.out.println("Step 4");
		assertFalse(navigationGraph.getNodes().contains(removedNode));
		assertFalse(connectedA.isConnected(removedNode));
		assertFalse(connectedB.isConnected(removedNode));
		assertFalse(connectedC.isConnected(removedNode));
	}
}