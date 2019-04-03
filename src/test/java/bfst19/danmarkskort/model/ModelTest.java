package bfst19.danmarkskort.model;

import org.junit.jupiter.api.Test;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ModelTest {
	@Test
	void getBoundsOSM() throws IOException, XMLStreamException, ClassNotFoundException {
		//Small osm bounds :  <bounds minlat="55.6631" minlon="7.090 " maxlat="55.6804" maxlon="7.107"/>
		String filePath = this.getClass().getResource("small.osm").getPath();
		List<String> args = new ArrayList<String>();
		args.add(filePath);
		Model model = new Model(args);
		assertEquals(55.6631 ,model.modelBounds.yMin, 0.02);
		assertEquals(7.090 ,model.modelBounds.xMin, 0.02);
		assertEquals(55.6804 ,model.modelBounds.yMax, 0.02);
		assertEquals(7.1073 ,model.modelBounds.xMax, 0.02);
	}

	@Test
	void loadFromObj() throws IOException, XMLStreamException, ClassNotFoundException {
		//Small osm bounds :  <bounds minlat="55.6631" minlon="7.090 " maxlat="55.6804" maxlon="7.107"/>
		String filePath = this.getClass().getResource("small.zip").getPath();
		List<String> args = new ArrayList<String>();
		args.add(filePath);
		Model model = new Model(args);
		args.set(0, args.get(0) + ".ser");
		Model objModel = new Model(args);
		assertEquals(55.6631 ,objModel.modelBounds.yMin, 0.02);
		assertEquals(7.090 ,objModel.modelBounds.xMin, 0.02);
		assertEquals(55.6804 ,objModel.modelBounds.yMax, 0.02);
		assertEquals(7.1073 ,objModel.modelBounds.xMax, 0.02);
	}
}