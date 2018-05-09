package com.netflix.pricing.pricingservice.customer;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;
import org.springframework.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@RedisHash("NFCustomer")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Customer implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @NonNull
  private String id;
  @Indexed
  private String country;
  @Indexed
  private String servicePlan;
  private BigDecimal price;
  private String currency;

}
