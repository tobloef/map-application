package bfst19.danmarkskort.model;

import bfst19.danmarkskort.model.parsing.OSMParser;
import org.junit.jupiter.api.Test;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DijkstraTest {
	@Test
	public void testShortestPath() throws IOException, XMLStreamException {
		DrawableModel drawableModel = new BasicDrawableModel();
		String filePath = this.getClass().getResource("small.osm").getPath();
		OSMParser osmParser = new OSMParser(filePath, drawableModel);
		PolyRoad a = PolyRoad.allPolyRoads[0];
		for (PolyRoad road : a.getAllConnections()) {
			assertTrue(road.getAllConnections().contains(a));
		}
		PolyRoad b = PolyRoad.allPolyRoads[693];
		PolyRoad c = PolyRoad.allPolyRoads[156];
		//System.out.println(a.getAllConnections());
		//System.out.println(b.getAllConnections());
		List<PolyRoad> shortestPath = Dijkstra.getShortestPath(a, b);
		assertNotNull(shortestPath);
		assertEquals(2, shortestPath.size());
		shortestPath = Dijkstra.getShortestPath(a, c);
		System.out.println(shortestPath);
		assertEquals(3, shortestPath.size());
	}
}
