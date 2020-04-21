package de.agiledojo.cdd.producer.calculator;

import java.util.List;

public interface PriceCalculator {
    long priceFor(List<BOOKS> purchasedBooks);

    public enum BOOKS {I,II,III,IV,V}
}
