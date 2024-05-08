package com.guctechie.users.configs;

import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import lombok.Getter;
import lombok.Setter;
import org.apache.kafka.common.utils.Java;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;

@Configuration
@ConfigurationProperties(prefix = "guctechie.mailer")
@Getter
@Setter
public class MailConfig {
    private String host = "smtp.gmail.com";
    private int port = 587;
    private String username = "";
    private String password = "";


    public JavaMailSender getJavaMailSender() {
            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
            mailSender.setHost(getHost());
            mailSender.setPort(getPort());

            mailSender.setUsername(getUsername());
            mailSender.setPassword(getPassword());

            Properties props = mailSender.getJavaMailProperties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.debug", "true");

            return mailSender;
        }


}
