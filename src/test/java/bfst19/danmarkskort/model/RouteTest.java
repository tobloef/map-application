package bfst19.danmarkskort.model;

import org.junit.jupiter.api.Test;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RouteTest {

    // Somethings really wrong with this test, it passes and fails depending on how it's run.
    @Test
    void basicTest() throws XMLStreamException, IOException, ClassNotFoundException {
        String filePath = this.getClass().getResource("small.osm").getPath();
        List<String> args = new ArrayList<>();
        args.add(filePath);
        Model model = new Model(args);
        float startX = 7.0991707f;
        float startY = 55.672295f;
        PolyRoad start = model.getClosestRoad(startX, startY);
        float endX = 7.09944f;
        float endY = 55.673607f;
        PolyRoad end = model.getClosestRoad(endX, endY);
        assertEquals("Wildersgade", start.getStreetName());
        assertEquals("Strandgade", end.getStreetName());

        model.setMouseModelCoords(startX, startY);
        model.updateStart();
        model.setMouseModelCoords(endX, endY);
        model.updateEnd();
        Route route = model.getShortestPath();
        assertEquals(3, route.size());
        List<String> fakeDescription = new ArrayList<>();
        fakeDescription.add("Start on Wildersgade");
        fakeDescription.add("Drive 199 m and turn left onto Torvegade");
        fakeDescription.add("Drive 87 m and turn right onto Strandgade");
        fakeDescription.add("You will then arrive at your destination");
        assertEquals(fakeDescription, route.getTextDescription());
    }
}
