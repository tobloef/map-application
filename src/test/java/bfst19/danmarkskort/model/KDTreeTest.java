package bfst19.danmarkskort.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

import bfst19.danmarkskort.utils.ResourceLoader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.xml.stream.XMLStreamException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KDTreeTest {
	static Model model;

	@BeforeAll
	public static void setUpClass() throws XMLStreamException, IOException, ClassNotFoundException {
		String filePath = ResourceLoader.getResource("model/small.osm").getPath();
		List<String> args = new ArrayList<String>();
		args.add(filePath);
		model = new Model(args);
	}

	@Test
	void OneWayInSight() {
		Rectangle rect = new Rectangle( 7.0878f, 55.71057f, 7.0916f, 55.713f);
		Iterable ways = model.getWaysOfType(WayType.RAILWAY, rect);
		assertEquals(1, countIterable(ways));
	}

	@Test
	void NoWayInSight() {
		Rectangle rect = new Rectangle( 7.0878f, 56.71057f, 7.0916f, 56.713f);
		for (WayType wayType : WayType.values()) {
			Iterable ways = model.getWaysOfType(wayType, rect);
			assertEquals(0, countIterable(ways));
		}
	}

	private static long countIterable(Iterable iterable){
		return StreamSupport.stream(iterable.spliterator(), false).count();
	}
}
