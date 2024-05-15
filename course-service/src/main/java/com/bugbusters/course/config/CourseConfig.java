package com.bugbusters.course.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class CourseConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
