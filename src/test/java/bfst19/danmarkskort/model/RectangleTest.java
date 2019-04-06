package bfst19.danmarkskort.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RectangleTest {

	@Test
	void intersectTrue() {
		Rectangle rect1 = new Rectangle(0, 0, 5, 10);
		Rectangle rect2 = new Rectangle(4, 1, 9, 9);
		assertTrue(rect1.intersect(rect2));
	}

	@Test
	void intersectFalse() {
		Rectangle rect1 = new Rectangle(0, 0, 4, 10);
		Rectangle rect2 = new Rectangle(5, 1, 9, 9);
		assertFalse(rect1.intersect(rect2));
	}

	@Test
	void contains() {
		Rectangle rect1 = new Rectangle(0, 0, 10, 10); //The larger one
		Rectangle rect2 = new Rectangle(1, 1, 9, 9); //The smaller one
		assertTrue(rect1.contains(rect2));
		assertFalse(rect2.contains(rect1));
	}
}