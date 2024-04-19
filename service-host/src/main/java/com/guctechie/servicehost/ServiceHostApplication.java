package com.guctechie.servicehost;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.guctechie.kafkamessagequeue")
public class ServiceHostApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceHostApplication.class, args);
    }

}
