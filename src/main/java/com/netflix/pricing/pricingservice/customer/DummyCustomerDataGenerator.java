package com.netflix.pricing.pricingservice.customer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DummyCustomerDataGenerator {

  private CustomerRepository customerRepo;

  @Autowired
  public DummyCustomerDataGenerator(CustomerRepository customerRepo) {
    this.customerRepo = customerRepo;
  }

  public void generateCustomerDataSetWithDefaults(int n) {
    generateDummyCustomerDataSet("US", "SP1S", new BigDecimal("9.99"), "USD", n);
    generateDummyCustomerDataSet("US", "SP2S", new BigDecimal("10.99"), "USD", n);
    generateDummyCustomerDataSet("US", "SP4S", new BigDecimal("12.99"), "USD", n);
    generateDummyCustomerDataSet("DE", "SP1S", new BigDecimal("4.98"), "EUR", n);
    generateDummyCustomerDataSet("DE", "SP2S", new BigDecimal("6.98"), "EUR", n);
    generateDummyCustomerDataSet("DE", "SP4S", new BigDecimal("8.98"), "EUR", n);

  }

  public void generateDummyCustomerDataSet(String countryCode, String servicePlan, BigDecimal price,
      String currency, int n) {
    List<Customer> customers = new ArrayList<>();
    String customerPrefix = random2LetterPrefix();
    for (int i = 0; i < n; i++) {
      String customerId = customerPrefix + i;
      Customer customer = new Customer(customerId, countryCode, servicePlan, price, currency);
      customers.add(customer);
    }
    customerRepo.saveAll(customers);
  }

  private String random2LetterPrefix() {
    Random r = new Random();
    return new StringBuilder("cust").append((char) (r.nextInt(26) + 'a'))
        .append((char) (r.nextInt(26) + 'a')).append("-").toString();
  }

}
