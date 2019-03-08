package bfst19.osmdrawing.model;

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
		assertEquals(55.6631 ,model.modelBounds.ymin, 0.02);
		assertEquals(7.090 ,model.modelBounds.xmin, 0.02);
		assertEquals(55.6804 ,model.modelBounds.ymax, 0.02);
		assertEquals(7.1073 ,model.modelBounds.xmax, 0.02);
	}

	@Test
	void loadFromObj() throws IOException, XMLStreamException, ClassNotFoundException {
		//Small osm bounds :  <bounds minlat="55.6631" minlon="7.090 " maxlat="55.6804" maxlon="7.107"/>
		//NOTE: This test might get outdated as the model changes. It should be remade each time the model changes
		// as the .obj file will get outdated.
		String filePath = this.getClass().getResource("small.zip.obj").getPath();
		List<String> args = new ArrayList<String>();
		args.add(filePath);
		Model model = new Model(args);
		assertEquals(55.6631 ,model.modelBounds.ymin, 0.02);
		assertEquals(7.090 ,model.modelBounds.xmin, 0.02);
		assertEquals(55.6804 ,model.modelBounds.ymax, 0.02);
		assertEquals(7.1073 ,model.modelBounds.xmax, 0.02);
	}
}