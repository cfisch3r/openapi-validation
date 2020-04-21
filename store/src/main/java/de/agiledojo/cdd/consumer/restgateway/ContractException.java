package de.agiledojo.cdd.consumer.restgateway;

import feign.FeignException;

public class ContractException extends RuntimeException {
    public ContractException(String messages) {
        super(messages);
    }

    public ContractException(RuntimeException e) {
        super(e);
    }
}
