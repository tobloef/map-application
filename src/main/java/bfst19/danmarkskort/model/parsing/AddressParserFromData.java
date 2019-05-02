package bfst19.danmarkskort.model.parsing;

import bfst19.danmarkskort.model.AddressQuery;
import bfst19.danmarkskort.utils.BinarySearch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static bfst19.danmarkskort.model.parsing.AddressParsingRegexes.*;
import static bfst19.danmarkskort.utils.Misc.trimList;

public class AddressParserFromData implements AddressParser {
    private final List<String> streetNames;
    private final List<String> cities;

    public AddressParserFromData(List<String> streetNames, List<String> cities) {
        this.streetNames = streetNames;
        this.cities = cities;
    }

    public AddressQuery parse(String query) {
        AddressQuery addressQuery = new AddressQuery();
        List<List<String>> splits = getSplits(query);
        List<List<Integer>> usedSplits = new ArrayList<>();

        for (int s = 0; s < splits.size(); s++) {
            List<String> subSplits = splits.get(s);
            usedSplits.add(new ArrayList<>());
            trySetStreet(addressQuery, usedSplits, s, subSplits);
            trySetCity(addressQuery, usedSplits, s, subSplits);
            trySetPostCode(addressQuery, usedSplits, s, subSplits);
            trySetHouseInfo(addressQuery, usedSplits, subSplits, 0, s);
        }

        setFromRemainingSplits(
                addressQuery,
                AddressQuery::getStreetName,
                AddressQuery::setStreetName,
                splits,
                usedSplits
        );
        setFromRemainingSplits(
                addressQuery,
                AddressQuery::getCity,
                AddressQuery::setCity,
                splits,
                usedSplits
        );

        return addressQuery;
    }

    private void trySetPostCode(AddressQuery addressQuery, List<List<Integer>> usedSplits, int s, List<String> subSplits) {
        for (int i = 0; i < subSplits.size(); i++) {
            if (usedSplits.get(s).contains(i)) {
                continue;
            }
            String candidate = subSplits.get(i);
            if (addressQuery.getPostCode() == null && candidate.matches(postcodeRegex)) {
                addressQuery.setPostCode(candidate);
                usedSplits.get(s).add(i);
                break;
            }
        }
    }

    private void trySetStreet(AddressQuery addressQuery, List<List<Integer>> usedSplits, int s, List<String> subSplits) {
        if (addressQuery.getStreetName() != null) {
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
                addressQuery.setStreetName(candidate);
                for (int k = i; k < j; k++) {
                    usedSplits.get(s).add(k);
                }
                trySetHouseInfo(addressQuery, usedSplits, subSplits, j, s);
                return;
            }
        }
    }

    private void trySetHouseInfo(AddressQuery addressQuery, List<List<Integer>> usedSplits, List<String> subSplits, int j, int s) {
        for (int k = j; k < subSplits.size(); k++) {
            if (usedSplits.get(s).contains(k)) {
                continue;
            }
            String candidateAfterStreetName = subSplits.get(k);
            if (addressQuery.getHouseNumber() == null && candidateAfterStreetName.matches(houseRegex)) {
                addressQuery.setHouseNumber(candidateAfterStreetName);
                usedSplits.get(s).add(k);
            } else if (addressQuery.getFloor() == null && candidateAfterStreetName.matches(floorRegex)) {
                addressQuery.setFloor(candidateAfterStreetName);
                usedSplits.get(s).add(k);
            } else if (addressQuery.getDoor() == null && candidateAfterStreetName.matches(doorRegex)) {
                addressQuery.setDoor(candidateAfterStreetName);
                usedSplits.get(s).add(k);
            }
        }
    }

    private void trySetCity(AddressQuery addressQuery, List<List<Integer>> usedSplits, int s, List<String> subSplits) {
        if (addressQuery.getCity() != null) {
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
                addressQuery.setCity(candidate);
                for (int k = i; k < j; k++) {
                    usedSplits.get(s).add(k);
                }
                return;
            }
        }
    }

    private void setFromRemainingSplits(
            AddressQuery addressQuery,
            Function<AddressQuery, String> getter,
            BiConsumer<AddressQuery, String> setter,
            List<List<String>> splits,
            List<List<Integer>> usedSplits
    ) {
        if (getter.apply(addressQuery) != null) {
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
        setter.accept(addressQuery, candidate);
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
            splits.add(trimList(Arrays.asList(commaSplit.split(" "))));
        }
        return splits;
    }
}
