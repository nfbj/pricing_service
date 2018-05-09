package com.netflix.pricing.pricingservice.customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends PagingAndSortingRepository<Customer, String> {

  Page<Customer> findByCountryAndServicePlan(String country, String servicePlan, Pageable pageable);

  Page<Customer> findByCountry(String country, Pageable pageable);

}
