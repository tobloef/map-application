package bfst19.danmarkskort.model;

import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class AddressSearchTest {
    private static List<Address> addresses = new ArrayList<>(Arrays.asList(
            new Address(0, 0, "Vej 1", "1", "By 1"),
            new Address(0, 0, "Vej 1", "2", "By 1"),
            new Address(0, 0, "Vej 1", "3", "By 1"),
            new Address(0, 0, "Vej 2", "1", "By 1"),
            new Address(0, 0, "Vej 2", "2", "By 1"),
            new Address(0, 0, "Vej 2", "3", "By 1"),
            new Address(0, 0, "Vej 2", "4", "By 1"),
            new Address(0, 0, "Vej 3", "1", "By 2"),
            new Address(0, 0, "Vej 3", "2", "By 2"),
            new Address(0, 0, "Vej 3", "3", "By 2")
    ));

    @Test
    public void testGetAddressesWithStreetName() {
        String streetName = "Vej 2";
        List<Address> result = AddressSearch.getAddressesWithStreetName(addresses, streetName);
        long expectedCount = addresses.stream().filter(a -> Objects.equals(a.getStreetName(), streetName)).count();
        assertEquals(expectedCount, result.size());
        result.forEach(a -> assertSame(a.getStreetName(), streetName));
    }
}
