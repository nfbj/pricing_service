package com.netflix.pricing.pricingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import com.netflix.pricing.pricingservice.customer.priceupdate.processor.CustomerPriceUpdateWorkerThreadPool;
import com.netflix.pricing.pricingservice.customer.priceupdate.processor.ProcessPriceChangeThread;

@SpringBootApplication
public class PricingServiceApplication {

  public static void main(String[] args) {
    ApplicationContext ctx = SpringApplication.run(PricingServiceApplication.class, args);

    ProcessPriceChangeThread priceChangeProcessor = ctx.getBean(ProcessPriceChangeThread.class);
    new Thread(priceChangeProcessor).start();

    CustomerPriceUpdateWorkerThreadPool workerThread =
        ctx.getBean(CustomerPriceUpdateWorkerThreadPool.class);
    workerThread.start();

  }



}
