package com.netflix.pricing.pricingservice.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.util.StringUtils;
import com.netflix.pricing.pricingservice.common.ResourceMessages;
import com.netflix.pricing.pricingservice.domain.CountryCurrencyUtil;
import com.netflix.pricing.pricingservice.domain.ServicePlanPrice;
import com.netflix.pricing.pricingservice.exception.ServiceInputValidationException;

/**
 * Util class to convert price plan dto to price plan & vice versa + its validations
 */
public class PriceModelMapperUtil {
  public static List<ServicePlanPrice> createPricefromPriceDTOList(
      List<ServicePlanPriceDTO> planDtos) {
    // TODO:STREAMS
    List<ServicePlanPrice> prices = new ArrayList<>();
    for (ServicePlanPriceDTO planDto : planDtos) {
      validateRequest(planDto);
      prices.add(createPricefromPriceDTO(planDto));
    }
    return prices;
  }

  public static List<ServicePlanPriceDTO> createPriceDTOfromPriceList(
      Iterable<ServicePlanPrice> plans) {
    List<ServicePlanPriceDTO> prices = new ArrayList<>();
    for (ServicePlanPrice price : plans) {
      prices.add(createPriceDTOfromPrice(price));
    }
    return prices;
  }

  public static ServicePlanPriceDTO createPriceDTOfromPrice(ServicePlanPrice price) {
    ServicePlanPriceDTO priceDto =
        ServicePlanPriceDTO.builder().countryISOCode(price.getCountryISOCode())
            .price(price.getPrice()).currency(price.getCurrency())
            .servicePlan(price.getServicePlan()).effectiveDate(price.getEffectiveDate())
            .isActive(price.isActive()).id(price.getId()).build();
    return priceDto;
  }

  public static ServicePlanPrice createPricefromPriceDTO(ServicePlanPriceDTO priceDto) {
    ServicePlanPrice price = ServicePlanPrice.builder().countryISOCode(priceDto.getCountryISOCode())
        .price(priceDto.getPrice()).currency(priceDto.getCurrency())
        .servicePlan(priceDto.getServicePlan()).effectiveDate(priceDto.getEffectiveDate()).build();
    return price;
  }

  /*
   * Input Validations for DTO Basic validations done, check for additional business validations
   * e.g. effective date to be in the future
   */
  public static void validateRequest(ServicePlanPriceDTO servicePrice) {

    validateCountry(servicePrice.getCountryISOCode());
    if (!CountryCurrencyUtil.isValidCurrency(servicePrice.getCurrency())) {
      throw new ServiceInputValidationException(
          ResourceMessages.getString("PricePlanCR.invalidCurrency") + servicePrice.getCurrency()); //$NON-NLS-1$
    }
    if (!CountryCurrencyUtil.isValidCurrencyForCountry(servicePrice.getCurrency(),
        servicePrice.getCountryISOCode())) {
      throw new ServiceInputValidationException(
          ResourceMessages.getString("PricePlanCR.inValidCurrencyForCountry") //$NON-NLS-1$
              + servicePrice.getCurrency() + ":" //$NON-NLS-1$
              + servicePrice.getCountryISOCode());
    }

    if (servicePrice.effectiveDate == null) {
      throw new ServiceInputValidationException(
          ResourceMessages.getString("PricePlanCR.effectiveDateNull")); //$NON-NLS-1$
    }

    /**
     * Disabled - Validation's effective date *must* be in the future
     */
    // if (servicePrice.effectiveDate.isBefore(ZonedDateTime.now()))
    // throw new
    // ServiceInputValidationException(ResourceMessages.getString("PricePlanCR.effectiveDatePast"));
    // //$NON-NLS-1$

    if (servicePrice.price == null) {
      throw new ServiceInputValidationException(
          ResourceMessages.getString("PricePlanCR.PriceNull")); //$NON-NLS-1$
    }
    if (servicePrice.servicePlan == null) {
      throw new ServiceInputValidationException(
          ResourceMessages.getString("PricePlanCR.servicePlanNull")); //$NON-NLS-1$
    }
  }

  public static void validateCountry(String countryCode) {
    if (!StringUtils.isEmpty(countryCode)
        && !CountryCurrencyUtil.ISO_COUNTRIES.contains(countryCode.toUpperCase())) {
      throw new ServiceInputValidationException(
          ResourceMessages.getString("PricePlanCR.invalidCountryCode") + countryCode //$NON-NLS-1$
              + ResourceMessages.getString("PricePlanCR.invalidCountryCode2")); //$NON-NLS-1$
    }
  }
}
