package de.agiledojo.cdd.marketing.price_api.api;

import de.agiledojo.cdd.marketing.price_api.invoker.ApiClient;
import de.agiledojo.cdd.marketing.price_api.invoker.EncodingUtils;

import de.agiledojo.cdd.marketing.price_api.model.Price;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import feign.*;


public interface DefaultApi extends ApiClient.Api {


  /**
   * 
   * 
   * @param requestBody  (required)
   * @return Price
   */
  @RequestLine("POST /price")
  @Headers({
    "Content-Type: application/json",
    "Accept: application/json",
  })
  Price pricePost(List<String> requestBody);
}
