package bfst19.danmarkskort;

import bfst19.danmarkskort.model.drawableModel.Rectangle;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RectangleTest {
    Rectangle testRectangle;

    @BeforeEach
    void setup(){
        testRectangle = new Rectangle(0,0,10,10);
    }

    @AfterEach
    void tearDown(){
        testRectangle = null;
    }

    @Test
    void intersectTrue() {
        Rectangle rect1 = new Rectangle(0, 0, 5, 10);
        Rectangle rect2 = new Rectangle(4, 1, 9, 9);
        assertTrue(rect1.intersect(rect2));
    }

    @Test
    void CopyRectangle() {
        Rectangle rect1 = new Rectangle(0, 0, 5, 10);
        Rectangle rect2 = new Rectangle(rect1);
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
        assertEquals(0, rect1.euclideanDistanceSquaredTo(2, 2), 0.02);
    }

    @Test
    void pointMiddleX() {
        Rectangle rect1 = new Rectangle(0, 0, 10, 10); //The larger one
        assertEquals(2 * 2, rect1.euclideanDistanceSquaredTo(2, 12), 0.02);
    }

    @Test
    void pointTopLeft() {
        Rectangle rect1 = new Rectangle(0, 0, 10, 10); //The larger one
        assertEquals((1) + (2 * 2), rect1.euclideanDistanceSquaredTo(-1, 12), 0.02);
    }

    @Test
    void pointBottomRight() {
        Rectangle rect1 = new Rectangle(0, 0, 10, 10); //The larger one
        assertEquals((4 * 4) + (1), rect1.euclideanDistanceSquaredTo(14, -1), 0.02);
    }

    @Test
    void wrongX() {
        assertThrows(IllegalArgumentException.class, () -> new Rectangle(100, 0, 10, 10)); //The larger one
    }

    @Test
    void wrongY() {
        assertThrows(IllegalArgumentException.class, () -> new Rectangle(0, 100, 10, 10)); //The larger one
    }


    @Test
    void getMiddleY() {
        assertEquals(5,testRectangle.getMiddleY());
    }

    @Test
    void getMiddleX() {
        assertEquals(5,testRectangle.getMiddleX());
    }

    @Test
    void getSize() {
        assertEquals(10*10,testRectangle.getSize());
    }

    @Test
    void getHeight() {
        assertEquals(10,testRectangle.getHeight());

    }

    @Test
    void getWidth() {
        assertEquals(10,testRectangle.getWidth());
    }


    @Test
    void growToEncompass() {
        testRectangle = new Rectangle();
        testRectangle.growToEncompass(new Rectangle(19,19,20,20));
        testRectangle.growToEncompass(new Rectangle(-20,-20, -19,-19));
        assertEquals(-20,testRectangle.xMin);
        assertEquals(-20,testRectangle.yMin);
        assertEquals(20,testRectangle.xMax);
        assertEquals(20,testRectangle.yMax);
    }

    @Test
    void getSizeLargestDelta() {
        assertEquals(0.5, testRectangle.getSizeLargestDelta(new Rectangle(0,0,20,20)));
        assertEquals(2, testRectangle.getSizeLargestDelta(new Rectangle(0,0,5,5)));
    }

    @Test
    void toStringTest(){
        assertNotNull(testRectangle.toString());
    }
}