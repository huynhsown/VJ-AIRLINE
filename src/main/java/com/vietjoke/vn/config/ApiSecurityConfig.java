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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class ApiSecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors() // Enable CORS config
                .and()
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(requests -> {
                    requests
                            .requestMatchers(
                                    "/api/v1/auth/**",
                                    "/api/v1/flight/search",
                                    "/api/v1/flight/select",
                                    "/api/v1/location/routes",
                                    "/api/v1/countries",
                                    "/api/v1/airports",
                                    "/v3/api-docs/**",
                                    "/swagger-ui/**",
                                    "/swagger-ui.html"
                            ).permitAll()
                            .requestMatchers(
                                    "/api/v1/user/profile",
                                    "/api/v1/avatar",
                                    "/api/v1/payment/**",
                                    "/api/v1/booking/**",
                                    "/api/v1/chat/**",
                                    "/api/v1/addon/types"
                            ).hasRole("CUSTOMER")
                            .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                            .anyRequest().authenticated();
                });
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*")); // Cho phép tất cả origin
        config.setAllowedMethods(List.of("*"));        // Cho phép tất cả phương thức
        config.setAllowedHeaders(List.of("*"));        // Cho phép tất cả header
        config.setAllowCredentials(true);              // Cho phép gửi cookie (nếu cần)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
