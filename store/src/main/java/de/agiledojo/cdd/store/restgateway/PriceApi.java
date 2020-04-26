package de.agiledojo.cdd.store.restgateway;

import de.agiledojo.cdd.store.core.Price;
import feign.Headers;
import feign.RequestLine;

import java.util.List;

public interface PriceApi {

    @RequestLine("POST /price")
    @Headers("Content-Type: application/json")
    Price priceFor(List<String> books);
}
