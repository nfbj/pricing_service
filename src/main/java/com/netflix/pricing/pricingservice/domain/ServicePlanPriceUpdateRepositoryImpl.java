package com.netflix.pricing.pricingservice.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class ServicePlanPriceUpdateRepositoryImpl implements ServicePlanPriceUpdateRepository {

  @Autowired
  ApplicationContext appContext;

  @Override
  public void setCurrentPriceActiveAndOthersInactive(long id, String countryCode,
      ServicePlan servicePlan) {
    ServicePlanPriceRepository repo = appContext.getBean(ServicePlanPriceRepository.class);
    repo.setInActive(countryCode, servicePlan);
    repo.setActive(id);
  }

}
