package com.guctechie.users.services;

import com.guctechie.users.configs.MailConfig;
import com.guctechie.users.models.OTPMailModel;
import com.guctechie.users.models.UserProfileData;
import com.guctechie.users.repositories.UserRepository;
import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.io.*;
import java.util.Date;

@Service
public class MailService {

    private final MailConfig mailConfig;
    private final Mustache.Compiler compiler;
    private final UserRepository userRepository;
    private final OTPGenerator otpGenerator;
    private final PasswordEncoder passwordEncoder;

    private String environment;


    public MailService(MailConfig mailConfig, Mustache.Compiler templateLoader, UserRepository userRepository, OTPGenerator otpGenerator, PasswordEncoder passwordEncoder) {
        this.mailConfig = mailConfig;
        this.compiler = templateLoader;
        this.userRepository = userRepository;
        this.otpGenerator = otpGenerator;
        this.passwordEncoder = passwordEncoder;
        this.environment = System.getProperty("app.environment", "testing");
    }

    public void sendWelcomeEmail(UserProfileData user) {
        sendOtpMail(user, "Welcome to GUCTechie Coursera", "Register");
    }

    public void sendForgotPasswordEmail(UserProfileData user) {
        sendOtpMail(user, "Reset your GUCTechie Coursera Password", "ForgotPassword");
    }

    private void sendOtpMail(UserProfileData user, String subject, String template) {
        String otp = otpGenerator.generateOTP();
        String otpEncoded = passwordEncoder.encode(otp);
        userRepository.createPasswordResetRequest(user.getUserId(), otpEncoded, new Date(System.currentTimeMillis()
                + 3 * 24 * 60 * 60 * 1000));

        OTPMailModel model = OTPMailModel.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .oneTimePassword(otp)
                .email(user.getEmail())
                .build();
        sendMail(user.getEmail(), subject, template, model);
    }

    private void sendMail(String to, String subject, String template, Object model) {

        if(environment.equals("testing")){
            return;
        }
        try {

            JavaMailSender mailSender = mailConfig.getJavaMailSender();

            MimeMessage message = mailSender.createMimeMessage();
            message.setFrom(mailConfig.getUsername());
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
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
