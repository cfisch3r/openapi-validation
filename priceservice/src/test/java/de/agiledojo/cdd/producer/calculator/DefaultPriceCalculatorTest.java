package de.agiledojo.cdd.producer.calculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static de.agiledojo.cdd.producer.calculator.PriceCalculator.BOOKS.*;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class DefaultPriceCalculatorTest {

    public static final int SINGLE_BOOK_PRICE = 800;
    private DefaultPriceCalculator store;

    @BeforeEach
    void setUp() {
        store = new DefaultPriceCalculator(SINGLE_BOOK_PRICE);
        store.addDiscount(2,0.05);
        store.addDiscount(3,0.10);
        store.addDiscount(4,0.20);
        store.addDiscount(5,0.25);
    }

    @Test
    void singleBookPrice() {
        long price = store.priceFor(asList(I));
        assertThat(price).isEqualTo(SINGLE_BOOK_PRICE);
    }

    @Test
    void multipleIdenticalBooks() {
        long price = store.priceFor(asList(I,I));
        assertThat(price).isEqualTo(2 * SINGLE_BOOK_PRICE);
    }

    @Test
    void twoBookSeriesGetsDiscount() {
        long price = store.priceFor(asList(I,II));
        assertThat(price).isEqualTo(Math.round(2 * SINGLE_BOOK_PRICE * (1-0.05)));
    }

    @Test
    void threeBookSeriesGetsDiscount() {
        long price = store.priceFor(asList(I,II,III));
        assertThat(price).isEqualTo(Math.round(3 * SINGLE_BOOK_PRICE * (1-0.10)));
    }

    @Test
    void fourBookSeriesGetsDiscount() {
        long price = store.priceFor(asList(I,II,III,IV));
        assertThat(price).isEqualTo(Math.round(4 * SINGLE_BOOK_PRICE * (1-0.20)));
    }

    @Test
    void fiveBookSeriesGetsDiscount() {
        long price = store.priceFor(asList(I,II,III,IV,V));
        assertThat(price).isEqualTo(Math.round(5 * SINGLE_BOOK_PRICE * (1-0.25)));
    }

    @Test
    void combination() {
        long price = store.priceFor(asList(I,I,II));
        assertThat(price).isEqualTo(Math.round(SINGLE_BOOK_PRICE + (2 * SINGLE_BOOK_PRICE * (1-0.05))));
    }
}
