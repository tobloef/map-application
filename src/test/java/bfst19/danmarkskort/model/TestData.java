package bfst19.danmarkskort.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TestData {
    public final static List<Address> addresses = new ArrayList<>(Arrays.asList(
            new Address(0, 0, "Vej Et", "1", "By Et"),
            new Address(0, 0, "Vej Et", "2", "By Et"),
            new Address(0, 0, "Vej Et", "3", "By Et"),
            new Address(0, 0, "Vej To", "1", "By Et"),
            new Address(0, 0, "Vej To", "2", "By Et"),
            new Address(0, 0, "Vej To", "1", "By To"),
            new Address(0, 0, "Vej To", "2", "By To"),
            new Address(0, 0, "Vej To", "3", "By To"),
            new Address(0, 0, "Vej Tre", "1", "By To"),
            new Address(0, 0, "Vej Tre", "2", "By To"),
            new Address(0, 0, "Vej Tre", "3", "By To")
    ));

    public final static List<String> cities = TestData.addresses.stream()
            .map(Address::getCity)
            .distinct()
            .collect(Collectors.toList());
}
