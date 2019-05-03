package bfst19.danmarkskort.model;

import bfst19.danmarkskort.model.parsing.AddressParser;
import bfst19.danmarkskort.model.parsing.AddressParserFromData;
import bfst19.danmarkskort.utils.BinarySearch;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AddressSearch {
    private List<Address> addressesByStreetName;
    private List<Address> addressesByCity;
    private List<String> streetNames;
    private List<String> cities;
    private AddressParser addressParser;

    public AddressSearch(
            List<Address> addressesByStreetName,
            List<Address> addressesByCity,
            List<String> streetNames,
            List<String> cities
    ) {
        this.addressesByStreetName = addressesByStreetName;
        this.addressesByCity = addressesByCity;
        this.streetNames = streetNames;
        this.cities = cities;
        addressParser = new AddressParserFromData(streetNames, cities);
    }

    public List<Pair<String, Address>> getSuggestions(String stringQuery) {
        AddressQuery addressQuery = addressParser.parse(stringQuery);
        if (addressQuery == null) {
            return null;
        }
        List<Address> matches = null;
        if (addressQuery.getStreetName() != null) {
            matches = getAddressesWithStreetName(addressQuery.getStreetName());
        }
        if (matches == null && addressQuery.getCity() != null) {
            matches = getAddressesWithCity(addressQuery.getCity());
            matches.sort(Comparator.comparing(Address::getStreetName));
        }
        List<Pair<String, Address>> suggestions;
        if (matches != null) {
            suggestions = matches.stream()
                    .filter(a -> searchIfQueryNotNull(addressQuery, a, AddressQuery::getStreetName, Address::getStreetName))
                    .filter(a -> searchIfQueryNotNull(addressQuery, a, AddressQuery::getCity, Address::getCity))
                    .filter(a -> searchIfQueryNotNull(addressQuery, a, AddressQuery::getHouseNumber, Address::getHouseNumber))
                    .map(a -> new Pair<>(a.toString(), a))
                    .collect(Collectors.toList());
        } else {
            suggestions = streetNames.stream()
                    .filter(s -> s.toLowerCase().startsWith(stringQuery.toLowerCase()))
                    .map(s -> new Pair<String, Address>(s, null))
                    .collect(Collectors.toList());
            if (suggestions.size() == 0) {
                suggestions = cities.stream()
                        .filter(s -> s.toLowerCase().startsWith(stringQuery.toLowerCase()))
                        .map(s -> new Pair<String, Address>(s, null))
                        .collect(Collectors.toList());
            }
        }
        return suggestions;
    }

    public List<Address> getAddressesWithStreetName(String streetName) {
        return getAddressesWithProperty(
                addressesByStreetName,
                streetName,
                Address::getStreetName,
                String::compareToIgnoreCase
        );
    }

    public List<Address> getAddressesWithCity(String city) {
        return getAddressesWithProperty(
                addressesByCity,
                city,
                Address::getCity,
                String::compareToIgnoreCase
        );
    }

    private <T> List<Address> getAddressesWithProperty(List<Address> addresses, T value, Function<Address, T> getter, Comparator<T> comparator) {
        int middle = BinarySearch.search(addresses, value, getter, comparator);
        if (middle == -1) {
            return null;
        }
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

    private boolean searchIfQueryNotNull(
            AddressQuery query,
            Address address,
            Function<AddressQuery, String> queryGetter,
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
