package com.geulkkoli;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.thymeleaf.spring5.context.SpringContextUtils;

import java.util.Arrays;

@EnableJpaAuditing
@SpringBootApplication
public class GeulkkoliApplication {

    public static void main(String[] args) {
        SpringApplication.run(GeulkkoliApplication.class, args);
    }

}
