package de.agiledojo.cdd.producer.tax;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DefaultTaxCalculatorTest {

    public static final double TAX_RATE = 0.19;

    @Test
    void should_calculate_tax() {
        DefaultTaxCalculator calculator = new DefaultTaxCalculator(TAX_RATE);
        long tax = calculator.taxFor(200);
        assertThat(tax).isEqualTo(38);
    }
}
