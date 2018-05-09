package com.netflix.pricing.pricingservice.updates;

import java.time.ZonedDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.netflix.pricing.pricingservice.config.RedisPriceChangeQueueConfig;
import com.netflix.pricing.pricingservice.domain.ServicePlanPrice;
import com.netflix.pricing.pricingservice.domain.ServicePlanPriceRepository;

@Component
public class EffectiveDatedPriceChangeProcessingScheudledJob {

  private static final Logger log =
      LoggerFactory.getLogger(EffectiveDatedPriceChangeProcessingScheudledJob.class);

  @Autowired
  ServicePlanPriceRepository repo;

  @Autowired
  StringRedisTemplate template;

  @Scheduled(fixedRate = 5000)
  public void processEfcectiveDatedPriceChangeIfAny() {
    // if any updated price that falls in current time? continue
    List<ServicePlanPrice> processPriceList = getPriceChangesToProcess();

    // price change :-)
    if (processPriceList != null && !processPriceList.isEmpty()) {
      // get one entry - publish event - update flag
      processPriceList.stream().forEach(priceChange -> publish(priceChange));
    }
  }


  public void publish(ServicePlanPrice priceChange) {
    log.info("chaaaanged" + priceChange.getId());
    ListOperations<String, String> redisOperations = template.opsForList();
    redisOperations.leftPush(RedisPriceChangeQueueConfig.PRICE_CHANGE_QUEUE,
        constructEvent(priceChange).serializeEvent());
    repo.setNotifedFlagToTrue(priceChange.getId());
    repo.setCurrentPriceActiveAndOthersInactive(priceChange.getId(),
        priceChange.getCountryISOCode(), priceChange.getServicePlan());
  }

  List<ServicePlanPrice> getPriceChangesToProcess() {

    return repo.findByIsChangeNotifiedFalseAndEffectiveDateBefore(ZonedDateTime.now());

  }

  private PriceChangeEvent constructEvent(ServicePlanPrice priceChange) {
    return PriceChangeEvent.builder().countryISOCode(priceChange.getCountryISOCode())
        .currency(priceChange.getCurrency()).price(priceChange.getPrice())
        .servicePlan(priceChange.getServicePlan().name()).build();
  }


}
