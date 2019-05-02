package bfst19.danmarkskort.model.parsing;

import bfst19.danmarkskort.model.Address;
import bfst19.danmarkskort.model.AddressQuery;
import bfst19.danmarkskort.model.AddressSearch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AddressParser {
    // The regular expressions below are based on the actual legal requirements.
    private static String streetRegex = "(?<street>.+?)";
    private static String houseRegex = "(?<house>(?i)[0-9]{1,3}[a-z]?)";
    private static String floorRegex = "(?<floor>(?i)(st|[0-9]{1,2})(th|nd|st|rd)?\\.?)";
    private static String doorRegex = "(?<door>(?i)((tv|th|mf)|([a-z]?[0-9]+))\\.?)";
    private static String postcodeRegex = "(?<postCode>[0-9]{4})";
    private static String cityRegex = "(?<city>\\p{Lu}(\\p{L}+[\\p{L}\\-.]*( \\p{L}+)?))";
    private static String fillRegex = "[ ,]+";
    private static String floorDoorRegex = "(?<floor>(?i)((st|[0-9]{1,2})\\.?))(?<door>(?i)(tv|th|mf)\\.?)";

    // These are the relevant permutations of the regex patterns above.
    // Inputted addresses should hopefully match any of these.
    // TODO: Generate these permutations based on a priority hierarchy
    private static String[][] patterns = {
            {streetRegex, houseRegex, postcodeRegex, cityRegex, floorDoorRegex},
            {streetRegex, houseRegex, floorDoorRegex, postcodeRegex, cityRegex},

            {streetRegex, houseRegex, floorRegex, doorRegex, postcodeRegex, cityRegex},
            {streetRegex, houseRegex, floorRegex, doorRegex, cityRegex, postcodeRegex},

            {streetRegex, houseRegex, floorRegex, doorRegex, postcodeRegex},
            {streetRegex, houseRegex, floorRegex, doorRegex, cityRegex},

            {streetRegex, houseRegex, floorRegex, postcodeRegex, cityRegex},
            {streetRegex, houseRegex, doorRegex, postcodeRegex, cityRegex},
            {streetRegex, houseRegex, floorRegex, cityRegex, postcodeRegex},
            {streetRegex, houseRegex, doorRegex, cityRegex, postcodeRegex},

            {streetRegex, houseRegex, postcodeRegex, cityRegex},

            {streetRegex, houseRegex, floorRegex, postcodeRegex},
            {streetRegex, houseRegex, postcodeRegex},

            {streetRegex, houseRegex, cityRegex},

            {streetRegex, houseRegex, doorRegex, postcodeRegex},
            {streetRegex, houseRegex, doorRegex, cityRegex},
            {streetRegex, houseRegex, floorRegex, cityRegex},

            {streetRegex, houseRegex, floorRegex, doorRegex},
            {streetRegex, houseRegex, doorRegex, floorRegex},

            {streetRegex, houseRegex, floorRegex},
            {streetRegex, houseRegex, doorRegex},

            {streetRegex, houseRegex},

            {streetRegex, cityRegex, postcodeRegex},

            {streetRegex},
    };

    private final static Pattern[] compiledPatterns = Arrays.stream(patterns)
            .map(p -> String.join(fillRegex, p))
            .map(Pattern::compile)
            .toArray(Pattern[]::new);

    private static String tryGetGroup(Matcher matcher, String group) {
        try {
            return matcher.group(group);
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }

    public static AddressQuery parse(List<Address> addresses, List<String> cities, String query) {
        List<String> commaSplits = Arrays.asList(query.split(","));
        commaSplits = trimList(commaSplits);
        // TODO: Implement this
        return null;
    }

    private static List<String> trimList(List<String> list) {
        return list.stream()
                .map(String::trim)
                .collect(Collectors.toList());
    }

    private static AddressQuery parseWithPatterns(String query) {
        // We simply try each pattern until a match is found
        for (Pattern pattern : compiledPatterns) {
            Matcher matcher = pattern.matcher(query.trim());
            if (matcher.matches()) {
                // Try to get the properties from each named group, ignoring the group if it isn't found.
                return new AddressQuery.Builder()
                        .streetName(tryGetGroup(matcher, "street"))
                        .houseNumber(tryGetGroup(matcher, "house"))
                        .floor(tryGetGroup(matcher, "floor"))
                        .door(tryGetGroup(matcher, "door"))
                        .postCode(tryGetGroup(matcher, "postCode"))
                        .city(tryGetGroup(matcher, "city"))
                        .build();
            }

        }
        return null;
    }
}
