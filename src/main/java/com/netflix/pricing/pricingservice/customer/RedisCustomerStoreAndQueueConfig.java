package com.netflix.pricing.pricingservice.customer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

@Component
public class RedisCustomerStoreAndQueueConfig {

  public static final String IMPACTED_CUSTOMER_QUEUE = "customerQueue";
  public static final String IMPACTED_CUSTOMER_INPROGRESS_QUEUE_PREFIX = "customerQueue:inProcess:";

  @Value("${spring.redis.host}")
  private String redisHost;

  @Value("${spring.redis.port}")
  private int redisPort;

  @Bean
  JedisConnectionFactory jedisConnectionFactory() {
    RedisStandaloneConfiguration standaloneConfig =
        new RedisStandaloneConfiguration(redisHost, redisPort);
    JedisConnectionFactory jedisConFactory = new JedisConnectionFactory(standaloneConfig);
    return jedisConFactory;
  }

  @Bean
  public RedisTemplate<String, Customer> redisTemplateCustomer() {
    RedisTemplate<String, Customer> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(jedisConnectionFactory());
    redisTemplate.setKeySerializer(new StringRedisSerializer());
    return redisTemplate;
  }

}
