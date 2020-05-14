package de.agiledojo.cdd.marketing.catalog.restgateway;

import com.atlassian.oai.validator.wiremock.OpenApiValidationListener;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import de.agiledojo.cdd.marketing.catalog.core.CalculatorGatewayException;
import de.agiledojo.cdd.marketing.catalog.core.POTTER_BOOKS;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Price Service Adapter")
public class PriceServiceRestGatewayIT {


    private static final String VALID_RESPONSE_BDY = "{\"inCent\":800,\"tax\": 22}";
    public static final String RESPONSE_WITH_ADDITIONAL_FIELD = "{\"inCent\":800,\"tax\": 22,\"xxx\":5}";
    private static final String EMPTY_RESPONSE_BODY = "{}";
    private static final String CALCULATOR_CONTRACT = "de/agiledojo/cdd/price-api/marketing.yml";
    private static final long CONNECT_TIMEOUT = 50L;
    private static final long READ_TIMEOUT = 50L;
    public static final String ENDPOINT_PATH = "/price";
    private PriceServiceRestGateway gateway;
    private WireMockServer server;
    private OpenApiValidationListener apiValidationListener;

    @BeforeEach
    void setUp() {
        server = new WireMockServer(WireMockConfiguration.DYNAMIC_PORT);
        server.start();
        gateway = new PriceServiceRestGateway(server.baseUrl(),CONNECT_TIMEOUT,READ_TIMEOUT);
    }

    @AfterEach
    void tearDown() {
        server.stop();
    }

    @Test
    void should_return_price_for_purchased_books_from_Rest_API() {
        setUpProducerEndpointWithSuccessfulResponse(VALID_RESPONSE_BDY);
        var price = gateway.priceFor(singletonList(POTTER_BOOKS.I));
        assertThat(price.inCent).isEqualTo(800);
    }

    @Test
    void should_return_tax_for_purchased_books_from_Rest_API() {
        setUpProducerEndpointWithSuccessfulResponse(VALID_RESPONSE_BDY);
        var price = gateway.priceFor(singletonList(POTTER_BOOKS.I));
        assertThat(price.tax).isEqualTo(22);
    }

    @Test
    void should_tolerate_additional_fields_in_responce_from_producer() {
        setUpProducerEndpointWithSuccessfulResponse(RESPONSE_WITH_ADDITIONAL_FIELD);
        var price = gateway.priceFor(singletonList(POTTER_BOOKS.I));
        assertThat(price.inCent).isEqualTo(800);
    }

    @Test
    void should_throw_an_exception_for_missing_or_invalid_attributes_in_response_body() {
        setUpProducerEndpointWithSuccessfulResponse(EMPTY_RESPONSE_BODY);
        assertThrows(CalculatorGatewayException.class, () ->
                gateway.priceFor(singletonList(POTTER_BOOKS.I)));
    }

    @Test
    void should_communicate_with_calculator_endpoint_according_to_contract() {
        apiValidationListener = new OpenApiValidationListener(CALCULATOR_CONTRACT);
        server.addMockServiceRequestListener(apiValidationListener);
        setUpProducerEndpointWithSuccessfulResponse(VALID_RESPONSE_BDY);
        gateway.priceFor(singletonList(POTTER_BOOKS.I));
        apiValidationListener.assertValidationPassed();
    }

    @Test
    void should_throw_an_exception_when_response_status_code_is_not_successful() {
        setupProducerWithResponseStatusCode(400);
        assertThrows(CalculatorGatewayException.class, () ->
                gateway.priceFor(singletonList(POTTER_BOOKS.I)));
    }

    @Test
    void should_throw_an_exception_when_connection_to_server_fails() {
        server.stop();
        assertThrows(CalculatorGatewayException.class, () ->
                gateway.priceFor(singletonList(POTTER_BOOKS.I)));
    }

    @Test
    void should_throw_an_exception_when_endpoint_does_not_respond_within_timeout() {
        server.stubFor(post(urlEqualTo(ENDPOINT_PATH)).willReturn(
                aResponse()
                        .withStatus(200)
                        .withBody(VALID_RESPONSE_BDY)
                        .withFixedDelay((int) (READ_TIMEOUT + 10))));
        assertThrows(CalculatorGatewayException.class, () ->
                gateway.priceFor(singletonList(POTTER_BOOKS.I)));
    }

    private void setupProducerWithResponseStatusCode(int statusCode) {
        server.stubFor(post(urlEqualTo(ENDPOINT_PATH))
                .willReturn(status(statusCode)));
    }

    private void setUpProducerEndpointWithSuccessfulResponse(String body) {
        server.stubFor(post(urlEqualTo(ENDPOINT_PATH))
                .willReturn(okJson(body)));
    }


}
