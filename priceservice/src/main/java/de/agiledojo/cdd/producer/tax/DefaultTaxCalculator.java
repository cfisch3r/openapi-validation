package de.agiledojo.cdd.producer.tax;

public class DefaultTaxCalculator implements TaxCalculator{
    private double rate;

    public DefaultTaxCalculator(double rate) {
        this.rate = rate;
    }

    @Override
    public long taxFor(long priceInCent) {
        return Math.round(rate * priceInCent);
    }
}
