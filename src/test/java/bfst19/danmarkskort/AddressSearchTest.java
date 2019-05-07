package bfst19.danmarkskort;

import bfst19.danmarkskort.data.AddressTestData;
import bfst19.danmarkskort.data.RealAddresses;
import bfst19.danmarkskort.data.TestAddresses;
import bfst19.danmarkskort.model.Address;
import bfst19.danmarkskort.model.AddressData;
import bfst19.danmarkskort.model.AddressSearch;
import javafx.util.Pair;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class AddressSearchTest {
    private AddressTestData testAddresses = new TestAddresses();
    private AddressTestData realAddresses = new RealAddresses();
    private AddressSearch addressSearchTestData = new AddressSearch(new AddressData(
            testAddresses.getAddressesByStreetName(),
            testAddresses.getAddressesByCity(),
            testAddresses.getStreetNames(),
            testAddresses.getCities()
    ));private AddressSearch addressSearchRealData = new AddressSearch(new AddressData(
            realAddresses.getAddressesByStreetName(),
            realAddresses.getAddressesByCity(),
            realAddresses.getStreetNames(),
            realAddresses.getCities()
    ));

    @Test
    public void testGetAddressesWithStreetName() {
        String streetName = "Vej To";
        List<Address> result = addressSearchTestData.getAddressesWithStreetName(streetName);
        long expectedCount = testAddresses.getAddressesByStreetName().stream()
                .filter(a -> Objects.equals(a.getStreetName(), streetName))
                .count();
        assertNotNull(result);
        assertEquals(expectedCount, result.size());
        result.forEach(a -> assertSame(a.getStreetName(), streetName));
    }

    @Test
    public void testGetSuggestions_Street() {
        String query = "Vej To";
        List<Address> expected = testAddresses.getAddressesByStreetName().stream()
                .filter(address -> Objects.equals(address.getStreetName(), "Vej To"))
                .collect(Collectors.toList());
        List<Pair<String, Address>> actual = addressSearchTestData.getSuggestions(query);
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i).getValue());
        }
    }

    @Test
    public void testGetSuggestions_StreetCity() {
        String query = "Vej To By Et";
        List<Address> expected = testAddresses.getAddressesByStreetName().stream()
                .filter(address -> Objects.equals(address.getStreetName(), "Vej To"))
                .filter(address -> Objects.equals(address.getCity(), "By Et"))
                .collect(Collectors.toList());
        List<Pair<String, Address>> actual = addressSearchTestData.getSuggestions(query);
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i).getValue());
        }
    }

    @Test
    public void testGetSuggestions_CityStreet() {
        String query = "By Et Vej To";
        List<Address> expected = testAddresses.getAddressesByStreetName().stream()
                .filter(address -> Objects.equals(address.getStreetName(), "Vej To"))
                .filter(address -> Objects.equals(address.getCity(), "By Et"))
                .collect(Collectors.toList());
        List<Pair<String, Address>> actual = addressSearchTestData.getSuggestions(query);
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i).getValue());
        }
    }

    @Test
    public void testGetSuggestions_CityStreetHouseNumber() {
        String query = "By Et Vej To 1";
        List<Address> expected = testAddresses.getAddressesByStreetName().stream()
                .filter(address -> Objects.equals(address.getStreetName(), "Vej To"))
                .filter(address -> Objects.equals(address.getCity(), "By Et"))
                .filter(address -> address.getHouseNumber().startsWith("1"))
                .collect(Collectors.toList());
        List<Pair<String, Address>> actual = addressSearchTestData.getSuggestions(query);
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i).getValue());
        }
    }

    @Test
    public void testGetSuggestions_PartialStreet() {
        String query = "Vej T";
        List<Pair<String, Address>> actual = addressSearchTestData.getSuggestions(query);
        if (actual != null) {
            assertEquals(2, actual.size());
        }
    }

    @Test
    public void testGetSuggestions_CityPartialStreet() {
        String query = "By Et Vej";
        List<Address> expected = testAddresses.getAddressesByStreetName().stream()
                .filter(address -> address.getStreetName().startsWith("Vej"))
                .filter(address -> Objects.equals(address.getCity(), "By Et"))
                .collect(Collectors.toList());
        List<Pair<String, Address>> actual = addressSearchTestData.getSuggestions(query);
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i).getValue());
        }
    }

    @Test
    public void testGetSuggestions_StreetPartialCity() {
        String query = "Vej To By";
        List<Address> expected = testAddresses.getAddressesByStreetName().stream()
                .filter(address -> Objects.equals(address.getStreetName(), "Vej To"))
                .filter(address -> address.getCity().startsWith("By"))
                .collect(Collectors.toList());
        List<Pair<String, Address>> actual = addressSearchTestData.getSuggestions(query);
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i).getValue());
        }
    }

    @Test
    public void testGetSuggestions_StreetPartialHouseNumber() {
        String query = "Vej To 1";
        List<Address> expected = testAddresses.getAddressesByStreetName().stream()
                .filter(address -> Objects.equals(address.getStreetName(), "Vej To"))
                .filter(address -> address.getHouseNumber().startsWith("1"))
                .collect(Collectors.toList());
        List<Pair<String, Address>> actual = addressSearchTestData.getSuggestions(query);
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i).getValue());
        }
    }
}
