package de.agiledojo.cdd.marketing.price_api.api;

import de.agiledojo.cdd.marketing.price_api.invoker.ApiClient;
import de.agiledojo.cdd.marketing.price_api.model.Price;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for DefaultApi
 */
public class DefaultApiTest {

    private DefaultApi api;

    @Before
    public void setup() {
        api = new ApiClient().buildClient(DefaultApi.class);
    }

    
    /**
     * 
     *
     * 
     */
    @Test
    public void pricePostTest() {
        List<String> requestBody = null;
        // Price response = api.pricePost(requestBody);

        // TODO: test validations
    }

    
}
