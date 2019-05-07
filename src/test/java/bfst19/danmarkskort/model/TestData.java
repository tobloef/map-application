package bfst19.danmarkskort.model;

import bfst19.danmarkskort.model.address.Address;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TestData {
    private final static List<Address> ADDRESSES = new ArrayList<>(Arrays.asList(
            new Address(0, 0, "Vej Et", "1", "By Et"),
            new Address(0, 0, "Vej Et", "2", "By Et"),
            new Address(0, 0, "Vej Et", "3", "By Et"),
            new Address(0, 0, "Vej To", "1", "By Et"),
            new Address(0, 0, "Vej To", "2", "By Et"),
            new Address(0, 0, "Vej To", "1", "By To"),
            new Address(0, 0, "Vej To", "2", "By To"),
            new Address(0, 0, "Vej To", "3", "By To"),
            new Address(0, 0, "Vej To", "11", "By To"),
            new Address(0, 0, "Vej To", "12", "By To"),
            new Address(0, 0, "Vej Tre", "1", "By To"),
            new Address(0, 0, "Vej Tre", "2", "By To"),
            new Address(0, 0, "Vej Tre", "3", "By To")
    ));

    public final static List<Address> ADDRESSES_BY_STREET_NAME_1 = ADDRESSES.stream()
            .sorted(Comparator.comparing(Address::getStreetName))
            .collect(Collectors.toList());

    public final static List<Address> ADDRESSES_BY_CITY_1 = ADDRESSES.stream()
            .sorted(Comparator.comparing(Address::getCity))
            .collect(Collectors.toList());

    public final static List<String> CITIES_1 = TestData.ADDRESSES.stream()
            .map(Address::getCity)
            .distinct()
            .collect(Collectors.toList());


    public final static List<String> STREET_NAMES_1 = TestData.ADDRESSES.stream()
            .map(Address::getStreetName)
            .distinct()
            .collect(Collectors.toList());
}
