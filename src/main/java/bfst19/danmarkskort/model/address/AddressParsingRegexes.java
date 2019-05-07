package bfst19.danmarkskort.model.address;

public class AddressParsingRegexes {
	public static final String houseRegex = "(?<house>(?i)[0-9]{1,3}[a-z]?)";
    public static final String floorRegex = "(?<floor>(?i)(st|[0-9]{1,2})(th|nd|st|rd)?\\.?)";
    public static final String doorRegex = "(?<door>(?i)((tv|th|mf)|([a-z]?[0-9]{1,3}))\\.?)";
    public static final String postcodeRegex = "(?<postCode>[0-9]{4})";
}
