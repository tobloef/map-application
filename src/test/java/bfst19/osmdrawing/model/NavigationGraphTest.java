package bfst19.osmdrawing.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NavigationGraphTest {
	@Test
	void testMergedConnection() {
		assertEquals(55.6631 ,osmParser.getModelBounds().yMin, 0.02);
		assertEquals(7.090 ,osmParser.getModelBounds().xMin, 0.02);
		assertEquals(55.6804 ,osmParser.getModelBounds().yMax, 0.02);
		assertEquals(7.1073 ,osmParser.getModelBounds().xMax, 0.02);
	}
}
