package bfst19.danmarkskort.model.parsing;

import bfst19.danmarkskort.model.AddressInput;
import bfst19.danmarkskort.utils.BinarySearch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static bfst19.danmarkskort.model.parsing.AddressParsingRegexes.*;

/**
 * Address parser that relies on being able to check parts of the query against a set of data,
 * such as street names, cities, etc. This yield fairly accurate results, but you need to be
 * careful with how you handle large data sets.
 */
public class AddressParserFromData implements AddressParser {
    private final List<String> streetNames;
    private final List<String> cities;

    public AddressParserFromData(List<String> streetNames, List<String> cities) {
        this.streetNames = streetNames;
        this.cities = cities;
    }

    public AddressInput parse(String query) {
        AddressInput addressInput = new AddressInput();
        List<List<String>> splits = getSplits(query);
        List<List<Integer>> usedSplits = new ArrayList<>();

        for (int s = 0; s < splits.size(); s++) {
            List<String> subSplits = fixSplits(splits.get(s));
            usedSplits.add(new ArrayList<>());
            trySetStreet(addressInput, usedSplits, s, subSplits);
            trySetCity(addressInput, usedSplits, s, subSplits);
            trySetPostCode(addressInput, usedSplits, s, subSplits);
            trySetHouseInfo(addressInput, usedSplits, subSplits, 0, s);
        }

        setFromRemainingSplits(
                addressInput,
                AddressInput::getStreetName,
                AddressInput::setStreetName,
                splits,
                usedSplits
        );
        setFromRemainingSplits(
                addressInput,
                AddressInput::getCity,
                AddressInput::setCity,
                splits,
                usedSplits
        );

        return addressInput;
    }

    private void trySetPostCode(AddressInput addressInput, List<List<Integer>> usedSplits, int s, List<String> subSplits) {
        for (int i = 0; i < subSplits.size(); i++) {
            if (usedSplits.get(s).contains(i)) {
                continue;
            }
            String candidate = subSplits.get(i);
            if (addressInput.getPostCode() == null && candidate.matches(postcodeRegex)) {
                addressInput.setPostCode(candidate);
                usedSplits.get(s).add(i);
                break;
            }
        }
    }

    private void trySetStreet(AddressInput addressInput, List<List<Integer>> usedSplits, int s, List<String> subSplits) {
        if (addressInput.getStreetName() != null) {
            return;
        }
        for (int i = 0; i < subSplits.size(); i++) {
            for (int j = subSplits.size(); j > i; j--) {
                String candidate = String.join(" ", subSplits.subList(i, j));
                boolean splitsAvailable = true;
                for (int k = i; k < j; k++) {
                    if (usedSplits.get(s).contains(k)) {
                        splitsAvailable = false;
                        break;
                    }
                }
                if (!splitsAvailable) {
                    continue;
                }
                int streetIndex = BinarySearch.search(streetNames, candidate, String::compareToIgnoreCase);
                if (streetIndex == -1) {
                    continue;
                }
                addressInput.setStreetName(candidate);
                for (int k = i; k < j; k++) {
                    usedSplits.get(s).add(k);
                }
                trySetHouseInfo(addressInput, usedSplits, subSplits, j, s);
                return;
            }
        }
    }

    private void trySetHouseInfo(AddressInput addressInput, List<List<Integer>> usedSplits, List<String> subSplits, int j, int s) {
        for (int k = j; k < subSplits.size(); k++) {
            if (usedSplits.get(s).contains(k)) {
                continue;
            }
            String candidate = subSplits.get(k);
            if (addressInput.getHouseNumber() == null && candidate.matches(houseRegex)) {
                addressInput.setHouseNumber(candidate);
                usedSplits.get(s).add(k);
            } else if (addressInput.getFloor() == null && candidate.matches(floorRegex)) {
                addressInput.setFloor(candidate);
                usedSplits.get(s).add(k);
            } else if (addressInput.getDoor() == null && candidate.matches(doorRegex)) {
                addressInput.setDoor(candidate);
                usedSplits.get(s).add(k);
            }
        }
    }

