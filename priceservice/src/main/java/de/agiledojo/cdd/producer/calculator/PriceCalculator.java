package de.agiledojo.cdd.producer.calculator;

import java.util.List;

public interface PriceCalculator {
    long priceFor(List<BOOKS> purchasedBooks);
    long taxFor(long priceInCent);


    public enum BOOKS {I,II,III,IV,V}
}
