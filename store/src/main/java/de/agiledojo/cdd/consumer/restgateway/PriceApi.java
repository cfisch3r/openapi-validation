package de.agiledojo.cdd.consumer.restgateway;

import de.agiledojo.cdd.consumer.core.Price;
import feign.Headers;
import feign.RequestLine;

import java.util.List;

public interface PriceApi {

    @RequestLine("POST /priceFor")
    @Headers("Content-Type: application/json")
    Price priceFor(List<String> books);
}
