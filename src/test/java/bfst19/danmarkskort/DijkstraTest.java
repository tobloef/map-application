package bfst19.danmarkskort;

import bfst19.danmarkskort.model.Model;
import org.junit.jupiter.api.Test;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DijkstraTest {

    @Test
    public void testConnectionInGraph() throws IOException, XMLStreamException, ClassNotFoundException {
        String filePath = this.getClass().getResource("small.osm").getPath();
        List<String> args = new ArrayList<>();
        args.add(filePath);
        Model model = new Model(args);
        model.setMouseModelCoords(7.100265f, 55.660103f);
        model.updateStart();
        model.setMouseModelCoords(7.1075945f, 55.663807f);
        model.updateEnd();
        assertTrue(model.getShortestPath().size() > 2);
    }
}
