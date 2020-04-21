package de.agiledojo.cdd.marketing.catalog.core;

public class CalculatorGatewayException extends RuntimeException {
    public CalculatorGatewayException(String message) {
        super(message);
    }

    public CalculatorGatewayException(Throwable cause) {
        super(cause);
    }
}