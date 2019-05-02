package bfst19.danmarkskort.model;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public class AddressSearchTest {
    @Test
    public void testGetAddressesWithStreetName() {
        String streetName = "Vej To";
        List<Address> result = AddressSearch.getAddressesWithStreetName(TestData.addresses, streetName);
        long expectedCount = TestData.addresses.stream()
                .filter(a -> Objects.equals(a.getStreetName(), streetName))
                .count();
        assertEquals(expectedCount, result.size());
        result.forEach(a -> assertSame(a.getStreetName(), streetName));
    }
}
