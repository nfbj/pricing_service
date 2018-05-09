package com.netflix.pricing.pricingservice.customer.priceupdate.processor;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import com.netflix.pricing.pricingservice.customer.Customer;
import com.netflix.pricing.pricingservice.customer.CustomerRepository;
import com.netflix.pricing.pricingservice.customer.RedisCustomerStoreAndQueueConfig;

public class CustomerPriceChangeProcessorThread implements Runnable {

  private String customerPriceChangeInProcessQueue;
  private RedisConnection redisConn;
  private CustomerRepository customerRepo;

  CustomerPriceChangeProcessorThread(RedisConnectionFactory connectionFactory,
      CustomerRepository customerRepo) {
    this.redisConn = connectionFactory.getConnection();
    customerPriceChangeInProcessQueue =
        RedisCustomerStoreAndQueueConfig.IMPACTED_CUSTOMER_INPROGRESS_QUEUE_PREFIX
            + UUID.randomUUID().toString();
    this.customerRepo = customerRepo;
  }

  @Override
  public void run() {

    while (true) {
      byte[] element = redisConn.bRPopLPush(10,
          RedisCustomerStoreAndQueueConfig.IMPACTED_CUSTOMER_QUEUE.getBytes(),
          customerPriceChangeInProcessQueue.getBytes());
      if (element != null) {
        String str = new String(element);
        String[] result = str.split(":");
        updateCustomerPrice(result[0], new BigDecimal(result[1]), result[2]); // todo validations
        redisConn.lRem(customerPriceChangeInProcessQueue.getBytes(), 1, str.getBytes());
      }
    }
  }

  void updateCustomerPrice(String customerID, BigDecimal price, String currency) {

    Optional<Customer> container = customerRepo.findById(customerID);
    if (container.isPresent()) {
      Customer customer = container.get();
      customer.setPrice(price);
      customer.setCurrency(currency);

      synchronized (customerRepo) {
        customerRepo.save(customer);
      }
    }
  }

}
