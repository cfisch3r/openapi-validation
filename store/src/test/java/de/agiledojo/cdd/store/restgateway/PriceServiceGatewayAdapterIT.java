package de.agiledojo.cdd.store.restgateway;

import com.atlassian.oai.validator.wiremock.OpenApiValidationListener;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import de.agiledojo.cdd.store.core.POTTER_BOOKS;
import de.agiledojo.cdd.store.core.Price;
import de.agiledojo.cdd.store.core.PriceServiceGateway;
import org.junit.jupiter.api.*;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Price Service Gateway Adapter")
public class PriceServiceGatewayAdapterIT {
    private enum RESPONSE_BODIES {
        EMPTY("{}"),
        MINIMAL("{\"inCent\":800}"),
        WITH_ADDITIONAL_FIELDS("{\"inCent\":800,\"xxx\":5}");

        private String value;

        RESPONSE_BODIES(String value) {
            this.value = value;
        }
    }

    private static final String CALCULATOR_CONTRACT = "de/agiledojo/cdd/price-api/store.yml";
    private static final long CONNECT_TIMEOUT = 50L;
    private static final long READ_TIMEOUT = 50L;
    public static final String ENDPOINT_PATH = "/price";
    private PriceServiceGateway gateway;
    private WireMockServer server;

    @BeforeEach
    void setUp() {
        server = new WireMockServer(WireMockConfiguration.DYNAMIC_PORT);
        server.start();
        gateway = createGateway(server.baseUrl(), CONNECT_TIMEOUT, READ_TIMEOUT);
    }

    @AfterEach
    void tearDown() {
        server.stop();
    }

    @Test
    void returns_price_for_purchased_books_from_price_service() {
        setUpProducerEndpointWithSuccessfulResponse(RESPONSE_BODIES.MINIMAL);
        var price = gateway.priceFor(singletonList(POTTER_BOOKS.I));
        assertThat(price.inCent).isEqualTo(800);
    }

    @Test
    void sends_requested_books_to_price_service() {
        setUpProducerEndpointWithSuccessfulResponse(RESPONSE_BODIES.MINIMAL);
        gateway.priceFor(singletonList(POTTER_BOOKS.I));
        server.verify(postRequestedFor(urlPathEqualTo(ENDPOINT_PATH))
                .withRequestBody(equalToJson("[\"I\"]")));
    }

    @Test
    void communicates_with_price_service_according_to_contract() {
        var apiValidationListener = new OpenApiValidationListener(CALCULATOR_CONTRACT);
        server.addMockServiceRequestListener(apiValidationListener);
        setUpProducerEndpointWithSuccessfulResponse(RESPONSE_BODIES.MINIMAL);
        gateway.priceFor(singletonList(POTTER_BOOKS.I));
        apiValidationListener.assertValidationPassed();
    }
    @Test
    void tolerates_additional_fields_in_response_from_price_service() {
        setUpProducerEndpointWithSuccessfulResponse(RESPONSE_BODIES.WITH_ADDITIONAL_FIELDS);
        var price = gateway.priceFor(singletonList(POTTER_BOOKS.I));
        assertThat(price.inCent).isEqualTo(800);
    }

    @Nested
    class throws_a_communication_exception {

        @Test
        void for_remote_server_error_code() {
            setupProducerWithResponseStatusCode(500);
            assertThatCommunicationExceptionWasThrownWhenRequestingPriceForBooks();
        }

        @Test
        void when_connection_to_server_fails() {
            server.stop();
            assertThatCommunicationExceptionWasThrownWhenRequestingPriceForBooks();
        }

        @Test
        void when_endpoint_does_not_respond_within_timeout() {
            server.stubFor(post(urlEqualTo(ENDPOINT_PATH)).willReturn(
                    aResponse()
                            .withStatus(200)
                            .withBody(RESPONSE_BODIES.MINIMAL.value)
                            .withFixedDelay((int) (READ_TIMEOUT + 10))));
            assertThatCommunicationExceptionWasThrownWhenRequestingPriceForBooks();
        }

        private void assertThatCommunicationExceptionWasThrownWhenRequestingPriceForBooks() {
            assertThrows(CommunicationException.class, () ->
                    gateway.priceFor(singletonList(POTTER_BOOKS.I)));
        }


    }

    @Nested
    class throws_a_contract_exception {


        @Test
        void for_missing_or_invalid_attributes_in_response_body() {
            setUpProducerEndpointWithSuccessfulResponse(RESPONSE_BODIES.EMPTY);
            assertThatContractExceptionWasThrownWhenRequestingPriceForBooks();
        }

        @Test
        void when_sending_invalid_book_ids() {
            setupProducerWithResponseStatusCode(400);
            assertThatContractExceptionWasThrownWhenRequestingPriceForBooks();
        }

        private ContractException assertThatContractExceptionWasThrownWhenRequestingPriceForBooks() {
            return assertThrows(ContractException.class, () ->
                    gateway.priceFor(singletonList(POTTER_BOOKS.I)));
        }
    }

    private void setupProducerWithResponseStatusCode(int statusCode) {
        server.stubFor(post(urlEqualTo(ENDPOINT_PATH))
                .willReturn(status(statusCode)));
    }

    private void setUpProducerEndpointWithSuccessfulResponse(RESPONSE_BODIES body) {
        server.stubFor(post(urlEqualTo(ENDPOINT_PATH))
                .willReturn(okJson(body.value)));
    }

    private PriceServiceGateway createGateway(String baseUrl, long connectTimeout, long readTimeout) {
        return new PriceServiceGatewayAdapter(baseUrl, connectTimeout, readTimeout);
    }
}
