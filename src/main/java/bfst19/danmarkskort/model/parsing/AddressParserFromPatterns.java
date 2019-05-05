package bfst19.danmarkskort.model.parsing;

import bfst19.danmarkskort.model.AddressQuery;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static bfst19.danmarkskort.model.parsing.AddressParsingRegexes.*;

/**
 * Address parser that relies purely on pattern matching and doens't need any data about street names, cities, etc.
 * This is somewhat useful for very large data sets, but the results aren't as accurate as they could be with the data.
 */
public class AddressParserFromPatterns implements AddressParser {
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

    @Override
    public AddressQuery parse(String query) {
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
