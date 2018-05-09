package com.netflix.pricing.pricingservice.controller;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import com.netflix.pricing.pricingservice.domain.ServicePlan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServicePlanPriceDTO {


  String countryISOCode;

  ServicePlan servicePlan;

  BigDecimal price;

  String currency;

  ZonedDateTime effectiveDate;

  // TODO: Swagger Annotate to MARK as Read only or create example?
  boolean isActive;
  long id;

}
