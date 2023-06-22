package com.tamayo.gcash.exam.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SwaggerConfig {
    @Value("${info.app.name}")
    String applicationName;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .info(new Info().title(applicationName)
                .description("Rest API for " + applicationName + " || Source Code: " + "https://github.com/fvaTamayo/DeliveryCostAPI")
                .contact(new Contact().name("Faustine Tamayo").email("faustine.tamayo@gmail.com"))
            );
    }
}
