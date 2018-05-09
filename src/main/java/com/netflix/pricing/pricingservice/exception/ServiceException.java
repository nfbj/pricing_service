package com.netflix.pricing.pricingservice.exception;

/**
 * This exception represents service exception. When exception of this type is thrown service will
 * return 500 Internal Server Error
 */
public class ServiceException extends RuntimeException {

  private static final long serialVersionUID = -2688796099308646789L;

  public ServiceException(String message, Throwable cause) {
    super(message, cause);
  }

  public ServiceException(Throwable cause) {
    super(cause);
  }

  public ServiceException(String message) {
    super(message);
  }

}
