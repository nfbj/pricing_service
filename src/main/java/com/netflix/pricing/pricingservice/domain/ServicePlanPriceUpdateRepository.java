package com.netflix.pricing.pricingservice.domain;

import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;

public interface ServicePlanPriceUpdateRepository {

  @Modifying(clearAutomatically = true)
  @Transactional
  void setCurrentPriceActiveAndOthersInactive(long id, String countryCode, ServicePlan plan);

}
