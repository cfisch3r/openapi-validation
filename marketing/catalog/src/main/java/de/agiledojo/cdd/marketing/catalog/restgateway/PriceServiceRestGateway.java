package de.agiledojo.cdd.marketing.catalog.restgateway;

import de.agiledojo.cdd.marketing.catalog.core.CalculatorGatewayException;
import de.agiledojo.cdd.marketing.catalog.core.POTTER_BOOKS;
import de.agiledojo.cdd.marketing.catalog.core.Price;
import de.agiledojo.cdd.marketing.catalog.core.PriceServiceGateway;
import de.agiledojo.cdd.marketing.price_api.api.DefaultApi;
import de.agiledojo.cdd.marketing.price_api.invoker.ApiClient;
import feign.FeignException;
import feign.Request;

import java.util.List;
import java.util.stream.Collectors;

public class PriceServiceRestGateway implements PriceServiceGateway {

    private final DefaultApi api;

    public PriceServiceRestGateway(String baseUrl, long connectTimeout, long readTimeout) {
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(baseUrl);
        Request.Options options = new Request.Options((int) connectTimeout,(int) readTimeout,true);
        apiClient.setFeignBuilder(apiClient.getFeignBuilder().options(options));
        api = apiClient.buildClient(DefaultApi.class);
    }

    @Override
    public Price priceFor(List<POTTER_BOOKS> books) {
        de.agiledojo.cdd.marketing.price_api.model.Price price = performRequest(books);
        if (price.getInCent() == null) {
            throw new CalculatorGatewayException("missing attribute");
        }
        return new Price(price.getInCent().longValue(),price.getTax().longValue());
    }

    private de.agiledojo.cdd.marketing.price_api.model.Price performRequest(List<POTTER_BOOKS> books) {
        try {
            return api.priceForPost(books.stream()
                        .map(POTTER_BOOKS::toString)
                        .collect(Collectors.toList()));
        } catch (FeignException e) {
            throw new CalculatorGatewayException(e);
        }
    }
}
