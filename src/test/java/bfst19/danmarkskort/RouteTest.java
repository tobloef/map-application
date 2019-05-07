package bfst19.danmarkskort;

import bfst19.danmarkskort.model.address.Address;
import bfst19.danmarkskort.model.address.AddressSearch;
import bfst19.danmarkskort.model.drawables.PolyRoad;
import bfst19.danmarkskort.model.routePlanning.Route;
import bfst19.danmarkskort.model.Model;
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
		AddressSearch addressSearch = new AddressSearch(model.getAddressData());
		Address startAddress = addressSearch.getSuggestions("Wildersgade").get(0).getValue();
		Address endAddress = addressSearch.getSuggestions("Strandgade").get(0).getValue();
        float startX = 7.0991707f;
        float startY = 55.672295f;
        float endX = 7.09944f;
		float endY = 55.673607f;
		model.setStart(startAddress);
		model.setEnd(endAddress);
        Route route = model.getShortestPath();
        assertEquals(22, route.size());
    }
}
