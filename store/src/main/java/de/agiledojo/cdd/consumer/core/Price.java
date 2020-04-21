package de.agiledojo.cdd.consumer.core;

import javax.validation.constraints.NotNull;

public class Price {

    @NotNull
    public Long inCent;

    public Price(long priceInCent) {
        inCent =priceInCent;
    }
}
