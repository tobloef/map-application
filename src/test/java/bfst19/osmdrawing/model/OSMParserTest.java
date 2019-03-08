package bfst19.osmdrawing.model;

import org.junit.jupiter.api.Test;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class OSMParserTest {

	@Test
	void getBoundsOSM() throws IOException, XMLStreamException {
		//Small osm bounds :  <bounds minlat="55.6631" minlon="7.090 " maxlat="55.6804" maxlon="7.107"/>
		DrawableModel drawableModel = new BasicDrawableModel();
		String filePath = this.getClass().getResource("small.osm").getPath();
		OSMParser osmParser = new OSMParser(filePath, drawableModel);
		assertEquals(55.6631 ,osmParser.getModelBounds().ymin, 0.02);
		assertEquals(7.090 ,osmParser.getModelBounds().xmin, 0.02);
		assertEquals(55.6804 ,osmParser.getModelBounds().ymax, 0.02);
		assertEquals(7.1073 ,osmParser.getModelBounds().xmax, 0.02);
	}

	@Test
	void getBoundsZIP() throws IOException, XMLStreamException {
		//Small osm bounds :  <bounds minlat="55.6631" minlon="7.090 " maxlat="55.6804" maxlon="7.107"/>
		DrawableModel drawableModel = new BasicDrawableModel();
		String filePath = this.getClass().getResource("small.zip").getPath();
		OSMParser osmParser = new OSMParser(filePath, drawableModel);
		assertEquals(55.6631 ,osmParser.getModelBounds().ymin, 0.02);
		assertEquals(7.090 ,osmParser.getModelBounds().xmin, 0.02);
		assertEquals(55.6804 ,osmParser.getModelBounds().ymax, 0.02);
		assertEquals(7.1073 ,osmParser.getModelBounds().xmax, 0.02);
	}
}