package com.netflix.pricing.pricingservice.customer.priceupdate.processor;

import java.math.BigDecimal;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;
import com.netflix.pricing.pricingservice.config.RedisPriceChangeQueueConfig;
import com.netflix.pricing.pricingservice.customer.Customer;
import com.netflix.pricing.pricingservice.customer.CustomerRepository;
import com.netflix.pricing.pricingservice.customer.RedisCustomerStoreAndQueueConfig;
import com.netflix.pricing.pricingservice.updates.PriceChangeEvent;

@Component
public class ProcessPriceChangeThread implements Runnable {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  private static final int CUST_RESULT_PAGE_SIZE = 50;
  private static final int REDIS_BLOCKING_POP_TIME_IN_SECONDS = 50;

  private String priceChangeInProcessTopic;
  private RedisConnection redisConn;
  private CustomerRepository customerRepo;

  @Autowired
  ProcessPriceChangeThread(RedisConnectionFactory connectionFactory,
      CustomerRepository customerRepo) {
    this.redisConn = connectionFactory.getConnection();
    priceChangeInProcessTopic =
        RedisPriceChangeQueueConfig.PRICE_CHANGE_QUEUE_INPROGRESS + UUID.randomUUID().toString();
    this.customerRepo = customerRepo;
  }

  @Override
  public void run() {

    while (true) {
      byte[] event = redisConn.bRPopLPush(REDIS_BLOCKING_POP_TIME_IN_SECONDS,
          RedisPriceChangeQueueConfig.PRICE_CHANGE_QUEUE.getBytes(),
          priceChangeInProcessTopic.getBytes());
      if (event != null) {
        String eventStr = new String(event);
        PriceChangeEvent evt = PriceChangeEvent.createPriceChangeEventFromString(eventStr);
        pushCustomersToQueueForProcessing(evt.getCountryISOCode(), evt.getServicePlan(),
            evt.getPrice(), evt.getCurrency());
        redisConn.lRem(priceChangeInProcessTopic.getBytes(), 1, event);
      }
    }
  }

  void pushCustomersToQueueForProcessing(String country, String servicePlan, BigDecimal price,
      String currency) {

    int pageCnt = 0;

    Pageable page = PageRequest.of(0, CUST_RESULT_PAGE_SIZE);
    Page<Customer> paginatedCustomers = customerRepo.findAll(page);
    // TODO: findByCountryAndServicePlan(country,servicePlan, page); - REDIS JPA Filtering issue?
    while (pageCnt <= paginatedCustomers.getTotalPages()) {
      paginatedCustomers.filter(
          cust -> cust.getCountry().equals(country) && cust.getServicePlan().equals(servicePlan)) // filter
                                                                                                  // done
                                                                                                  // here
          .forEach(cust -> addCustomerForProcessing(cust, price, currency));
      page = PageRequest.of(++pageCnt, CUST_RESULT_PAGE_SIZE);
      paginatedCustomers = customerRepo.findAll(page);// findByCountryAndServicePlan(country,
                                                      // servicePlan, page);
    }

  }

  private void addCustomerForProcessing(Customer cust, BigDecimal price, String currency) {
    if (cust == null) {
      logger.error("Cust ID is null - check why..."); // TODO: Remove this after unit tests
      return;
    }
    redisConn.lPush(RedisCustomerStoreAndQueueConfig.IMPACTED_CUSTOMER_QUEUE.getBytes(),
        (cust.getId() + ":" + price + ":" + currency).getBytes()); // Todo: Refactor & custID can't
                                                                   // have the delimiter- use native
                                                                   // serializer
  }


}
