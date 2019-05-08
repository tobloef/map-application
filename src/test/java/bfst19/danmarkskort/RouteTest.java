package bfst19.danmarkskort;

import bfst19.danmarkskort.exceptions.InvalidUserInputException;
import bfst19.danmarkskort.model.address.Address;
import bfst19.danmarkskort.model.address.AddressSearch;
import bfst19.danmarkskort.model.Model;
import bfst19.danmarkskort.model.routePlanning.Route;
import org.junit.jupiter.api.Test;

import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RouteTest {

	private String startQuery = "Pilestræde";
	private String endQuery = "Halfdansgade";
	private String filePath = this.getClass().getResource("small.osm").getPath();

	// Somethings really wrong with this test, it passes and fails depending on how it's run.
    @Test
    void testCreation() throws XMLStreamException, IOException, ClassNotFoundException {
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
	void testPrint() throws IOException, XMLStreamException, ClassNotFoundException, InvalidUserInputException {
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
		assertThrows(InvalidUserInputException.class, () -> {
			badRoute.printToFile(file);
		});
	}

	@Test
	void testFileName() throws XMLStreamException, IOException, ClassNotFoundException {
		List<String> args = new ArrayList<>();
		args.add(filePath);
		Model model = new Model(args);
		AddressSearch addressSearch = new AddressSearch(model.getAddressData());
		Address startAddress = addressSearch.getSuggestions(startQuery).get(0).getValue();
		Address endAddress = addressSearch.getSuggestions(endQuery).get(0).getValue();
		model.setStart(startAddress);
		model.setEnd(endAddress);
		Route route = model.getShortestPath();
		String first = route.get(0).getStreetNameOrDefault();
		String last = route.get(route.size()-1).getStreetNameOrDefault();
		String expectedFileName = first + "_" + last + ".txt";
		assertEquals(expectedFileName, route.getSuggestedFileName());
	}

	@Test
	void testBoundingBox() throws XMLStreamException, IOException, ClassNotFoundException {
		List<String> args = new ArrayList<>();
		args.add(filePath);
		Model model = new Model(args);
		AddressSearch addressSearch = new AddressSearch(model.getAddressData());
		Address startAddress = addressSearch.getSuggestions(startQuery).get(0).getValue();
		Address endAddress = addressSearch.getSuggestions(endQuery).get(0).getValue();
		model.setStart(startAddress);
		model.setEnd(endAddress);
		Route route = model.getShortestPath();
		assertNotNull(route.getBoundingBox());
	}
}
