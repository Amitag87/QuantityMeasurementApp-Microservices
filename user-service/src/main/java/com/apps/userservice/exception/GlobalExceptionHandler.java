package com.apps.userservice.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(QuantityMeasurementException.class)
	public ResponseEntity<Map<String, Object>> handleQuantityException(QuantityMeasurementException e,
			HttpServletRequest request) {
		return buildResponse(HttpStatus.BAD_REQUEST, e.getMessage(), request.getRequestURI());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, Object>> handleGlobalException(Exception e, HttpServletRequest request) {
		return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), request.getRequestURI());
	}

	private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message, String path) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("status", status.value());
		body.put("error", status.getReasonPhrase());
		body.put("message", message);
		body.put("path", path);
		return new ResponseEntity<>(body, status);
	}
}
