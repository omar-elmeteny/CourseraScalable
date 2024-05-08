package com.guctechie.users.services;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class OTPGenerator {
    private static final String CHARACTERS = "0123456789";
    private static final int LENGTH = 6;

    public String generateOTP() {
        SecureRandom random = new SecureRandom();

        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            otp.append(CHARACTERS.charAt(index));
        }

        return otp.toString();
    }
}
