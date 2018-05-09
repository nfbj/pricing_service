package com.netflix.pricing.pricingservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.netflix.pricing.pricingservice.PricingServiceApplication;
import com.netflix.pricing.pricingservice.common.ResourceMessages;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

  public SwaggerConfig() {}

  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2).select()
        .apis(RequestHandlerSelectors
            .basePackage(PricingServiceApplication.class.getPackage().getName()))
        .paths(PathSelectors.any()).build().apiInfo(metaData());

  }

  private ApiInfo metaData() {
    return new ApiInfoBuilder().title(ResourceMessages.getString("SwaggerConfig.Title")) //$NON-NLS-1$
        .description(ResourceMessages.getString("SwaggerConfig.Desc")) //$NON-NLS-1$
        .termsOfServiceUrl(ResourceMessages.getString("SwaggerConfig.Terms")).build(); //$NON-NLS-1$
  }
}
