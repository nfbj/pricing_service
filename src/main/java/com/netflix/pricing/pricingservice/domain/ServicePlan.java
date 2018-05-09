package com.netflix.pricing.pricingservice.domain;

/**
 * Service plan Enum enumeration the list of services. (Note: Assumpting the service plans are fixed
 * (atleast frequently changing), should be modelled as an object loaded from property files/db)
 * 
 */
public enum ServicePlan {

  SP1S(1, "Only Stream"), SP2S(2, "Two parallel Streams"), SP4S(4, "4 Parallel Streams");

  final int streamCount;
  final String description;

  ServicePlan(int streamCount, String desc) {
    this.streamCount = streamCount;
    this.description = desc;
  }

  public int getStreamCount() {
    return streamCount;
  }

  public String getDescriptoin() {
    return description;
  }
}
