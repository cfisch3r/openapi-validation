package de.agiledojo.cdd.producer.rest;

import de.agiledojo.cdd.producer.calculator.PriceCalculator;
import de.agiledojo.cdd.producer.calculator.PriceCalculator.BOOKS;
import de.agiledojo.cdd.producer.tax.TaxCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PriceApiController {

    private PriceCalculator calculator;

    @Autowired
    public PriceApiController(PriceCalculator calculator, TaxCalculator taxCalculator) {
        this.calculator = calculator;
    }


    @PostMapping("/price")
    public Price priceFor(@RequestBody  List<String> bookIds) {
        ArrayList<BOOKS> books = new ArrayList<>();
        for (String bookId : bookIds)
            books.add(BOOKS.valueOf(bookId));

        long priceInCent = calculator.priceFor(books);
        return new Price(priceInCent,calculator.taxFor(priceInCent));
    }
}
