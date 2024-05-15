package com.guctechie.datainsertions.services;

import com.guctechie.datainsertions.configs.AppConfig;
import com.guctechie.datainsertions.models.User;
import org.javatuples.Pair;
import org.javatuples.Quartet;
import org.javatuples.Triplet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;

@Service
public class UserGenerator {

    Logger logger = LoggerFactory.getLogger(UserGenerator.class);
    private final ArrayList<String> maleFirstNames;
    private final ArrayList<String> femaleFirstNames;
    private final ArrayList<String> lastNames;
    private final ArrayList<String> emailDomains;
    private final ArrayList<String> userAgents;
    private final ArrayList<String> userBios;
    String[] problems = {"Account hacked", "Forgot password", "Account locked", "Account suspended", "Account deleted"};
    String[] status = {"Open", "In Progress", "Closed"};
    private static final int TOKEN_LENGTH = 16;

    private ArrayList<String> userActivity = new ArrayList<>();
    private final AppConfig appConfig;
    private final Random random = new Random();

    public UserGenerator(AppConfig appConfig) throws IOException {
        maleFirstNames = loadResource("maleFirstNames.txt");
        femaleFirstNames = loadResource("femaleFirstNames.txt");
        lastNames = loadResource("lastNames.txt");
        emailDomains = loadResource("emailDomains.txt");
        userAgents = loadResource("userAgents.txt");
        userActivity = loadResource("userActivity.txt");
        userBios = loadResource("userBios.txt");
        this.appConfig = appConfig;
    }

    public User generateUser() {
        User user = new User();
        user.setFullName(generateFullName());

        user.setUsername(generateUsername(user.getFullName()));
        user.setEmail(generateEmail(user.getUsername()));
        user.setPassword(generatePassword());
        user.setDateOfBirth(generateDateOfBirth());
        user.setRegistrationDate(generateRegistrationDate());
        user.setPhoneVerified(random.nextBoolean());
        user.setEmailVerified(random.nextBoolean());
        user.setProfilePhotoUrl(generateProfilePhotoUrl(user.getUsername()));
        user.setPhoneNumber(generatePhoneNumber());
        user.setBio(generateBio());

        generateSocialMediaLinks(user);
        generateUserRole(user);
        generateUserLoginHistory(user);
        generateUserProblemReports(user);
        generateUserPasswordResetRequests(user);
        generateUserActivityLogs(user);
        return user;
    }

    private String generateBio() {
        return userBios.get(random.nextInt(userBios.size()));
    }

    private void generateUserActivityLogs(User user) {
        for(int i = 0; i < appConfig.getUserActivityLogCount(); i++) {
            String activity = userActivity.get(random.nextInt(userActivity.size()));
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            user.getUserActivityLogs().add(new Pair<>(activity, timestamp));
        }
    }

