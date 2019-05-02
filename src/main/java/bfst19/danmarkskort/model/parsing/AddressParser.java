package bfst19.danmarkskort.model.parsing;

import bfst19.danmarkskort.model.AddressQuery;

public interface AddressParser {
    public AddressQuery parse(String query);
}
