package de.agiledojo.cdd.store.restgateway;

public class ContractException extends RuntimeException {
    public ContractException(String messages) {
        super(messages);
    }

    public ContractException(RuntimeException e) {
        super(e);
    }
}
