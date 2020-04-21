package de.agiledojo.cdd.producer.calculator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DefaultPriceCalculator implements PriceCalculator {

    private int singleBookPrice;

    private Map<Long,Double> discountsForSeries = new HashMap<>();

    public DefaultPriceCalculator(int singleBookPrice) {
        this.singleBookPrice = singleBookPrice;
    }

    @Override
    public long priceFor(List<BOOKS> purchasedBooks) {
        List<BOOKS> series = largestSeriesIn(purchasedBooks);
        if (series.size() > 1)
            return seriesPrice(series.size()) + priceFor(substract(purchasedBooks, series));
        else
            return basePrice(purchasedBooks);
    }

    private int basePrice(List<BOOKS> purchasedBooks) {
        return purchasedBooks.size() * singleBookPrice;
    }

    private List<BOOKS> largestSeriesIn(List<BOOKS> purchasedBooks) {
        return purchasedBooks.stream().distinct().collect(Collectors.toList());
    }

    private long seriesPrice(int seriesLength) {
        return Math.round(seriesLength * singleBookPrice * getDiscount(seriesLength));
    }

    private List<BOOKS> substract(List<BOOKS> books, List<BOOKS> otherBooks) {
        List<BOOKS> remainingBooks = new ArrayList<>(books);
        for (BOOKS book :otherBooks)
            remainingBooks.remove(book);
        return remainingBooks;
    }

    public void addDiscount(long seriesLength,double discount) {
        discountsForSeries.put(seriesLength,discount);
    }

    private double getDiscount(long seriesLength) {
        Double discount = discountsForSeries.get(seriesLength);
        return 1 - discount.doubleValue();
    }
}