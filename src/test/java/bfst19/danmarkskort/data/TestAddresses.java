package bfst19.danmarkskort.data;

import bfst19.danmarkskort.model.Address;
import bfst19.danmarkskort.model.AddressData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestAddresses extends AddressTestData {
    @Override
    public List<Address> getAddresses() {
        return new ArrayList<>(Arrays.asList(
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
    }
}
