package bfst19.danmarkskort;

import bfst19.danmarkskort.model.Model;
import org.junit.jupiter.api.Test;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ModelTest {
	String filePath = this.getClass().getResource("small.osm").getPath();
    @Test
    void getBoundsOSM() throws IOException, XMLStreamException, ClassNotFoundException {
        //Small osm bounds :  <bounds minlat="55.6631" minlon="7.090 " maxlat="55.6804" maxlon="7.107"/>
        List<String> args = new ArrayList<>();
        args.add(filePath);
        Model model = new Model(args);
        assertEquals(55.6631, model.getModelBounds().yMin, 0.02);
        assertEquals(7.090, model.getModelBounds().xMin, 0.02);
        assertEquals(55.6804, model.getModelBounds().yMax, 0.02);
        assertEquals(7.1073, model.getModelBounds().xMax, 0.02);
    }

    @Test
    void loadFromObj() throws IOException, XMLStreamException, ClassNotFoundException {
        //Small osm bounds :  <bounds minlat="55.6631" minlon="7.090 " maxlat="55.6804" maxlon="7.107"/>
        List<String> args = new ArrayList<>();
        args.add(filePath);
        new Model(args);
        args.set(0, args.get(0) + ".ser");
        Model objModel = new Model(args);
        assertEquals(55.6631, objModel.getModelBounds().yMin, 0.02);
        assertEquals(7.090, objModel.getModelBounds().xMin, 0.02);
        assertEquals(55.6804, objModel.getModelBounds().yMax, 0.02);
        assertEquals(7.1073, objModel.getModelBounds().xMax, 0.02);
    }

	@Test
	void testMouse() throws IOException, XMLStreamException, ClassNotFoundException {
		List<String> args = new ArrayList<>();
		args.add(filePath);
		Model model = new Model(args);
		float mouseModelX = 55.6631f;
		float mouseModelY = 7.090f;
		model.setMouseModelCoords(mouseModelX, mouseModelY);
		assertEquals(mouseModelX, model.getMouseModelX());
		assertEquals(mouseModelY, model.getMouseModelY());
		float mouseScreenX = 55.6631f;
		float mouseScreenY = 7.090f;
		model.setMouseScreenCoords(mouseScreenX, mouseScreenY);
		assertEquals(mouseScreenX, model.getMouseScreenX());
		assertEquals(mouseScreenY, model.getMouseScreenY());
		model.updateMouseIdle();
	}

	@Test
	void testLoadData() throws IOException, XMLStreamException, ClassNotFoundException {
		List<String> args = new ArrayList<>();
		args.add(filePath);
		Model model = new Model(args);
		model.loadNewMapData(filePath);
	}
}