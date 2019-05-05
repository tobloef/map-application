package bfst19.danmarkskort.model.parsing;

public class AddressParsingRegexes {
    // The regular expressions below are based on the actual legal requirements.
    public static String streetRegex = "(?<street>.+?)";
    public static String houseRegex = "(?<house>(?i)[0-9]{1,3}[a-z]?)";
    public static String floorRegex = "(?<floor>(?i)(st|[0-9]{1,2})(th|nd|st|rd)?\\.?)";
    public static String doorRegex = "(?<door>(?i)((tv|th|mf)|([a-z]?[0-9]+))\\.?)";
    public static String postcodeRegex = "(?<postCode>[0-9]{4})";
    public static String cityRegex = "(?<city>\\p{Lu}(\\p{L}+[\\p{L}\\-.]*( \\p{L}+)?))";
    public static String fillRegex = "[ ,]+";
    public static String floorDoorRegex = "(?<floor>(?i)((st|[0-9]{1,2})\\.?))(?<door>(?i)(tv|th|mf)\\.?)";
}
