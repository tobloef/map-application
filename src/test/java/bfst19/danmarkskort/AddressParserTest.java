package bfst19.danmarkskort;

import bfst19.danmarkskort.data.AddressTestData;
import bfst19.danmarkskort.data.RealAddresses;
import bfst19.danmarkskort.data.TestAddresses;
import bfst19.danmarkskort.model.AddressInput;
import bfst19.danmarkskort.model.parsing.AddressParser;
import bfst19.danmarkskort.model.parsing.AddressParserFromData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SuppressWarnings("SimplifiableJUnitAssertion")
public class AddressParserTest {
    private AddressTestData testAddresses = new TestAddresses();
    private AddressTestData realAddresses = new RealAddresses();
    private AddressParser addressParserTestData = new AddressParserFromData(
            testAddresses.getStreetNames(),
            testAddresses.getCities()
    );
    private AddressParser addressParserRealData = new AddressParserFromData(
            realAddresses.getStreetNames(),
            realAddresses.getCities()
    );

    @Test
    public void testParse_Street() {
        String query = "Vej Et";
        AddressInput result = addressParserTestData.parse(query);
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
        AddressInput result = addressParserTestData.parse(query);
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
        AddressInput result = addressParserTestData.parse(query);
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
        AddressInput result = addressParserTestData.parse(query);
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
        AddressInput result = addressParserTestData.parse(query);
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
        AddressInput result = addressParserTestData.parse(query);
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
        AddressInput result = addressParserTestData.parse(query);
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
        AddressInput result = addressParserTestData.parse(query);
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
        AddressInput result = addressParserTestData.parse(query);
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
        AddressInput result = addressParserTestData.parse(query);
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
        AddressInput result = addressParserTestData.parse(query);
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
        AddressInput result = addressParserTestData.parse(query);
        assertNotNull(result);
        assertEquals("Vej To", result.getStreetName());
        assertEquals("By Et", result.getCity());
        assertEquals(null, result.getPostCode());
        assertEquals("2", result.getHouseNumber());
        assertEquals(null, result.getFloor());
        assertEquals(null, result.getDoor());
    }



    /////////////// TESTS FROM ORIGINAL ADDRESS PARSER PROJECT ///////////////

    @Test
    public void testCommaSeparated() {
        String query = "Amagerbrogade 268, 7., 709, København S, 2300";
        AddressInput result = addressParserRealData.parse(query);
        assertEquals("Amagerbrogade", result.getStreetName());
        assertEquals("268", result.getHouseNumber());
        assertEquals("7.", result.getFloor());
        assertEquals("709", result.getDoor());
        assertEquals("2300", result.getPostCode());
        assertEquals("København S", result.getCity());
    }

    @Test
    public void testSpaceSeparated() {
        String query = "Amagerbrogade 268 709 København S 2300";
        AddressInput result = addressParserRealData.parse(query);
        assertEquals("Amagerbrogade", result.getStreetName());
        assertEquals("268", result.getHouseNumber());
        assertEquals("709", result.getDoor());
        assertEquals("2300", result.getPostCode());
        assertEquals("København S", result.getCity());
    }

    @Test
    public void testSpecialCharactersInStreetName() {
        String query = "H/F Solpl-Clausensv. 14 th.";
        AddressInput result = addressParserRealData.parse(query);
        assertEquals("H/F Solpl-Clausensv.", result.getStreetName());
        assertEquals("14", result.getHouseNumber());
        assertEquals("th.", result.getDoor());
    }

    @Test
    public void testFourNumberedEverything() {
        String query = "Hf. Haveselskabet 1936 876 876 8766 Nr. Snede";
        AddressInput result = addressParserRealData.parse(query);
        assertEquals("Hf. Haveselskabet 1936", result.getStreetName());
        assertEquals("876", result.getHouseNumber());
        assertEquals("876", result.getDoor());
        assertEquals("8766", result.getPostCode());
        assertEquals("Nr. Snede", result.getCity());
    }

    @Test
    public void testConfusingNumbers() {
        String query = "Hf. Haveselskabet 1936 19 66 1799";
        AddressInput result = addressParserRealData.parse(query);
        assertEquals("Hf. Haveselskabet 1936", result.getStreetName());
        assertEquals("19", result.getHouseNumber());
        assertEquals("66", result.getFloor());
        assertEquals("1799", result.getPostCode());
    }

    @Test
    public void testUnicodeCharacters() {
        String query = "Konsul Beÿers Allé 54";
        AddressInput result = addressParserRealData.parse(query);
        assertEquals("Konsul Beÿers Allé", result.getStreetName());
        assertEquals("54", result.getHouseNumber());
    }

