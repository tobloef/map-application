package bfst19.danmarkskort.model;

import bfst19.danmarkskort.model.parsing.AddressParser;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SuppressWarnings("SimplifiableJUnitAssertion")
public class AddressParserTest {


    @Test
    public void testParse_Street() {
        String query = "Vej";
        AddressQuery result = AddressParser.parse(TestData.addresses, TestData.cities, query);
        assertNotNull(result);
        assertEquals(result.getStreetName(), "Vej");
        assertEquals(result.getCity(), null);
        assertEquals(result.getPostCode(), null);
        assertEquals(result.getHouseNumber(), null);
        assertEquals(result.getFloor(), null);
        assertEquals(result.getDoor(), null);
    }

    @Test
    public void testParse_StreetWithSpace() {
        String query = "Vej To";
        AddressQuery result = AddressParser.parse(TestData.addresses, TestData.cities, query);
        assertNotNull(result);
        assertEquals(result.getStreetName(), "Vej To");
        assertEquals(result.getCity(), null);
        assertEquals(result.getPostCode(), null);
        assertEquals(result.getHouseNumber(), null);
        assertEquals(result.getFloor(), null);
        assertEquals(result.getDoor(), null);
    }

    @Test
    public void testParse_StreetWithSpaceAndCity() {
        String query = "Vej To By";
        AddressQuery result = AddressParser.parse(TestData.addresses, TestData.cities, query);
        assertNotNull(result);
        assertEquals(result.getStreetName(), "Vej To");
        assertEquals(result.getCity(), "By");
        assertEquals(result.getPostCode(), null);
        assertEquals(result.getHouseNumber(), null);
        assertEquals(result.getFloor(), null);
        assertEquals(result.getDoor(), null);
    }

    @Test
    public void testParse_StreetWithSpaceCommaCity() {
        String query = "Vej To, By";
        AddressQuery result = AddressParser.parse(TestData.addresses, TestData.cities, query);
        assertNotNull(result);
        assertEquals(result.getStreetName(), "Vej To");
        assertEquals(result.getCity(), "By");
        assertEquals(result.getPostCode(), null);
        assertEquals(result.getHouseNumber(), null);
        assertEquals(result.getFloor(), null);
        assertEquals(result.getDoor(), null);
    }

    @Test
    public void testParse_CityStreetName() {
        String query = "By Vej To";
        AddressQuery result = AddressParser.parse(TestData.addresses, TestData.cities, query);
        assertNotNull(result);
        assertEquals(result.getStreetName(), "Vej To");
        assertEquals(result.getCity(), "By");
        assertEquals(result.getPostCode(), null);
        assertEquals(result.getHouseNumber(), null);
        assertEquals(result.getFloor(), null);
        assertEquals(result.getDoor(), null);
    }

    @Test
    public void testParse_CityStreetNameHouseNumberCommas() {
        String query = "By, Vej To, 268";
        AddressQuery result = AddressParser.parse(TestData.addresses, TestData.cities, query);
        assertNotNull(result);
        assertEquals(result.getStreetName(), "Vej To");
        assertEquals(result.getCity(), "By");
        assertEquals(result.getPostCode(), null);
        assertEquals(result.getHouseNumber(), "268");
        assertEquals(result.getFloor(), null);
        assertEquals(result.getDoor(), null);
    }

    @Test
    public void testParse_testParse_CityStreetNameHouseNumber() {
        String query = "By Vej To 268";
        AddressQuery result = AddressParser.parse(TestData.addresses, TestData.cities, query);
        assertNotNull(result);
        assertEquals(result.getStreetName(), "Vej To");
        assertEquals(result.getCity(), "By");
        assertEquals(result.getPostCode(), null);
        assertEquals(result.getHouseNumber(), "268");
        assertEquals(result.getFloor(), null);
        assertEquals(result.getDoor(), null);
    }

    @Test
    public void testParse_StreetNameHouseNumberCity() {
        String query = "Vej To 268 By";
        AddressQuery result = AddressParser.parse(TestData.addresses, TestData.cities, query);
        assertNotNull(result);
        assertEquals(result.getStreetName(), "Vej To");
        assertEquals(result.getCity(), "By");
        assertEquals(result.getPostCode(), null);
        assertEquals(result.getHouseNumber(), "268");
        assertEquals(result.getFloor(), null);
        assertEquals(result.getDoor(), null);
    }
}
