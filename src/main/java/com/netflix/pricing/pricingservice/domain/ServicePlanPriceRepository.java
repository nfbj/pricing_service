package com.netflix.pricing.pricingservice.domain;

import java.time.ZonedDateTime;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ServicePlanPriceRepository
    extends CrudRepository<ServicePlanPrice, Long>, ServicePlanPriceUpdateRepository {

  List<ServicePlanPrice> findByIsActiveTrue();

  List<ServicePlanPrice> findByCountryISOCodeIgnoreCase(String countryCode);

  List<ServicePlanPrice> findByCountryISOCodeIgnoreCaseAndIsActiveTrue(String countryCode);

  List<ServicePlanPrice> findByCountryISOCodeAndServicePlanAllIgnoreCase(String countryCode,
      ServicePlan servicePlan);

  List<ServicePlanPrice> findByCountryISOCodeAndServicePlanAndIsActiveTrueAllIgnoreCase(
      String countryCode, ServicePlan servicePlan);

  List<ServicePlanPrice> findByIsChangeNotifiedFalseAndEffectiveDateBefore(ZonedDateTime dateTime);

  @Modifying(clearAutomatically = true)
  @Transactional
  @Query("update ServicePlanPrice p set p.isChangeNotified = 1 where p.id = ?1")
  int setNotifedFlagToTrue(long id);

  @Modifying(clearAutomatically = true)
  @Query("update ServicePlanPrice p set p.isActive = 0 where p.countryISOCode = ?1 and p.servicePlan = ?2 ")
  int setInActive(String countryCode, ServicePlan servicePlan);

  @Modifying(clearAutomatically = true)
  @Query("update ServicePlanPrice p set p.isActive = 1 where p.id = ?1")
  int setActive(long id);

}
