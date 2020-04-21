package de.agiledojo.cdd.producer.rest;

public class Price {

    public long inCent;

    public long tax;

    public Price(long inCent, long tax) {
        this.inCent = inCent;
        this.tax = tax;
    }
}
