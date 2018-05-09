package com.netflix.pricing.pricingservice.customer.priceupdate.processor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;
import com.netflix.pricing.pricingservice.customer.CustomerRepository;

@Component
public class CustomerPriceUpdateWorkerThreadPool {

  private static final int THREAD_POOL_SIZE = 5;

  private RedisConnectionFactory connectionFactory;
  private CustomerRepository customerRepo;

  @Autowired
  CustomerPriceUpdateWorkerThreadPool(RedisConnectionFactory connectionFactory,
      CustomerRepository customerRepo) {
    this.connectionFactory = connectionFactory;
    this.customerRepo = customerRepo;
  }

  public void start() {
    //
    // ThreadPoolTaskExecutor executor = ctx.getExec("receivemsgdb");
    // if (executor != null) {
    // executor.execute(new ReceiveMsgToCallHistory(queueName));
    // }

    ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    for (int i = 0; i < THREAD_POOL_SIZE; i++) {
      Runnable worker = new CustomerPriceChangeProcessorThread(connectionFactory, customerRepo);
      executor.execute(worker);
    }
    executor.shutdown();
  }

}
