package com.netflix.pricing.pricingservice.exception;

/**
 * This exception represents input validation exception. When exception of this type is thrown
 * service will return 400 Bad Request
 *
 */
public class ServiceInputValidationException extends ServiceException {

  private static final long serialVersionUID = -7555569108874760695L;

  public ServiceInputValidationException(String message) {
    super(message);
  }

}
