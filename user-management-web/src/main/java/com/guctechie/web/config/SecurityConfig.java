package com.guctechie.web.config;

import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true)
public class SecurityConfig {

    private final String[] permitAll = {"/api/v1/auth/login",
            "/api/v1/auth/register",
            "api/v1/auth/refresh-token",
            "/api/v1/auth/verify-email",
            "/api/v1/auth/forgot-password",
            "/api/v1/auth/verify-email"};
    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        req -> req.requestMatchers(permitAll)
                                .anonymous()
                                .anyRequest()
                                .authenticated()
                ).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore((Filter) jwtAuthFilter, (Class<? extends Filter>) UsernamePasswordAuthenticationFilter.class)
                .logout( logout ->
                        logout.logoutUrl("/logout")
                                .logoutSuccessHandler((req, res, auth) ->{
                                    SecurityContextHolder.clearContext();
                                    res.setStatus(200);
                                })
                );
        return http.build();
    }

}
