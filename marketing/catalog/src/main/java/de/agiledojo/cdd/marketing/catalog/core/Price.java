package de.agiledojo.cdd.marketing.catalog.core;

import javax.validation.constraints.NotNull;

public class Price {

    @NotNull
    public Long inCent;
    public Long tax;

    public Price(long priceInCent, long tax) {
        inCent = priceInCent;
        this.tax = tax;
    }
}

