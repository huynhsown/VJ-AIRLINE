package com.vietjoke.vn.config;

import com.vietjoke.vn.config.filter.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class ApiSecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(requests -> {
                    requests
                            .requestMatchers(
                                    "/api/v1/auth/**",
                                    "/api/v1/flight/search",
                                    "/api/v1/location/routes",
                                    "/api/v1/countries",
                                    "/api/v1/airports"
                            ).permitAll()
                            .requestMatchers(
                                    "/api/v1/user/profile",
                                    "/api/v1/avatar",
                                    "/api/v1/payment/**",
                                    "/api/v1/booking/**",
                                    "/api/v1/chat/**",
                                    "/api/v1/flight/select",
                                    "/api/v1/addon/types"
                            ).hasRole("CUSTOMER")
                            .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                            .anyRequest().authenticated();
                });
        return http.build();
    }
}