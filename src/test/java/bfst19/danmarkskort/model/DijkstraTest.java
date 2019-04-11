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
		PolyRoad b = PolyRoad.allPolyRoads[482];
		System.out.println(a.getAllConnections());
		System.out.println(b.getAllConnections());
		List<PolyRoad> shortestPath = Dijkstra.getShortestPath(a, b);
		assertNotNull(shortestPath);
		b = PolyRoad.allPolyRoads[1086];
		shortestPath = Dijkstra.getShortestPath(a, b);
		assertEquals(shortestPath.size(), 3);
	}
}
