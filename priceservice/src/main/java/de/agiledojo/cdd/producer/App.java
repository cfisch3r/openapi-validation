/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package de.agiledojo.cdd.producer;

import de.agiledojo.cdd.producer.calculator.DefaultPriceCalculator;
import de.agiledojo.cdd.producer.calculator.PriceCalculator;
import de.agiledojo.cdd.producer.tax.DefaultTaxCalculator;
import de.agiledojo.cdd.producer.tax.TaxCalculator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    public PriceCalculator priceCalculator() {
        DefaultPriceCalculator defaultPriceCalculator = new DefaultPriceCalculator(800);
        defaultPriceCalculator.addDiscount(2,5);
        defaultPriceCalculator.addDiscount(3,10);
        defaultPriceCalculator.addDiscount(4,20);
        defaultPriceCalculator.addDiscount(5,25);
        return defaultPriceCalculator;
    }

    @Bean
    public TaxCalculator taxCalculator() {
        return new DefaultTaxCalculator(0.12);
    }
}
