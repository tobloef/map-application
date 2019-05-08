package bfst19.danmarkskort;

import bfst19.danmarkskort.model.Model;
import bfst19.danmarkskort.model.address.Address;
import bfst19.danmarkskort.model.address.AddressSearch;
import bfst19.danmarkskort.model.routePlanning.Dijkstra;
import bfst19.danmarkskort.model.routePlanning.Route;
import org.junit.jupiter.api.Test;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DijkstraTest {
	private String startQuery = "Pilestræde";
	private String endQuery = "Halfdansgade";
	private String filePath = this.getClass().getResource("small.osm").getPath();

	// Somethings really wrong with this test, it passes and fails depending on how it's run.
	@Test
	void testConnectionInGraph() throws XMLStreamException, IOException, ClassNotFoundException {
		List<String> args = new ArrayList<>();
		args.add(filePath);
		Model model = new Model(args);
		AddressSearch addressSearch = new AddressSearch(model.getAddressData());
		Address startAddress = addressSearch.getSuggestions(startQuery).get(0).getValue();
		Address endAddress = addressSearch.getSuggestions(endQuery).get(0).getValue();
		model.setStart(startAddress);
		model.setEnd(endAddress);
		Route route = model.getShortestPath();
		assertNotNull(route);
		assertTrue(route.size() > 2);
		assertNotNull(Dijkstra.getLastVisitedRoads());
		model.cleanup();
	}
}
