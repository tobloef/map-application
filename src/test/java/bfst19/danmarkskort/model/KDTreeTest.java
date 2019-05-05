package bfst19.danmarkskort.model;

import bfst19.danmarkskort.utils.ResourceLoader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KDTreeTest {
    static Model model;

    @BeforeAll
    public static void setUpClass() throws XMLStreamException, IOException, ClassNotFoundException {
        String filePath = ResourceLoader.getResource("model/small.osm").getPath();
        List<String> args = new ArrayList<String>();
        args.add(filePath);
        model = new Model(args);
    }

    private static long countIterable(Iterable iterable) {
        return StreamSupport.stream(iterable.spliterator(), false).count();
    }

    @Test
    void oneWayInSight() {
        Rectangle rect = new Rectangle(7.0878f, 55.71057f, 7.0916f, 55.713f);
        Iterable ways = model.getWaysOfType(WayType.RAILWAY, rect);
        assertEquals(1, countIterable(ways));
    }

    @Test
    void noWayInSight() {
        Rectangle rect = new Rectangle(7.0878f, 56.71057f, 7.0916f, 56.713f);
        for (WayType wayType : WayType.values()) {
            Iterable ways = model.getWaysOfType(wayType, rect);
            assertEquals(0, countIterable(ways));
        }
    }

    @Test
    void countAllFootWaysWithQuery() {
        Rectangle rect = new Rectangle(0f, 0f, 180f, 180f);
        Iterable ways = model.getWaysOfType(WayType.FOOTWAY, rect);
        assertEquals(773, countIterable(ways));
    }

    @Test
    void countAllFootWaysWithoutQuery() {
        Iterable ways = model.getWaysOfType(WayType.FOOTWAY);
        assertEquals(773, countIterable(ways));
    }

    @Test
    void testInsertion() {
        Rectangle rect = new Rectangle(0f, 0f, 180f, 180f);
        model.insert(WayType.POI, new PointOfInterest(20, 20));
        Iterable ways = model.getWaysOfType(WayType.POI, rect);
        assertEquals(1, countIterable(ways));
    }

    @Test
    void rebalanceOffKDTree() {
        for (int x = 0; x < 100; x++) {
            for (int y = 0; y < 100; y++) {
                model.insert(WayType.RACEWAY, new PointOfInterest(x, y));
            }
        }
        assertEquals(10000, countIterable(model.getWaysOfType(WayType.RACEWAY)));
        Rectangle rect = new Rectangle(0.5f, 0.5f, 10.5f, 10.5f);
        Iterable<Drawable> ways = model.getWaysOfType(WayType.RACEWAY, rect);
        for (Drawable drawable : ways) {
            PointOfInterest poi = ((PointOfInterest) drawable);
            assertEquals(0, rect.euclideanDistanceSquaredTo(poi.getRepresentativeX(), poi.getRepresentativeY()));
        }
        assertEquals(100, countIterable(ways));
    }
}
