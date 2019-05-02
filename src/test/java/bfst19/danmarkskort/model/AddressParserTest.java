package bfst19.danmarkskort.model;

import bfst19.danmarkskort.model.parsing.AddressParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SuppressWarnings("SimplifiableJUnitAssertion")
public class AddressParserTest {


    @Test
    public void testParse_Street() {
        String query = "Vej Et";
        AddressQuery result = AddressParser.parse(TestData.addresses, TestData.cities, query);
        assertNotNull(result);
        assertEquals("Vej Et", result.getStreetName());
        assertEquals(null, result.getCity());
        assertEquals(null, result.getPostCode());
        assertEquals(null, result.getHouseNumber());
        assertEquals(null, result.getFloor());
        assertEquals(null, result.getDoor());
    }

    @Test
    public void testParse_StreetWithSpace() {
        String query = "Vej To";
        AddressQuery result = AddressParser.parse(TestData.addresses, TestData.cities, query);
        assertNotNull(result);
        assertEquals("Vej To", result.getStreetName());
        assertEquals(null, result.getCity());
        assertEquals(null, result.getPostCode());
        assertEquals(null, result.getHouseNumber());
        assertEquals(null, result.getFloor());
        assertEquals(null, result.getDoor());
    }

    @Test
    public void testParse_StreetWithSpaceAndCity() {
        String query = "Vej To By Et";
        AddressQuery result = AddressParser.parse(TestData.addresses, TestData.cities, query);
        assertNotNull(result);
        assertEquals("Vej To", result.getStreetName());
        assertEquals("By Et", result.getCity());
        assertEquals(null, result.getPostCode());
        assertEquals(null, result.getHouseNumber());
        assertEquals(null, result.getFloor());
        assertEquals(null, result.getDoor());
    }

    @Test
    public void testParse_StreetWithSpaceCommaCity() {
        String query = "Vej To, By Et";
        AddressQuery result = AddressParser.parse(TestData.addresses, TestData.cities, query);
        assertNotNull(result);
        assertEquals("Vej To", result.getStreetName());
        assertEquals("By Et", result.getCity());
        assertEquals(null, result.getPostCode());
        assertEquals(null, result.getHouseNumber());
        assertEquals(null, result.getFloor());
        assertEquals(null, result.getDoor());
    }

    @Test
    public void testParse_CityStreetName() {
        String query = "By Et Vej To";
        AddressQuery result = AddressParser.parse(TestData.addresses, TestData.cities, query);
        assertNotNull(result);
        assertEquals("Vej To", result.getStreetName());
        assertEquals("By Et", result.getCity());
        assertEquals(null, result.getPostCode());
        assertEquals(null, result.getHouseNumber());
        assertEquals(null, result.getFloor());
        assertEquals(null, result.getDoor());
    }

    @Test
    public void testParse_CityStreetNameHouseNumberCommas() {
        String query = "By Et, Vej To, 268";
        AddressQuery result = AddressParser.parse(TestData.addresses, TestData.cities, query);
        assertNotNull(result);
        assertEquals("Vej To", result.getStreetName());
        assertEquals("By Et", result.getCity());
        assertEquals(null, result.getPostCode());
        assertEquals("268", result.getHouseNumber());
        assertEquals(null, result.getFloor());
        assertEquals(null, result.getDoor());
    }

    @Test
    public void testParse_testParse_CityStreetNameHouseNumber() {
        String query = "By Et Vej To 268";
        AddressQuery result = AddressParser.parse(TestData.addresses, TestData.cities, query);
        assertNotNull(result);
        assertEquals("Vej To", result.getStreetName());
        assertEquals("By Et", result.getCity());
        assertEquals(null, result.getPostCode());
        assertEquals("268", result.getHouseNumber());
        assertEquals(null, result.getFloor());
        assertEquals(null, result.getDoor());
    }

    @Test
    public void testParse_StreetNameHouseNumberCity() {
        String query = "Vej To 268 By Et";
        AddressQuery result = AddressParser.parse(TestData.addresses, TestData.cities, query);
        assertNotNull(result);
        assertEquals("Vej To", result.getStreetName());
        assertEquals("By Et", result.getCity());
        assertEquals(null, result.getPostCode());
        assertEquals("268", result.getHouseNumber());
        assertEquals(null, result.getFloor());
        assertEquals(null, result.getDoor());
    }
}
