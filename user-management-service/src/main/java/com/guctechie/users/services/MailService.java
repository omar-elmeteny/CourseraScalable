package com.guctechie.users.services;

import com.guctechie.users.configs.MailConfig;
import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


import java.io.*;

@Service
public class MailService {

    private final MailConfig mailConfig;
    private final Mustache.Compiler compiler;


    public MailService(MailConfig mailConfig, Mustache.Compiler templateLoader) {
        this.mailConfig = mailConfig;
        this.compiler = templateLoader;
    }


    public void sendMail(String to, String subject, String template, Object model) {
      try {

        JavaMailSender mailSender = mailConfig.getJavaMailSender();

    MimeMessage message = mailSender.createMimeMessage();
            message.setFrom(mailConfig.getUsername());
            MimeMessageHelper helper = null;
            helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);

            Reader reader = new InputStreamReader(new ClassPathResource(template + ".mustache").getInputStream());
            Template compiledTemplate = compiler.compile(reader);

            String mailContent = compiledTemplate.execute(model);
            helper.setText(mailContent, true);

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
