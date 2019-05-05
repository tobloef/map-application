package bfst19.danmarkskort.model.parsing;

import bfst19.danmarkskort.model.AddressInput;

public interface AddressParser {
    public AddressInput parse(String query);
}