    private void trySetCity(AddressInput addressInput, List<List<Integer>> usedSplits, int s, List<String> subSplits) {
        if (addressInput.getCity() != null) {
            return;
        }
        for (int i = 0; i < subSplits.size(); i++) {
            for (int j = subSplits.size(); j > i; j--) {
                String candidate = String.join(" ", subSplits.subList(i, j));
                boolean splitsAvailable = true;
                for (int k = i; k < j; k++) {
                    if (usedSplits.get(s).contains(k)) {
                        splitsAvailable = false;
                        break;
                    }
                }
                if (!splitsAvailable) {
                    continue;
                }
                int cityIndex = BinarySearch.search(cities, candidate, String::compareToIgnoreCase);
                if (cityIndex == -1) {
                    continue;
                }
                addressInput.setCity(candidate);
                for (int k = i; k < j; k++) {
                    usedSplits.get(s).add(k);
                }
                return;
            }
        }
    }

    private void setFromRemainingSplits(
            AddressInput addressInput,
            Function<AddressInput, String> getter,
            BiConsumer<AddressInput, String> setter,
            List<List<String>> splits,
            List<List<Integer>> usedSplits
    ) {
        if (getter.apply(addressInput) != null) {
            return;
        }

        int usedSplitCandidate = -1;
        List<Integer> usedSubSplitsCandidate = new ArrayList<>();
        String candidate = null;

        // Try to add whole splits
        for (int i = 0; i < splits.size(); i++) {
            List<String> subSplits = splits.get(i);
            if (listContainsAnyInRange(usedSplits.get(i), 0, subSplits.size())) {
                continue;
            }
            String newCandidate = String.join(" ", subSplits);
            if (candidate != null && newCandidate.length() <= candidate.length()) {
                continue;
            }
            candidate = newCandidate;
            usedSplitCandidate = i;
            usedSubSplitsCandidate = listFromRange(0, subSplits.size());
        }
        // Couldn't add while splits, try adding parts of sub-splits
        if (candidate == null) {
            for (int i = 0; i < splits.size(); i++) {
                List<String> subSplits = splits.get(i);
                for (int j = 0; j < subSplits.size(); j++) {
                    for (int k = subSplits.size(); k > j; k--) {
                        if (listContainsAnyInRange(usedSplits.get(i), j, k)) {
                            continue;
                        }
                        String newCandidate = String.join(" ", subSplits.subList(j, k));
                        if (candidate != null && newCandidate.length() <= candidate.length()) {
                            continue;
                        }
                        candidate = newCandidate;
                        usedSplitCandidate = i;
                        usedSubSplitsCandidate = listFromRange(j, k);
                    }
                }
            }
        }

        if (candidate == null) {
            return;
        }
        setter.accept(addressInput, candidate);
        usedSplits.get(usedSplitCandidate).addAll(usedSubSplitsCandidate);
    }

    private boolean listContainsAnyInRange(List<Integer> list, int start, int end) {
        for (int i = start; i < end; i++) {
            if (list.contains(i)) {
                return true;
            }
        }
        return false;
    }

    private List<Integer> listFromRange(int start, int end) {
        List<Integer> list = new ArrayList<>();
        for (int i = start; i < end; i++) {
            list.add(i);
        }
        return list;
    }

    private List<List<String>> getSplits(String query) {
        ArrayList<List<String>> splits = new ArrayList<>();
        for (String commaSplit : query.split(",")) {
            List<String> subSplits = Arrays.asList(commaSplit.split(" "));
            splits.add(fixSplits(subSplits));
        }
        return splits;
    }

    private List<String> fixSplits(List<String> splits) {
        return splits.stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty() && !s.isBlank())
                .collect(Collectors.toList());
    }
}
