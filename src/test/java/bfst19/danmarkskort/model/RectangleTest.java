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

	@Test
	void containsPoint() {
		Rectangle rect1 = new Rectangle(0, 0, 10, 10); //The larger one
		assertEquals(2*2,rect1.euclideanDistanceSquaredTo(2, 12),  0.02);
	}

	@Test
	void pointMiddleX() {
		Rectangle rect1 = new Rectangle(0, 0, 10, 10); //The larger one
		assertEquals(2*2, rect1.euclideanDistanceSquaredTo(2, 12), 0.02);
	}

	@Test
	void pointTopLeft() {
		Rectangle rect1 = new Rectangle(0, 0, 10, 10); //The larger one
		assertEquals((1)+(2*2),rect1.euclideanDistanceSquaredTo(-1, 12), 0.02);
	}

	@Test
	void pointBottomRight() {
		Rectangle rect1 = new Rectangle(0, 0, 10, 10); //The larger one
		assertEquals((4*4)+(1), rect1.euclideanDistanceSquaredTo(14, -1), 0.02);
	}
}