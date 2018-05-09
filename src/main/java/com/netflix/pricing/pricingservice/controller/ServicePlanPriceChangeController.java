package com.netflix.pricing.pricingservice.controller;

import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.netflix.pricing.pricingservice.common.ResourceMessages;
import com.netflix.pricing.pricingservice.domain.ServicePlan;
import com.netflix.pricing.pricingservice.domain.ServicePlanPrice;
import com.netflix.pricing.pricingservice.domain.ServicePlanPriceRepository;
import com.netflix.pricing.pricingservice.exception.ServiceInputValidationException;
import io.swagger.annotations.Api;

/**
 * @author i815376
 *
 */
@RestController
@RequestMapping("/prices")
@Api(value = "Pricing Service API", description = "Main REST API for accessing & rollout out Price",
    tags = {"Netflix Pricing Customers API"})
public class ServicePlanPriceChangeController {

  private ServicePlanPriceRepository repo;

  @Autowired
  public void setServicePlanRepo(ServicePlanPriceRepository repo) {
    this.repo = repo;
  }

  @GetMapping("")
  @ResponseBody
  List<ServicePlanPriceDTO> getAllServicePlanPricesForAllCountries(
      @RequestParam(value = "active", required = false, defaultValue = "false") boolean active) {

    Iterable<ServicePlanPrice> servicePlanPrices = null;
    if (active) {
      servicePlanPrices = repo.findByIsActiveTrue();
    } else {
      servicePlanPrices = repo.findAll();
    }

    return PriceModelMapperUtil.createPriceDTOfromPriceList(servicePlanPrices);
  }

  @GetMapping("/{COUNTRY}")
  @ResponseBody
  List<ServicePlanPriceDTO> getServicePlanPriceByCountry(
      @PathVariable("COUNTRY") String countryCode,
      @RequestParam(value = "active", required = false, defaultValue = "true") boolean active) {

    Iterable<ServicePlanPrice> servicePlanPrices = null;
    if (active) {
      servicePlanPrices = repo.findByCountryISOCodeIgnoreCaseAndIsActiveTrue(countryCode);
    } else {
      servicePlanPrices = repo.findByCountryISOCodeIgnoreCase(countryCode);
    }

    return PriceModelMapperUtil.createPriceDTOfromPriceList(servicePlanPrices);
  }

  @GetMapping("/{COUNTRY}/{PLAN}")
  @ResponseBody
  List<ServicePlanPriceDTO> getServicePlanPriceByCountryAndServicePlan(
      @PathVariable("COUNTRY") String countryCode, @PathVariable("PLAN") ServicePlan plan,
      @RequestParam(value = "active", required = false, defaultValue = "true") boolean active) {

    Iterable<ServicePlanPrice> servicePlanPrices = null;
    if (active) {
      servicePlanPrices =
          repo.findByCountryISOCodeAndServicePlanAndIsActiveTrueAllIgnoreCase(countryCode, plan);
    } else {
      servicePlanPrices = repo.findByCountryISOCodeAndServicePlanAllIgnoreCase(countryCode, plan);
    }

    return PriceModelMapperUtil.createPriceDTOfromPriceList(servicePlanPrices);
  }

  @PostMapping("")
  @ResponseBody
  Iterable<ServicePlanPriceDTO> updatePrices(
      @Valid @RequestBody List<ServicePlanPriceDTO> servicePlanPriceDTO) {

    List<ServicePlanPrice> servicePlanPrices =
        PriceModelMapperUtil.createPricefromPriceDTOList(servicePlanPriceDTO);

    List<ServicePlanPrice> servicePlanPriceDTOResult =
        (List<ServicePlanPrice>) repo.saveAll(servicePlanPrices);

    return PriceModelMapperUtil.createPriceDTOfromPriceList(servicePlanPriceDTOResult);
  }

  /**
   * Can delete only price plans that are not active (Past or Furture prices) Safety net to correct
   * wrong entries before price becomes active.
   * 
   * @param id ID of the price change created earlier.
   * @return
   */
  @DeleteMapping
  @ResponseBody
  String deletePriceChangeEntryIfNotActive(Long id) {

    Optional<ServicePlanPrice> plan = repo.findById(id);
    if (plan.isPresent() && !plan.get().isActive()) {
      repo.deleteById(id);
      return "Deleted Price :" + id; //
    }
    throw new ServiceInputValidationException(ResourceMessages.getString("PricePlanCR.InValidID")); //$NON-NLS-1$
  }

}
