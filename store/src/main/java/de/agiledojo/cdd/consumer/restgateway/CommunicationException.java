package de.agiledojo.cdd.consumer.restgateway;

public class CommunicationException extends RuntimeException {
    public CommunicationException(String message) {
        super(message);
    }

    public CommunicationException(Throwable cause) {
        super(cause);
    }
}
