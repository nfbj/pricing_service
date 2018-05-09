package com.netflix.pricing.pricingservice.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

/**
 * Handles all exceptions that went are not handled by other handlers within controllers.
 */
@ControllerAdvice
public class GlobalExceptionHandler {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final ServerProperties serverProperties;

  public GlobalExceptionHandler(ServerProperties serverProperties) {
    this.serverProperties = serverProperties;
  }

  /**
   * Handles {@link ServiceInputValidationException}s.Status code 400 with original exception
   * message is returned
   */
  @ExceptionHandler({ServiceInputValidationException.class})
  public ResponseEntity<ErrorEntity.DTO> handleServiceValidationException(Exception ex,
      WebRequest request) {
    ErrorEntity errorEntity =
        createErrorEntity(HttpStatus.BAD_REQUEST, ex.getMessage(), ex, request);

    logErrorEntity(errorEntity, ex, false);

    return new ResponseEntity<>(errorEntity.getDTO(), HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles {@link ServiceException}s. Status code 500 with original exception message is returned
   * in response to request.
   * 
   */
  @ExceptionHandler({ServiceException.class})
  public ResponseEntity<ErrorEntity.DTO> handleServiceException(Exception ex, WebRequest request) {
    ErrorEntity errorEntity =
        createErrorEntity(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex, request);

    logErrorEntity(errorEntity, ex, true);

    return new ResponseEntity<>(errorEntity.getDTO(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  /**
   * Handles all unhandled {@link Exception}s. Original exception message will be replaced with
   * generic message to avoid bringing sensitive information to client.
   * 
   */
  @ExceptionHandler({Exception.class})
  public ResponseEntity<ErrorEntity.DTO> handleAll(Exception ex, WebRequest request) {
    ErrorEntity errorEntity = createErrorEntity(HttpStatus.INTERNAL_SERVER_ERROR,
        "Please check log files for response uuid to find reason.", ex, request);

    logErrorEntity(errorEntity, ex, true);

    return new ResponseEntity<>(errorEntity.getDTO(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  private ErrorEntity createErrorEntity(HttpStatus responseStatus, String message, Exception ex,
      WebRequest request) {

    String path = request instanceof ServletWebRequest
        ? ((ServletWebRequest) request).getRequest().getRequestURI()
        : "";

    // convert exception into error entity
    ErrorEntity errorEntity = new ErrorEntity(responseStatus, message, path);

    if (serverProperties.getError().isIncludeException()) {
      addException(ex, errorEntity);
    }

    if (serverProperties.getError()
        .getIncludeStacktrace() == ErrorProperties.IncludeStacktrace.ALWAYS) {
      addStackTrace(ex, errorEntity);
    }

    return errorEntity;
  }

  private void logErrorEntity(ErrorEntity errorEntity, Exception ex, boolean logError) {
    if (logError) {
      logger.error(String.format("%s %s", errorEntity, ex.getMessage()), ex);
    } else {
      logger.warn("{}", errorEntity);
    }
  }

  private void addException(Exception ex, ErrorEntity errorEntity) {
    errorEntity.setException(ex.getClass().getName());
  }

  private void addStackTrace(Throwable error, ErrorEntity errorEntity) {
    StringWriter stackTrace = new StringWriter();
    error.printStackTrace(new PrintWriter(stackTrace));
    stackTrace.flush();
    errorEntity.setTrace(stackTrace.toString());
  }
}
