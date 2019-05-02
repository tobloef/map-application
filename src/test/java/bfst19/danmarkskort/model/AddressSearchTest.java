package bfst19.danmarkskort.model;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class AddressSearchTest {
    private AddressSearch addressSearch = new AddressSearch(
            TestData.ADDRESSES_BY_STREET_NAME_1,
            TestData.ADDRESSES_BY_CITY_1,
            TestData.STREET_NAMES_1,
            TestData.CITIES_1
    );

    @Test
    public void testGetAddressesWithStreetName() {
        String streetName = "Vej To";
        List<Address> result = addressSearch.getAddressesWithStreetName(streetName);
        long expectedCount = TestData.ADDRESSES_BY_STREET_NAME_1.stream()
                .filter(a -> Objects.equals(a.getStreetName(), streetName))
                .count();
        assertNotNull(result);
        assertEquals(expectedCount, result.size());
        result.forEach(a -> assertSame(a.getStreetName(), streetName));
    }

    @Test
    public void testGetRecommendations_Street() {
        String query = "Vej To";
        List<Address> expected = TestData.ADDRESSES_BY_STREET_NAME_1.stream()
                .filter(address -> Objects.equals(address.getStreetName(), "Vej To"))
                .collect(Collectors.toList());
        List<Address> actual = addressSearch.getRecommendations(query);
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i));
        }
    }

    @Test
    public void testGetRecommendations_StreetCity() {
        String query = "Vej To By Et";
        List<Address> expected = TestData.ADDRESSES_BY_STREET_NAME_1.stream()
                .filter(address -> Objects.equals(address.getStreetName(), "Vej To"))
                .filter(address -> Objects.equals(address.getCity(), "By Et"))
                .collect(Collectors.toList());
        List<Address> actual = addressSearch.getRecommendations(query);
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i));
        }
    }

    @Test
    public void testGetRecommendations_CityStreet() {
        String query = "By Et Vej To";
        List<Address> expected = TestData.ADDRESSES_BY_STREET_NAME_1.stream()
                .filter(address -> Objects.equals(address.getStreetName(), "Vej To"))
                .filter(address -> Objects.equals(address.getCity(), "By Et"))
                .collect(Collectors.toList());
        List<Address> actual = addressSearch.getRecommendations(query);
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i));
        }
    }

    @Test
    public void testGetRecommendations_CityStreetHouseNumber() {
        String query = "By Et Vej To 1";
        List<Address> expected = TestData.ADDRESSES_BY_STREET_NAME_1.stream()
                .filter(address -> Objects.equals(address.getStreetName(), "Vej To"))
                .filter(address -> Objects.equals(address.getCity(), "By Et"))
                .filter(address -> address.getHouseNumber().startsWith("1"))
                .collect(Collectors.toList());
        List<Address> actual = addressSearch.getRecommendations(query);
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i));
        }
    }

    @Test
    public void testGetRecommendations_PartialStreet() {
        String query = "Vej T";
        List<Address> actual = addressSearch.getRecommendations(query);
        if (actual != null) {
            assertEquals(0, actual.size());
        }
    }

    @Test
    public void testGetRecommendations_CityPartialStreet() {
        String query = "By Et Vej";
        List<Address> expected = TestData.ADDRESSES_BY_STREET_NAME_1.stream()
                .filter(address -> address.getStreetName().startsWith("Vej"))
                .filter(address -> Objects.equals(address.getCity(), "By Et"))
                .collect(Collectors.toList());
        List<Address> actual = addressSearch.getRecommendations(query);
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i));
        }
    }

    @Test
    public void testGetRecommendations_StreetPartialCity() {
        String query = "Vej To By";
        List<Address> expected = TestData.ADDRESSES_BY_STREET_NAME_1.stream()
                .filter(address -> Objects.equals(address.getStreetName(), "Vej To"))
                .filter(address -> address.getCity().startsWith("By"))
                .collect(Collectors.toList());
        List<Address> actual = addressSearch.getRecommendations(query);
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i));
        }
    }

    @Test
    public void testGetRecommendations_StreetPartialHouseNumber() {
        String query = "Vej To 1";
        List<Address> expected = TestData.ADDRESSES_BY_STREET_NAME_1.stream()
                .filter(address -> Objects.equals(address.getStreetName(), "Vej To"))
                .filter(address -> address.getHouseNumber().startsWith("1"))
                .collect(Collectors.toList());
        List<Address> actual = addressSearch.getRecommendations(query);
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i));
        }
    }
}
