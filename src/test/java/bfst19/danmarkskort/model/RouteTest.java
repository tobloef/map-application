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
		PolyRoad start = model.getClosestRoad(7.0991707f, 55.672295f);
		PolyRoad end = model.getClosestRoad(7.09944f, 55.673607f);
		assertEquals("Wildersgade", start.getName());
		assertEquals("Strandgade", end.getName());

	}
}
