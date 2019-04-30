package bfst19.danmarkskort.model;

import org.junit.jupiter.api.Test;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RouteTest {

	@Test
	void basicTest() throws XMLStreamException, IOException, ClassNotFoundException {
		String filePath = this.getClass().getResource("small.osm").getPath();
		List<String> args = new ArrayList<String>();
		args.add(filePath);
		Model model = new Model(args);
		float startX = 7.0991707f;
		float startY = 55.672295f;
		PolyRoad start = model.getClosestRoad(startX, startY);
		float endX = 7.09944f;
		float endY = 55.673607f;
		PolyRoad end = model.getClosestRoad(endX, endY);
		assertEquals("Wildersgade", start.getName());
		assertEquals("Strandgade", end.getName());

		model.setMouseCoords(startX, startY);
		model.updateStart();
		model.setMouseCoords(endX, endY);
		model.updateEnd();
		Route route = model.getShortestPath();
		assertEquals(3, route.size());
		route.print();
		List<String> fakeDescription = new ArrayList<>();
		fakeDescription.add("Start on Wildersgade");
		fakeDescription.add("Drive 199 m and turn left onto Torvegade");
		fakeDescription.add("Drive 87 m and turn right onto Strandgade");
		fakeDescription.add("You will then arrive at your destination");
		assertEquals(fakeDescription, route.getTextDescription());
	}
}
