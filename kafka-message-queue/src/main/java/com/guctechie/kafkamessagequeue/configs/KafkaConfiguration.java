package com.guctechie.kafkamessagequeue.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
@ConfigurationProperties(prefix = "guctechie.kafka")
@Getter
@Setter
public class KafkaConfiguration {
    private String servers;
    private String username;
    private String password;

    public Properties createKafkaProperties() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", getServers());
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        // put password
        properties.put("security.protocol", "SASL_PLAINTEXT");
        properties.put("sasl.mechanism", "PLAIN");
        properties.put("sasl.jaas.config", "org.apache.kafka.common.security.plain.PlainLoginModule required username=\""
                + getUsername() + "\""
                + " password=\"" + getPassword() + "\";");


        return properties;
    }
}
