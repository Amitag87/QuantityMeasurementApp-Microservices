package com.apps.conversionservice.exception;

public class QuantityMeasurementException extends RuntimeException {

	public QuantityMeasurementException(String message) {
		super(message);
	}

	public QuantityMeasurementException(String message, Throwable cause) {
		super(message, cause);
	}
}
