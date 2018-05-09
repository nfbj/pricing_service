package com.netflix.pricing.pricingservice.exception;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.UUID;
import org.springframework.http.HttpStatus;

/**
 * This class represents error information structure to return from API calls.
 */
public class ErrorEntity extends LinkedHashMap<String, Object> {

  private static final long serialVersionUID = 1L;

  private final String ATTR_TIMESTAMP = "timestamp";
  private final String ATTR_UUID = "uuid";
  private final String ATTR_STATUS = "status";
  private final String ATTR_ERROR = "error";
  private final String ATTR_MESSAGE = "message";
  private final String ATTR_PATH = "path";
  private final String ATTR_EXCEPTION = "exception";
  private final String ATTR_TRACE = "trace";

  /**
   * Allow wrapping it into the object to be serialized and sent
   */
  public static class DTO extends HashMap<String, ErrorEntity> {

    private static final long serialVersionUID = -8813922241492899173L;
    public static final String ERROR_PROPERTY = "_error";

    public DTO(ErrorEntity errorEntity) {
      super();
      put(ERROR_PROPERTY, errorEntity);
    }

  }

  public ErrorEntity() {
    setTimestamp(new Date());
    setUuid(UUID.randomUUID());
  }

  /**
   * CTOR.
   * 
   * @param status response status. The status is used to populate status and error properties
   * @param message message
   */
  public ErrorEntity(HttpStatus status, String message) {
    this();

    setStatus(status.value());
    setError(status.getReasonPhrase());
    setMessage(message);
  }

  /**
   * CTOR.
   * 
   * @param status response status. The status is used to populate status and error properties.
   * @param message message
   * @param path request path
   */
  public ErrorEntity(HttpStatus status, String message, String path) {
    this(status, message);

    setPath(path);
  }

  public DTO getDTO() {
    return new DTO(this);

  }

  public void setTimestamp(Date timestamp) {
    this.put(ATTR_TIMESTAMP, timestamp);
  }

  public Date getTimestamp() {
    return (Date) this.get(ATTR_TIMESTAMP);
  }

  public void setUuid(UUID uuid) {
    this.put(ATTR_UUID, uuid);
  }

  public UUID getUuid() {
    return (UUID) this.get(ATTR_UUID);
  }

  public void setStatus(int status) {
    this.put(ATTR_STATUS, status);
  }

  public int getStatus() {
    return this.containsKey(ATTR_STATUS) ? (int) this.get(ATTR_STATUS) : 0;
  }

  public void setError(String error) {
    this.put(ATTR_ERROR, error);
  }

  public String getError() {
    return (String) this.get(ATTR_ERROR);
  }

  public void setMessage(String message) {
    this.put(ATTR_MESSAGE, message);
  }

  public String getMessage() {
    return (String) this.get(ATTR_MESSAGE);
  }

  public void setPath(String path) {
    this.put(ATTR_PATH, path);
  }

  public String getPath() {
    return (String) this.get(ATTR_PATH);
  }

  public void setException(String exception) {
    this.put(ATTR_EXCEPTION, exception);
  }

  public String getException() {
    return (String) this.get(ATTR_EXCEPTION);
  }

  public void setTrace(String trace) {
    this.put(ATTR_TRACE, trace);
  }

  public String getTrace() {
    return (String) this.get(ATTR_TRACE);
  }

  /**
   * {@inheritDoc}
   * 
   * @return string representation of error that can be written to log
   */
  @Override
  public String toString() {
    return String.format("uuid=[%s];status=[%s];path=[%s];message=[%s]", getUuid(), getStatus(),
        getPath(), getMessage());
  }
}
