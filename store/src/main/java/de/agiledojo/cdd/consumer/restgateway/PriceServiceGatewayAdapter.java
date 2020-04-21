package de.agiledojo.cdd.consumer.restgateway;

import de.agiledojo.cdd.consumer.core.PriceServiceGateway;
import de.agiledojo.cdd.consumer.core.POTTER_BOOKS;
import de.agiledojo.cdd.consumer.core.Price;
import feign.Feign;
import feign.FeignException;
import feign.Request;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class PriceServiceGatewayAdapter implements PriceServiceGateway {

    private final PriceApi restApi;
    private final Validator validator;

    public PriceServiceGatewayAdapter(String endpointUrl, long connectTimeout, long readTimeout) {
        restApi = setUpRestClient(endpointUrl, connectTimeout, readTimeout);
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Override
    public Price priceFor(List<POTTER_BOOKS> books) {
        Price price = getPriceFromEndpoint(books);
        assertIsValid(price);
        return price;
    }

    private Price getPriceFromEndpoint(List<POTTER_BOOKS> books) {
        try {
            return restApi.priceFor(books.stream()
                    .map(POTTER_BOOKS::toString)
                    .collect(Collectors.toList()));
        } catch (FeignException.BadRequest e) {
            throw new ContractException(e);
        } catch (RuntimeException e) {
            throw new CommunicationException(e);
        }
    }

    private void assertIsValid(Price price) {
        Set<ConstraintViolation<Price>> violations = validator.validate(price);
        if (violations.size() > 0) {
            String messages = violations.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining());
            throw new ContractException(messages);
        }
    }

    private PriceApi setUpRestClient(String endpointUrl, long connectTimeout, long readTimeout) {
        Request.Options options = new Request.Options(connectTimeout, TimeUnit.MILLISECONDS,
                readTimeout,TimeUnit.MILLISECONDS,true);
        return Feign.builder()
                .options(options)
                .decoder(new GsonDecoder())
                .encoder(new GsonEncoder())
                .target(PriceApi.class, endpointUrl);
    }
}
