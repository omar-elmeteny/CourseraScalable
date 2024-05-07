package com.guctechie.web.users.services;

import com.guctechie.messages.CommandNames;
import com.guctechie.messages.configs.WebServerConfig;
import com.guctechie.messages.exceptions.MessageQueueException;
import com.guctechie.messages.services.CommandDispatcher;
import com.guctechie.users.models.*;
import com.guctechie.web.users.dtos.JwtResponseDTO;
import com.guctechie.web.users.models.UserSecurityDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtService {
    private final WebServerConfig webServerConfig;
    // retrieve the secret key from the environment variable
    private final String tokenSecret;
    private final CommandDispatcher commandDispatcher;
    private final Logger logger;

    public JwtService(WebServerConfig webServerConfig, CommandDispatcher commandDispatcher) {
        this.webServerConfig = webServerConfig;
        this.commandDispatcher = commandDispatcher;
        String secret = System.getProperty("SECRET_KEY");
        if (secret == null) {
            secret = webServerConfig.getSecret();
        }
        this.tokenSecret = Base64.getEncoder().encodeToString(secret.getBytes());
        logger = org.slf4j.LoggerFactory.getLogger(JwtService.class);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    @SuppressWarnings("unchecked")
    private static ArrayList<String> extractRoles(Claims claims) {
        return claims.get("roles", ArrayList.class);
    }

    public JwtResponseDTO refreshTokens(String refreshToken) throws MessageQueueException {
        Claims claims;
        try {
            claims = extractAllClaims(refreshToken);
        } catch (JwtException e) {
            logger.warn("Invalid refresh token", e);
            return null;
        }
        Function<Claims, Date> expFunction = Claims::getExpiration;
        Date expiration = expFunction.apply(claims);

        if (expiration == null || expiration.before(new Date())) {
            logger.warn("Refresh token expired");
            return null;
        }

        Function<Claims, Date> issFunction = Claims::getIssuedAt;
        Date issuedAt = issFunction.apply(claims);
        if (issuedAt == null) {
            logger.warn("refresh token missing issued at");
            return null;
        }

        Function<Claims, String> subFunction = Claims::getSubject;
        String username = subFunction.apply(claims);

        if (username == null) {
            logger.warn("refresh token missing subject");
            return null;
        }

        String usage = claims.get("token_usage", String.class);
        if (!"refresh_token".equals(usage)) {
            logger.warn("refresh token not a refresh token");
            return null;
        }

        return generateTokens(username, issuedAt);
    }

    public UserDetails validateAccessToken(String token) {
        Claims claims;
        try {
            claims = extractAllClaims(token);
        } catch (JwtException e) {
            logger.warn("Invalid access token", e);
            return null;
        }
        Function<Claims, Date> dateFunction = Claims::getExpiration;
        Date expiration = dateFunction.apply(claims);

        if (expiration.before(new Date())) {
            logger.warn("Access token expired");
            return null;
        }
        Function<Claims, String> subFunction = Claims::getSubject;
        String userIdStr = subFunction.apply(claims);
        int userId = Integer.parseInt(userIdStr);

        String usage = claims.get("token_usage", String.class);
        if (!"access_token".equals(usage)) {
            logger.warn("access token not an access token");
            return null;
        }
        String username = claims.get("name", String.class);
        if (username == null) {
            logger.warn("access token missing name");
            return null;
        }
        ArrayList<String> roles = extractRoles(claims);
        UserStatus userStatus = UserStatus.builder()
                .userId(userId)
                .roles(roles)
                .username(username)
                .locked(false)
                .deleted(false)
                .emailVerified(true)
                .build();

        return new UserSecurityDetails(userStatus);
    }

    public JwtResponseDTO generateTokens(String username, Date refreshIssueDate) throws MessageQueueException {
        var profileData = getUserProfileData(username);
        if (profileData == null) {
            logger.error("User not found with username: {}", username);
            throw new IllegalArgumentException("User not found");
        }
        var userStatus = getUserStatus(profileData.getUserId());
        if (userStatus == null) {
            logger.error("User not found with id: {}", profileData.getUserId());
            throw new IllegalArgumentException("User not found");
        }

        if (userStatus.isLocked()) {
            logger.warn("User is locked");
            throw new IllegalArgumentException("User is locked");
        }

        if (userStatus.isDeleted()) {
            logger.warn("User is deleted");
            throw new IllegalArgumentException("User is deleted");
        }

        if (!userStatus.isEmailVerified()) {
            logger.warn("Email not verified");
            throw new IllegalArgumentException("Email not verified");
        }

        if (refreshIssueDate != null && userStatus.getPasswordDate().after(refreshIssueDate)) {
            logger.warn("Password changed after refresh token issued");
            throw new IllegalArgumentException("Password changed after refresh token issued");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("token_usage", "refresh_token");
        String refreshToken = createToken(claims, username, webServerConfig.getRefreshTokenDurationDays() * 24 * 60);

        claims = new HashMap<>();
        claims.put("roles", userStatus.getRoles());
        claims.put("userId", userStatus.getUserId());
        claims.put("name", userStatus.getUsername());
        claims.put("email", profileData.getEmail());
        claims.put("given_name", profileData.getFirstName());
        claims.put("family_name", profileData.getLastName());
        claims.put("email_verified", true);
        claims.put("token_type", "Bearer");
        claims.put("token_usage", "access_token");
        String accessToken = createToken(claims, Integer.toString(userStatus.getUserId()), webServerConfig.getAccessTokenDurationMinutes());

        logger.info("Generated tokens for user: {}", username);
        return JwtResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private String createToken(Map<String, Object> claims, String subject, int durationMinutes) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ 1000L *60*durationMinutes))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(tokenSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private UserProfileData getUserProfileData(String username) throws MessageQueueException {
        return commandDispatcher.sendCommand(
                CommandNames.GET_USER_BY_NAME,
                UserByNameRequest.builder()
                        .username(username)
                        .build()
                ,
                UserByNameResult.class).getUser();
    }

    private UserStatus getUserStatus(int userId) throws MessageQueueException {
        return commandDispatcher.sendCommand(
                CommandNames.USER_STATUS,
                UserStatusRequest.builder()
                        .userId(userId)
                        .build(),
                UserStatusResult.class).getUserStatus();
    }
}