    @Test
    public void NumberInStreetNameWhole(){
        String query = "Haveforeningen Af 10. Maj 1918 44 St. Th., 8260 Viby J";
        AddressInput result = addressParserRealData.parse(query);
        assertEquals("Haveforeningen Af 10. Maj 1918", result.getStreetName());
        assertEquals( "44", result.getHouseNumber());
        assertEquals( "8260", result.getPostCode());
        assertEquals( "Viby J",result.getCity());
        assertEquals("St.", result.getFloor());
        assertEquals( "Th.",result.getDoor());
    }

    @Test
    public void NumberInStreetNameWithoutFloor(){
        String query = "Haveforeningen Af 10. Maj 1918 44 8260 Viby J";
        AddressInput result = addressParserRealData.parse(query);
        assertEquals("Haveforeningen Af 10. Maj 1918", result.getStreetName());
        assertEquals( "44", result.getHouseNumber());
        assertEquals( "8260", result.getPostCode());
        assertEquals( "Viby J",result.getCity());
    }

    @Test
    public void testNumberInStreetNameStreetAndNumber(){
        String query = "Haveforeningen Af 10. Maj 1918 44";
        AddressInput result = addressParserRealData.parse(query);
        assertEquals("Haveforeningen Af 10. Maj 1918", result.getStreetName());
        assertEquals( "44", result.getHouseNumber());
    }

    @Test
    public void NumberInStreetNameJustStreet(){
        String query = "Haveforeningen Af 10. Maj 1918";
        AddressInput result = addressParserRealData.parse(query);
        assertEquals( "Haveforeningen Af 10. Maj 1918", result.getStreetName());
    }


    @Test
    public void NumberInStartOfStreetNameOne(){
        String query = "10. Februar Vej 11 6070 Christiansfeld";
        AddressInput result = addressParserRealData.parse(query);
        assertEquals("10. Februar Vej", result.getStreetName());
        assertEquals( "11", result.getHouseNumber());
        assertEquals( "6070", result.getPostCode());
        assertEquals( "Christiansfeld", result.getCity());
    }

    @Test
    public void NumberInStartOfStreetNameTwo(){
        String query = "10. Februar Vej 11 6070";
        AddressInput result = addressParserRealData.parse(query);
        assertEquals("10. Februar Vej", result.getStreetName());
        assertEquals( "11", result.getHouseNumber());
        assertEquals( "6070", result.getPostCode());
    }

    @Test
    public void NumberInStartOfStreetNameThree(){
        String query = "10. Februar Vej 11 Christiansfeld";
        AddressInput result = addressParserRealData.parse(query);
        assertEquals("10. Februar Vej", result.getStreetName());
        assertEquals( "11", result.getHouseNumber());
        assertEquals( "Christiansfeld", result.getCity());
    }

    @Test
    public void NumberInStartOfStreetNameFour(){
        String query = "10. Februar Vej 11";
        AddressInput result = addressParserRealData.parse(query);
        assertEquals("10. Februar Vej", result.getStreetName());
        assertEquals( "11", result.getHouseNumber());
    }


    @Test
    public void DotInCityName(){
        String query = "Engelsborgvej 28 2800 Kgs. Lyngby";
        AddressInput result = addressParserRealData.parse(query);
        assertEquals("Engelsborgvej", result.getStreetName());
        assertEquals("28", result.getHouseNumber());
        assertEquals("2800", result.getPostCode());
        assertEquals("Kgs. Lyngby", result.getCity());
    }

    @Test
    public void LetterInStreetNumber(){
        String query = "Engelsborgvej 28E 2800 Kongens Lyngby";
        AddressInput result = addressParserRealData.parse(query);
        assertEquals("Engelsborgvej", result.getStreetName());
        assertEquals("28E", result.getHouseNumber());
        assertEquals("2800", result.getPostCode());
        assertEquals("Kongens Lyngby", result.getCity());
    }

    @Test
    public void MyAddressGeneric(){
        String query = "Hf. Havebyen Mozart 55, 2450 København SV";
        AddressInput result = addressParserRealData.parse(query);
        assertEquals("Hf. Havebyen Mozart", result.getStreetName());
        assertEquals("55", result.getHouseNumber());
        assertEquals("2450", result.getPostCode());
        assertEquals("København SV", result.getCity());
    }

    @Test
    public void ParanthesesInStreetName(){
        String query = "Skovbrynet (9850) 4, 9850 Hirtshals";
        AddressInput result = addressParserRealData.parse(query);
        assertEquals("Skovbrynet (9850)", result.getStreetName());
        assertEquals("4", result.getHouseNumber());
        assertEquals("9850", result.getPostCode());
        assertEquals("Hirtshals", result.getCity());
    }
}
