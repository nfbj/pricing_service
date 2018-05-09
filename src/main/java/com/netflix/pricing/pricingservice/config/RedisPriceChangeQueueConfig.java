package com.netflix.pricing.pricingservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@Configuration
public class RedisPriceChangeQueueConfig {

  public static final String PRICE_CHANGE_QUEUE = "priceChangeList";
  public static final String PRICE_CHANGE_QUEUE_INPROGRESS = "priceChange:inProcess:";

  @Bean
  RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory) {

    RedisMessageListenerContainer container = new RedisMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    return container;
  }


}
