package bfst19.danmarkskort;

import bfst19.danmarkskort.model.address.Address;
import bfst19.danmarkskort.model.address.AddressSearch;
import bfst19.danmarkskort.model.routePlanning.Route;
import bfst19.danmarkskort.model.Model;
import org.junit.jupiter.api.Test;

import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RouteTest {

	private String startQuery = "Wildersgade";
	private String endQuery = "Strandgade";

    // Somethings really wrong with this test, it passes and fails depending on how it's run.
    @Test
    void testCreation() throws XMLStreamException, IOException, ClassNotFoundException {
		String filePath = this.getClass().getResource("small.osm").getPath();
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
    }

    @Test
	void testPrint() throws IOException, XMLStreamException, ClassNotFoundException {
		String filePath = this.getClass().getResource("small.osm").getPath();
		List<String> args = new ArrayList<>();
		args.add(filePath);
		Model model = new Model(args);
		AddressSearch addressSearch = new AddressSearch(model.getAddressData());
		Address startAddress = addressSearch.getSuggestions(startQuery).get(0).getValue();
		Address endAddress = addressSearch.getSuggestions(endQuery).get(0).getValue();
		model.setStart(startAddress);
		model.setEnd(endAddress);
		Route route = model.getShortestPath();
		route.printToFile(null); //this is strictly for line coverage
		File file = new File("sample.txt");
		route.printToFile(file);
		BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
		String line;
		List<String> fromFile = new ArrayList<>();
		while((line = bufferedReader.readLine()) != null) {
			fromFile.add(line);
		}
		assertEquals(route.getTextDescription(), fromFile);
		Route badRoute = new Route();
		badRoute.printToFile(file);
	}
}
