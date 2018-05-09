package com.netflix.pricing.pricingservice.customer;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.netflix.pricing.pricingservice.exception.ServiceInputValidationException;
import io.swagger.annotations.Api;

@RestController
@RequestMapping("/customers")
@Api(value = "Customer Price Info API",
    description = "Not the main api - used for dummy customer population and verification",
    tags = {"Customers API"})
public class CustomerController {

  private CustomerRepository customerRepo;

  private DummyCustomerDataGenerator dummyDataGenerator;

  List<String> SERVICE_PLANS = (List<String>) java.util.Arrays.asList("SP1S", "SP2S", "SP4S");


  @Autowired
  public void setCustomerDataPopulator(DummyCustomerDataGenerator populate,
      CustomerRepository customerRepo) {
    this.dummyDataGenerator = populate;
    this.customerRepo = customerRepo;
  }



  @GetMapping("")
  Iterable<Customer> getAllCustomersOrByCountryCode(
      @RequestParam(value = "countryCode", required = false, defaultValue = "") String countryCode,
      Pageable pageable) {
    // @PathVariable("COUNTRY_CODE")
    if (StringUtils.isEmpty(countryCode)) {
      return customerRepo.findAll(pageable);
    } else {
      return customerRepo.findByCountry(countryCode, pageable);
    }
  }

  @GetMapping("/{ID}")
  Optional<Customer> getCustomerById(@PathVariable("ID") String id) {
    return customerRepo.findById(id);
  }


  @PostMapping("")
  void createDummyCustomersForS1PS() {
    dummyDataGenerator.generateCustomerDataSetWithDefaults(100);
  }

  @PostMapping("/{COUNTRY_CODE}/{SERVICE_PLAN}/{PRICE}/{CURRENCY}/{COUNT}")
  void createDummyCustomersForCountryServicePlan(@PathVariable("COUNTRY_CODE") String countryCode,
      @PathVariable("SERVICE_PLAN") String servicePlan, @PathVariable("PRICE") BigDecimal price,
      @PathVariable("CURRENCY") String currency, @PathVariable("COUNT") Integer count) {
    if (!SERVICE_PLANS.contains(servicePlan)) {
      throw new ServiceInputValidationException(
          "servicePlan should have one of the values :" + SERVICE_PLANS.toString());
    }
    dummyDataGenerator.generateDummyCustomerDataSet(countryCode, servicePlan, price, currency,
        count);
  }

}
