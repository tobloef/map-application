package bfst19.danmarkskort.model;

import bfst19.danmarkskort.model.parsing.OSMParser;
import bfst19.danmarkskort.utils.ResourceLoader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static bfst19.danmarkskort.utils.Misc.countIterable;

class KDTreeDrawableModelTest {
	KDTreeDrawableModel model;


	@BeforeEach
	void setUp() throws XMLStreamException, IOException, ClassNotFoundException {
		model = new KDTreeDrawableModel();
		String filePath = ResourceLoader.getResource("model/small.osm").getPath();
		new OSMParser(filePath, model);
	}

	@AfterEach
	void tearDown() {
		model = null;
	}

	@Test
	void newDataSet() throws IOException, XMLStreamException {
		long totalBefore = countAllDrawables();
		String filePath = ResourceLoader.getResource("model/small.osm").getPath();
		new OSMParser(filePath, model);
		assertEquals(totalBefore, countAllDrawables());
	}

	private long countAllDrawables() {
		long totalCount = 0;
		for (WayType wayType : WayType.values()) {
			totalCount+= countIterable(model.getDrawablesOfTypeInBounds(wayType, model.modelBounds));
		}
		return totalCount;
	}
}