    public static String generateToken() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] tokenBytes = new byte[TOKEN_LENGTH / 2]; // Each byte represents two characters in hexadecimal
        secureRandom.nextBytes(tokenBytes);
        return new BigInteger(1, tokenBytes).toString(16); // Convert to hexadecimal string
    }

    private void generateUserPasswordResetRequests(User user) {
        for(int i = 0; i < appConfig.getUserPasswordResetRequestCount(); i++) {
            String token = generateToken();
            Timestamp requestedAt = new Timestamp(System.currentTimeMillis());
            user.getPasswordResetRequests().add(new Pair<>(token, requestedAt));
        }
    }


    private void generateUserProblemReports(User user) {
        for(int i = 0; i < appConfig.getUserProblemReportCount(); i++) {
            String problemDescription = problems[random.nextInt(problems.length)];
            String problemStatus = status[random.nextInt(status.length)];
            Timestamp reportedAt = new Timestamp(System.currentTimeMillis());
            user.getProblemReports().add(new Triplet<>(problemDescription, problemStatus, reportedAt));
        }
    }

    private void generateUserLoginHistory(User user) {
        for(int i = 0; i < appConfig.getUserHistoryCount(); i++) {
            boolean loginStatus = random.nextBoolean();
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String ipAddress = random.nextInt(256) + "." + random.nextInt(256) + "." + random.nextInt(256) + "." + random.nextInt(256);
            String userAgent = userAgents.get(random.nextInt(userAgents.size()));
            user.getHistory().add(new Quartet<>(loginStatus, timestamp, ipAddress, userAgent));
        }
    }

    private void generateUserRole(User user) {
        if (random.nextBoolean()) {
            user.getRoleId().add(1);
        }
        if(random.nextBoolean()) {
            user.getRoleId().add(2);
        }
        if(random.nextBoolean()) {
            user.getRoleId().add(3);
        }
    }

    private void generateSocialMediaLinks(User user) {

        if (random.nextBoolean()) {
            user.getSocialMediaLinks().put("Facebook", "https://facebook.com/" + user.getUsername());
        }
        if (random.nextBoolean()) {
            user.getSocialMediaLinks().put("Twitter", "https://twitter.com/" + user.getUsername());
        }
        if (random.nextBoolean()) {
            user.getSocialMediaLinks().put("Instagram", "https://instagram.com/" + user.getUsername());
        }
        if (random.nextBoolean()) {
            user.getSocialMediaLinks().put("LinkedIn", "https://linkedin.com/" + user.getUsername());
        }

    }

    private String generateProfilePhotoUrl(String username) {
        if (random.nextBoolean()) {
            return "https://guctechie.com/" + username + ".jpg";
        }
        return null;
    }

    private String generatePhoneNumber() {
        StringBuilder phoneNumber = new StringBuilder();
        phoneNumber.append("+");
        phoneNumber.append(random.nextInt(9) + 1);
        for (int i = 0; i < 9; i++) {
            phoneNumber.append(random.nextInt(10));
        }
        return phoneNumber.toString();
    }

    private String generatePassword() {
        StringBuilder password = new StringBuilder();
        int length = random.nextInt(10) + 8;
        for (int i = 0; i < length; i++) {
            String passwordChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789#$%!&()[]{}/?.+-*=";
            password.append(passwordChars.charAt(random.nextInt(passwordChars.length())));
        }
        return password.toString();
    }

    private Date generateDateOfBirth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, random.nextInt(71) + 1940);
        calendar.set(Calendar.MONTH, random.nextInt(12));
        calendar.set(Calendar.DAY_OF_MONTH, random.nextInt(31) + 1);
        return new java.sql.Date(calendar.getTimeInMillis());
    }

    private Date generateRegistrationDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, random.nextInt(2) + 2022);
        calendar.set(Calendar.MONTH, random.nextInt(12));
        calendar.set(Calendar.DAY_OF_MONTH, random.nextInt(31) + 1);
        return new java.sql.Date(calendar.getTimeInMillis());
    }

    private String generateEmail(String username) {
        return username + "@" + emailDomains.get(random.nextInt(emailDomains.size()));
    }

    private String generateUsername(String fullName) {
        return fullName.toLowerCase().replace(" ", ".");
    }

    private String generateFullName() {
        String firstName;
        if (random.nextBoolean()) {
            firstName = maleFirstNames.get(random.nextInt(maleFirstNames.size()));
        } else {
            firstName = femaleFirstNames.get(random.nextInt(femaleFirstNames.size()));
        }
        String lastName = lastNames.get(random.nextInt(lastNames.size()));

        firstName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1).toLowerCase();
        lastName = lastName.substring(0, 1).toUpperCase() + lastName.substring(1).toLowerCase();
        return firstName + " " + lastName;
    }

    private ArrayList<String> loadResource(String path) throws IOException {
        logger.info("Loading resource {}", path);
        ArrayList<String> list = new ArrayList<>();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path)) {
            if (inputStream == null) {
                throw new IOException("Resource not found");
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    list.add(line);
                }
            }
        } catch (IOException e) {
            logger.error("Loading resource {} {}", path, e.getMessage());
            throw e;
        }
        logger.info("Resource {} loaded with {} entries", path, list.size());
        return list;
    }
}
