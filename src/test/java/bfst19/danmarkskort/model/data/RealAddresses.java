package bfst19.danmarkskort.model.data;

import bfst19.danmarkskort.model.Address;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RealAddresses extends AddressData {
    @Override
    public List<Address> getAddresses() {
        return new ArrayList<>(Arrays.asList(
                new Address(0, 0, "Amagerbrogade", "268", "København S"),
                new Address(0, 0, "H/F Solpl-Clausensv.", "14", "Holte"),
                new Address(0, 0, "Hf. Haveselskabet 1936", "876", "Nr. Snede"),
                new Address(0, 0, "Konsul Beÿers Allé", "54", "Holbæk"),
                new Address(0, 0, "Haveforeningen Af 10. Maj 1918", "44", "Viby J"),
                new Address(0, 0, "10. Februar Vej", "11", "Christiansfeld"),
                new Address(0, 0, "Engelsborgvej", "28", "Kgs. Lyngby"),
                new Address(0, 0, "Hf. Havebyen Mozart", "55", "København SV"),                new Address(0, 0, "Engelsborgvej", "28E", "Kgs. Lyngby"),
                new Address(0, 0, "Skovbrynet (9850)", "4", "Hirtshals")
        ));
    }
}
