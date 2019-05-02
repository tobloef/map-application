package bfst19.danmarkskort.model;

import bfst19.danmarkskort.model.parsing.AddressParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("SimplifiableJUnitAssertion")
public class AddressParserTest {


    @Test
    public void testParse_Street() {
        AddressQuery result = AddressParser.parse(TestData.addresses, "Vej");
        assertEquals(result.getStreetName(), "Vej");
        assertEquals(result.getCity(), null);
        assertEquals(result.getPostCode(), null);
        assertEquals(result.getHouseNumber(), null);
        assertEquals(result.getFloor(), null);
        assertEquals(result.getDoor(), null);
    }

    @Test
    public void testParse_StreetWithSpace() {
        AddressQuery result = AddressParser.parse(TestData.addresses, "Vej To");
        assertEquals(result.getStreetName(), "Vej To");
        assertEquals(result.getCity(), null);
        assertEquals(result.getPostCode(), null);
        assertEquals(result.getHouseNumber(), null);
        assertEquals(result.getFloor(), null);
        assertEquals(result.getDoor(), null);
    }

    @Test
    public void testParse_StreetWithSpaceAndCity() {
        AddressQuery result = AddressParser.parse(TestData.addresses, "Vej To By");
        assertEquals(result.getStreetName(), "Vej To");
        assertEquals(result.getCity(), "By");
        assertEquals(result.getPostCode(), null);
        assertEquals(result.getHouseNumber(), null);
        assertEquals(result.getFloor(), null);
        assertEquals(result.getDoor(), null);
    }

    @Test
    public void testParse_StreetWithSpaceCommaCity() {
        AddressQuery result = AddressParser.parse(TestData.addresses, "Vej To, By");
        assertEquals(result.getStreetName(), "Vej To");
        assertEquals(result.getCity(), "By");
        assertEquals(result.getPostCode(), null);
        assertEquals(result.getHouseNumber(), null);
        assertEquals(result.getFloor(), null);
        assertEquals(result.getDoor(), null);
    }

    @Test
    public void testParse_CityStreetName() {
        AddressQuery result = AddressParser.parse(TestData.addresses, "By Vej To");
        assertEquals(result.getStreetName(), "Vej To");
        assertEquals(result.getCity(), "By");
        assertEquals(result.getPostCode(), null);
        assertEquals(result.getHouseNumber(), null);
        assertEquals(result.getFloor(), null);
        assertEquals(result.getDoor(), null);
    }

    @Test
    public void testParse_CityStreetNameHouseNumberCommas() {
        AddressQuery result = AddressParser.parse(TestData.addresses, "By, Vej To, 268");
        assertEquals(result.getStreetName(), "Vej To");
        assertEquals(result.getCity(), "By");
        assertEquals(result.getPostCode(), null);
        assertEquals(result.getHouseNumber(), "268");
        assertEquals(result.getFloor(), null);
        assertEquals(result.getDoor(), null);
    }

    @Test
    public void testParse_testParse_CityStreetNameHouseNumber() {
        AddressQuery result = AddressParser.parse(TestData.addresses, "By Vej To 268");
        assertEquals(result.getStreetName(), "Vej To");
        assertEquals(result.getCity(), "By");
        assertEquals(result.getPostCode(), null);
        assertEquals(result.getHouseNumber(), "268");
        assertEquals(result.getFloor(), null);
        assertEquals(result.getDoor(), null);
    }

    @Test
    public void testParse_StreetNameHouseNumberCity() {
        AddressQuery result = AddressParser.parse(TestData.addresses, "Vej To 268 By");
        assertEquals(result.getStreetName(), "Vej To");
        assertEquals(result.getCity(), "By");
        assertEquals(result.getPostCode(), null);
        assertEquals(result.getHouseNumber(), "268");
        assertEquals(result.getFloor(), null);
        assertEquals(result.getDoor(), null);
    }
}
