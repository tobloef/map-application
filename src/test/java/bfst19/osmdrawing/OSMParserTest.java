package bfst19.osmdrawing;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class OSMParserTest {

	@Test
	void getBoundsOSM() throws IOException, XMLStreamException {
		DrawableModel drawableModel = new BasicDrawableModel();
		String filePath = this.getClass().getResource("small.osm").getPath();
		OSMParser osmParser = new OSMParser(filePath, drawableModel);
		System.out.println(osmParser.getBounds().ymin);
		assertEquals(55.6631 ,osmParser.getBounds().ymin);
		assertEquals(7.090333543002606 ,osmParser.getBounds().xmin);
		assertEquals(55.6804 ,osmParser.getBounds().ymax);
		assertEquals(7.107307935720682 ,osmParser.getBounds().xmax);
	}

	@Test
	void getBoundsZIP() throws IOException, XMLStreamException {
		DrawableModel drawableModel = new BasicDrawableModel();
		String filePath = this.getClass().getResource("small.zip").getPath();
		OSMParser osmParser = new OSMParser(filePath, drawableModel);
		System.out.println(osmParser.getBounds().ymin);
		assertEquals(55.6631 ,osmParser.getBounds().ymin);
		assertEquals(7.090333543002606 ,osmParser.getBounds().xmin);
		assertEquals(55.6804 ,osmParser.getBounds().ymax);
		assertEquals(7.107307935720682 ,osmParser.getBounds().xmax);
	}
}