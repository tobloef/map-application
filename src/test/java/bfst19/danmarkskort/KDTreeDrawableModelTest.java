package bfst19.danmarkskort;

import bfst19.danmarkskort.model.Drawable;
import bfst19.danmarkskort.model.KDTreeDrawableModel;
import bfst19.danmarkskort.model.PointOfInterest;
import bfst19.danmarkskort.model.WayType;
import bfst19.danmarkskort.model.parsing.OSMParser;
import bfst19.danmarkskort.utils.ResourceLoader;
import javafx.scene.canvas.GraphicsContext;
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
	void adding() {
		long totalBefore = countAllDrawables();
		long totalAdded = 0;
		for(WayType wayType : WayType.values()){
			for(int i = 0; i < 10; i++) {
				model.insert(wayType, new PointOfInterest(2, 2));
				totalAdded++;
			}
		}
		assertEquals(totalBefore+totalAdded, countAllDrawables());
	}

	@Test
	void newDataSet() throws IOException, XMLStreamException {
		long totalBefore = countAllDrawables();
		//adding something to the model and making sure it gets removed when the same dataset is loaded again.
		model.insert(WayType.COASTLINE, new PointOfInterest(2,2));
		assertEquals(totalBefore+1, countAllDrawables());
		String filePath = ResourceLoader.getResource("model/small.osm").getPath();
		new OSMParser(filePath, model);
		assertEquals(totalBefore, countAllDrawables());
	}

	private long countAllDrawables() {
		long totalCount = 0;
		for (WayType wayType : WayType.values()) {
			totalCount+= countIterable(model.getAllDrawablesOfType(wayType));
		}
		return totalCount;
	}

	private class NonSpatial implements Drawable {

		@Override
		public void stroke(GraphicsContext gc, double zoomFactor) {
		}

		@Override
		public void fill(GraphicsContext gc, double zoomFactor) {}

		@Override
		public long getNumOfFloats() {
			return 0;
		}
	}

	@Test
	void insertNonSpatial() {
		assertThrows(IllegalArgumentException.class , ()->model.insert(WayType.COASTLINE, new NonSpatial()));
	}
}