package com.netflix.pricing.pricingservice.updates;

import java.math.BigDecimal;
import com.netflix.pricing.pricingservice.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PriceChangeEvent {
  public static final String DELIIMETER = "::";

  private String countryISOCode;

  private String servicePlan;

  private BigDecimal price;

  String currency;

  /**
   * 
   * @param serializedEvent US::SP1S::8.99::USD
   * @return
   */
  public static PriceChangeEvent createPriceChangeEventFromString(String serializedEvent) {
    String[] result = serializedEvent.split(DELIIMETER);
    if (result == null || result.length < 4) {
      throw new ServiceException("Invalid Event:" + serializedEvent);
    }

    return PriceChangeEvent.builder().countryISOCode(result[0]).servicePlan(result[1])
        .price(new BigDecimal(result[2])).currency(result[3]).build();

  }

  /**
   * 
   * @return serialized value of the event e.g.US::SP1S::8.99::USD
   */
  public String serializeEvent() {

    return new StringBuilder().append(countryISOCode).append(DELIIMETER).append(servicePlan)
        .append(DELIIMETER).append(price).append(DELIIMETER).append(currency).toString();

  }
}
