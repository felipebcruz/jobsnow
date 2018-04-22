package br.com.jobsnow.database;

import static springfox.documentation.builders.PathSelectors.regex;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@Profile("!test")
public class SwaggerConfig {

	@Bean
    public Docket productApi() { 
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("br.com.jobsnow.database"))
                .paths(regex("/api/v1/database.*"))
                .build()
                .apiInfo(metaInfo());
    }

    private ApiInfo metaInfo() {
        ApiInfo apiInfo = new ApiInfo(
                "Jobs Now Database API Rest",
                "API REST para interagir com o banco de dados do jobsnow.",
                "1.0",
                "Terms of Service",
                new Contact("Jobs Now", "https://www.github.com/jobsnow","ccpjobsnow@gmail.com"),
                "Jobs Now License Version 1.0",
                "https://www.jobsnow.com/license.html",Collections.emptyList());

        return apiInfo;
    }	
}