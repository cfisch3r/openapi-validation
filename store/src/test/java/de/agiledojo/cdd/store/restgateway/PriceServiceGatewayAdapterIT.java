package de.agiledojo.cdd.store.restgateway;

import com.atlassian.oai.validator.wiremock.OpenApiValidationListener;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import de.agiledojo.cdd.store.core.POTTER_BOOKS;
import de.agiledojo.cdd.store.core.PriceServiceGateway;
import org.junit.jupiter.api.*;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Price Service Gateway Adapter")
public class PriceServiceGatewayAdapterIT {
    private static final String CALCULATOR_CONTRACT = "de/agiledojo/cdd/price-api/store.yml";
    private static final String ENDPOINT_PATH = "/price";
    private static final String RESPONSE_BODY = "{\"inCent\":800}";

    private PriceServiceGateway gateway;
    private WireMockServer server;

    @BeforeEach
    void setUp() {
        server = new WireMockServer(WireMockConfiguration.DYNAMIC_PORT);
        server.stubFor(post(urlEqualTo(ENDPOINT_PATH))
                .willReturn(okJson(RESPONSE_BODY)));
        server.start();
        gateway = new PriceServiceGatewayAdapter(server.baseUrl());
    }

    @AfterEach
    void tearDown() {
        server.stop();
    }

    @Test
    void sends_requested_books_to_price_service() {
        gateway.priceFor(singletonList(POTTER_BOOKS.I));
        server.verify(postRequestedFor(urlPathEqualTo(ENDPOINT_PATH))
                .withRequestBody(equalToJson("[\"I\"]")));
    }

    @Test
    void returns_price_for_purchased_books_from_price_service() {
        var price = gateway.priceFor(singletonList(POTTER_BOOKS.I));
        assertThat(price.inCent).isEqualTo(800);
    }

    @Test
    void communicates_with_price_service_according_to_contract() {
        var apiValidationListener = new OpenApiValidationListener(CALCULATOR_CONTRACT);
        server.addMockServiceRequestListener(apiValidationListener);
        gateway.priceFor(singletonList(POTTER_BOOKS.I));
        apiValidationListener.assertValidationPassed();
    }

}
