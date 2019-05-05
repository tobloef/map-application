package bfst19.danmarkskort.model;

import bfst19.danmarkskort.model.parsing.AddressParser;
import bfst19.danmarkskort.model.parsing.AddressParserFromData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SuppressWarnings("SimplifiableJUnitAssertion")
public class AddressParserTest {

    AddressParser addressParser = new AddressParserFromData(TestData.STREET_NAMES_1, TestData.CITIES_1);

    @Test
    public void testParse_Street() {
        String query = "Vej Et";
        AddressInput result = addressParser.parse(query);
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
        AddressInput result = addressParser.parse(query);
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
        AddressInput result = addressParser.parse(query);
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
        AddressInput result = addressParser.parse(query);
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
        AddressInput result = addressParser.parse(query);
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
        AddressInput result = addressParser.parse(query);
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
        AddressInput result = addressParser.parse(query);
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
        AddressInput result = addressParser.parse(query);
        assertNotNull(result);
        assertEquals("Vej To", result.getStreetName());
        assertEquals("By Et", result.getCity());
        assertEquals(null, result.getPostCode());
        assertEquals("268", result.getHouseNumber());
        assertEquals(null, result.getFloor());
        assertEquals(null, result.getDoor());
    }

    @Test
    public void testParse_PartialStreet() {
        String query = "Vej T";
        AddressInput result = addressParser.parse(query);
        assertNotNull(result);
        assertEquals("Vej T", result.getStreetName());
        assertEquals(null, result.getCity());
        assertEquals(null, result.getPostCode());
        assertEquals(null, result.getHouseNumber());
        assertEquals(null, result.getFloor());
        assertEquals(null, result.getDoor());
    }

    @Test
    public void testParse_CityPartialStreet() {
        String query = "By Et Vej";
        AddressInput result = addressParser.parse(query);
        assertNotNull(result);
        assertEquals("Vej", result.getStreetName());
        assertEquals("By Et", result.getCity());
        assertEquals(null, result.getPostCode());
        assertEquals(null, result.getHouseNumber());
        assertEquals(null, result.getFloor());
        assertEquals(null, result.getDoor());
    }

    @Test
    public void testParse_StreetPartialCity() {
        String query = "Vej To By";
        AddressInput result = addressParser.parse(query);
        assertNotNull(result);
        assertEquals("Vej To", result.getStreetName());
        assertEquals("By", result.getCity());
        assertEquals(null, result.getPostCode());
        assertEquals(null, result.getHouseNumber());
        assertEquals(null, result.getFloor());
        assertEquals(null, result.getDoor());
    }

    @Test
    public void testParse_StreetHouseNumberCommaCity() {
        String query = "Vej To 2, By Et";
        AddressInput result = addressParser.parse(query);
        assertNotNull(result);
        assertEquals("Vej To", result.getStreetName());
        assertEquals("By Et", result.getCity());
        assertEquals(null, result.getPostCode());
        assertEquals("2", result.getHouseNumber());
        assertEquals(null, result.getFloor());
        assertEquals(null, result.getDoor());
    }
}
