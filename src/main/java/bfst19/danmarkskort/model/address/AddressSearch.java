package bfst19.danmarkskort.model.address;

import bfst19.danmarkskort.utils.BinarySearch;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AddressSearch {
    private final AddressData addressData;
    private final AddressParser addressParser;

    public AddressSearch(AddressData addressData) {
        this.addressData = addressData;
        addressParser = new AddressParserFromData(
                addressData.getStreetNames(),
                addressData.getCities()
        );
    }

    public List<Pair<String, Address>> getSuggestions(String stringQuery) {
        AddressInput addressInput = addressParser.parse(stringQuery);
        if (addressInput == null) {
            return null;
        }
        List<Address> matches = getPossibleAddressMatches(addressInput);

        List<Pair<String, Address>> suggestions;
        if (matches != null) {
            // If any addresses were found matching the search query, get the suggestions from those.
            suggestions = getSuggestionsFromMatches(matches, addressInput);
        } else {
            // If no addresses have been found so far, suggest street names or cities.
            suggestions = getSuggestionsFromStreetsAndCities(stringQuery);
        }
        return suggestions;
    }

    private List<Address> getPossibleAddressMatches(AddressInput addressInput) {
        List<Address> matches = new ArrayList<>();
        if (addressInput.getStreetName() != null) {
            matches = getAddressesWithStreetName(addressInput.getStreetName());
        }
        if (matches == null && addressInput.getCity() != null) {
            matches = getAddressesWithCity(addressInput.getCity());
            matches.sort(Comparator.comparing(Address::getStreetName));
        }
        return matches;
    }

    private List<Pair<String, Address>> getSuggestionsFromStreetsAndCities(String stringQuery) {
        // Try to get suggestions from street names.
        List<Pair<String, Address>> suggestions;
        suggestions = addressData.getStreetNames().stream()
                .filter(s -> s.toLowerCase().startsWith(stringQuery.toLowerCase()))
                .map(s -> new Pair<String, Address>(s, null))
                .collect(Collectors.toList());
        // If no suggestions were found, try go get them from the city names.
        if (suggestions.size() == 0) {
            suggestions = addressData.getCities().stream()
                    .filter(s -> s.toLowerCase().startsWith(stringQuery.toLowerCase()))
                    .map(s -> new Pair<String, Address>(s, null))
                    .collect(Collectors.toList());
        }
        return suggestions;
    }

    private List<Pair<String, Address>> getSuggestionsFromMatches(List<Address> addresses, AddressInput addressInput) {
        List<Pair<String, Address>> list = new ArrayList<>();
        for (Address a : addresses) {
            if (!addressPropertyMatchesQuery(addressInput, a, AddressInput::getStreetName, Address::getStreetName)) {
                continue;
            }
            if (!addressPropertyMatchesQuery(addressInput, a, AddressInput::getCity, Address::getCity)) {
                continue;
            }
            if (!addressPropertyMatchesQuery(addressInput, a, AddressInput::getHouseNumber, Address::getHouseNumber)) {
                continue;
            }
            Pair<String, Address> stringAddressPair = new Pair<>(a.toString(), a);
            list.add(stringAddressPair);
        }
        return list;
    }

    public List<Address> getAddressesWithStreetName(String streetName) {
        return getAddressesWithProperty(
                addressData.getAddressesByStreetName(),
                streetName,
                Address::getStreetName,
                String::compareToIgnoreCase
        );
    }

    public List<Address> getAddressesWithCity(String city) {
        return getAddressesWithProperty(
                addressData.getAddressesByCity(),
                city,
                Address::getCity,
                String::compareToIgnoreCase
        );
    }

    private <T> List<Address> getAddressesWithProperty(List<Address> addresses, T value, Function<Address, T> getter, Comparator<T> comparator) {
        // Find some index of an address matching the query.
        int middle = BinarySearch.search(addresses, value, getter, comparator);
        if (middle == -1) {
            return null;
        }
        // Expand the range of indexes, to get all matching the query.
        int low = middle;
        int high = middle;
        while (low > 0) {
            if (!Objects.equals(getter.apply(addresses.get(low - 1)), value)) {
                break;
            }
            low--;
        }
        while (high + 2 < addresses.size()) {
            if (!Objects.equals(getter.apply(addresses.get(high + 1)), value)) {
                break;
            }
            high++;
        }
        return addresses.subList(low, high + 1);
    }

    private boolean addressPropertyMatchesQuery(
            AddressInput query,
            Address address,
            Function<AddressInput, String> queryGetter,
            Function<Address, String> addressGetter
    ) {
        String queryString = queryGetter.apply(query);
        String addressString = addressGetter.apply(address);
        if (queryString != null && addressString != null) {
            boolean isEqual = addressString.equalsIgnoreCase(queryString);
            boolean startsWith = addressString.startsWith(queryString);
            return isEqual || startsWith;
        }
        return true;
    }
}
