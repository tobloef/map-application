package bfst19.danmarkskort.model.parsing;

import bfst19.danmarkskort.model.Address;
import bfst19.danmarkskort.model.AddressQuery;
import bfst19.danmarkskort.model.AddressSearch;
import bfst19.danmarkskort.utils.BinarySearch;

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
        AddressQuery addressQuery = new AddressQuery();
        for (String commaSplit : commaSplits) {
            List<String> splits = Arrays.asList(commaSplit.split(" "));
            if (addressQuery.getStreetName() == null) {
                trySetStreetName(addresses, cities, addressQuery, splits);
                if (addressQuery.getStreetName() != null) {
                    continue;
                }
            }
            if (addressQuery.getCity() == null) {
                trySetCity(cities, addressQuery, splits, 0, splits.size());
                if (addressQuery.getCity() != null) {
                    continue;
                }
            }
            if (addressQuery.getHouseNumber() == null) {
                trySetHouseNumber(addressQuery, splits, 0);
            }
        }
        return addressQuery;
    }

    private static void trySetStreetName(List<Address> addresses, List<String> cities, AddressQuery addressQuery, List<String> splits) {
        for (int i = 0; i < splits.size(); i++) {
            for (int j = splits.size(); j > 0; j--) {
                String candidate = String.join(" ", splits.subList(i, j));
                int streetIndex = BinarySearch.search(addresses, candidate, Address::getStreetName, String::compareToIgnoreCase);
                if (streetIndex != -1) {
                    addressQuery.setStreetName(candidate);
                    // Try set house
                    trySetHouseNumber(addressQuery, splits, j);
                    // Try set city
                    trySetCity(cities, addressQuery, splits, 0, i);
                    if (addressQuery.getCity() == null) {
                        trySetCity(cities, addressQuery, splits, j, splits.size());
                    }
                    return;
                }
            }
        }
    }

    private static void trySetHouseNumber(AddressQuery addressQuery, List<String> splits, int startIndex) {
        if (startIndex < splits.size()) {
            String houseCandidate = splits.get(startIndex);
            if (houseCandidate.matches(houseRegex)) {
                addressQuery.setHouseNumber(houseCandidate);
            }
        }
    }

    private static void trySetCity(List<String> cities, AddressQuery addressQuery, List<String> splits, int startIndex, int endIndex) {
        for (int i = startIndex; i < endIndex; i++) {
            for (int j = endIndex - 1; j >= startIndex; j--) {
                String candidate = String.join(" ", splits.subList(i, j + 1));
                int cityIndex = BinarySearch.search(cities, candidate, String::compareToIgnoreCase);
                if (cityIndex != -1) {
                    addressQuery.setCity(candidate);
                    return;
                }
            }
        }
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
