package bfst19.danmarkskort;

import bfst19.danmarkskort.model.Model;
import bfst19.danmarkskort.model.drawableModel.Rectangle;
import bfst19.danmarkskort.model.drawables.Drawable;
import bfst19.danmarkskort.model.drawables.DrawableType;
import bfst19.danmarkskort.model.drawables.PointOfInterest;
import bfst19.danmarkskort.utils.ResourceLoader;
import javafx.geometry.Point2D;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static bfst19.danmarkskort.utils.Misc.countIterable;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class KDTreeTest {
    static Model model;

    @BeforeAll
    public static void setUpClass() throws XMLStreamException, IOException, ClassNotFoundException {
        String filePath = ResourceLoader.getResource("small.osm").getPath();
        List<String> args = new ArrayList<>();
        args.add(filePath);
        model = new Model(args);
    }


    @Test
    void oneWayInSight() {
        Rectangle rect = new Rectangle(7.0878f, 55.71057f, 7.0916f, 55.713f);
        Iterable ways = model.getWaysOfType(DrawableType.RAILWAY, rect);
        assertEquals(1, countIterable(ways));
    }

    @Test
    void noWayInSight() {
        Rectangle rect = new Rectangle(7.0878f, 56.71057f, 7.0916f, 56.713f);
        for (DrawableType drawableType : DrawableType.values()) {
            Iterable ways = model.getWaysOfType(drawableType, rect);
            assertEquals(0, countIterable(ways));
        }
    }

    @Test
    void countAllFootWaysWithQuery() {
        Rectangle rect = new Rectangle(0f, 0f, 180f, 180f);
        Iterable ways = model.getWaysOfType(DrawableType.FOOTWAY, rect);
        assertEquals(773, countIterable(ways));
    }

    @Test
    void countAllFootWaysWithoutQuery() {
        Iterable ways = model.getWaysOfType(DrawableType.FOOTWAY);
        assertEquals(773, countIterable(ways));
    }

    @Test
    void oneInsertion() {
        Rectangle rect = new Rectangle(0f, 0f, 180f, 180f);
        model.insert(DrawableType.POI, new PointOfInterest(20, 20));
        Iterable ways = model.getWaysOfType(DrawableType.POI, rect);
        assertEquals(1, countIterable(ways));
    }

    @Test
    void manyInsertions() {
        for (int x = 0; x < 100; x++) {
            for (int y = 0; y < 100; y++) {
                model.insert(DrawableType.RACEWAY, new PointOfInterest(x, y));
            }
        }
        assertEquals(10000, countIterable(model.getWaysOfType(DrawableType.RACEWAY)));
        Rectangle rect = new Rectangle(0.5f, 0.5f, 10.5f, 10.5f);
        Iterable<Drawable> ways = model.getWaysOfType(DrawableType.RACEWAY, rect); //Den type er ikke anvendt, så kdtræet er tomt
        for (Drawable drawable : ways) {
            PointOfInterest poi = ((PointOfInterest) drawable);
            assertEquals(0, rect.euclideanDistanceSquaredTo(poi.getRepresentativeX(), poi.getRepresentativeY()));
        }
        assertEquals(100, countIterable(ways));
    }


    @Test
    void insertOverSplit() {
        //NEW TEST AFTER 9. MAY 2019
        model.insert(DrawableType.RACEWAY, new PointOfInterest(200000000, 200000000));
    }

    @Test
    void insertUnderSplit() {
        //NEW TEST AFTER 9. MAY 2019
        model.insert(DrawableType.RACEWAY, new PointOfInterest(-20000000, -2000000));
    }

    @Test
    void bboxGrowsToEncompassInserted(){
        int numInserted = 0;
        for (int x = 1000; x < 1100; x++) {
            for (int y = 1000; y < 1100; y++) {
                PointOfInterest poi = new PointOfInterest(x, y);
                model.insert(DrawableType.POI, poi);
                numInserted++;
                Point2D coords = new Point2D(x, y );
                PointOfInterest nearest = (PointOfInterest) model.getNearest(DrawableType.POI, coords);
                assertEquals(poi.getRepresentativeX(), nearest.getRepresentativeX());
                assertEquals(poi.getRepresentativeY(), nearest.getRepresentativeY());
                assertEquals(poi,nearest);
            }
        }
    }

    @Test
    void manyInsertionsAtSamePoint(){
        //NEW TEST AFTER 9. MAY 2019
        int numInserted = 0;
        for (int x = 0; x < 10000; x++) {
            PointOfInterest poi = new PointOfInterest(0, 0);
            model.insert(DrawableType.POI, poi);
            numInserted++;
            Point2D coords = new Point2D(0, 0 );
        }
        PointOfInterest poi = new PointOfInterest(0, 0);
        model.insert(DrawableType.UNKNOWN, poi); //Also unused
    }

    public static double generateRandomDouble(double min, double max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }
}
