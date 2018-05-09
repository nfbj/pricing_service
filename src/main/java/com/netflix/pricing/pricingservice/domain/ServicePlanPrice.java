package com.netflix.pricing.pricingservice.domain;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "SRV_PLAN_PRICE")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServicePlanPrice {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "COUNTRY")
  @NotEmpty
  String countryISOCode;

  @Column(name = "PLAN")
  @NotNull
  ServicePlan servicePlan;

  @Column(name = "PRICE")
  @NotNull
  BigDecimal price;

  @Column(name = "CURRENCY")
  @NotEmpty
  String currency;

  @Column(name = "EFFECTIVE_DATE")
  ZonedDateTime effectiveDate;

  @Column(name = "IS_NOTIFIED")
  boolean isChangeNotified;

  @Column(name = "IS_APPLIED")
  boolean isChangeApplied;

  @Column(name = "IS_ACTIVE")
  boolean isActive;

  // TODO: createAt createdBy markForDelete

}
