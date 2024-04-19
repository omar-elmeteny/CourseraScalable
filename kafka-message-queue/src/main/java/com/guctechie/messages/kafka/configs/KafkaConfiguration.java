package com.guctechie.messages.kafka.configs;

import lombok.Getter;
import lombok.Setter;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
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
        properties.put("key.serializer", StringSerializer.class.getName());
        properties.put("value.serializer", StringSerializer.class.getName());
        properties.put("key.deserializer", StringDeserializer.class.getName());
        properties.put("value.deserializer", StringDeserializer.class.getName());
        // put password
        properties.put("security.protocol", "SASL_PLAINTEXT");
        properties.put("sasl.mechanism", "PLAIN");
        properties.put("group.id", "guc-techie");
        properties.put("sasl.jaas.config", "org.apache.kafka.common.security.plain.PlainLoginModule required username=\""
                + getUsername() + "\""
                + " password=\"" + getPassword() + "\";");


        return properties;
    }
}
