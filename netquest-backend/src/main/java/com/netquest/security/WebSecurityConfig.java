package com.netquest.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers(HttpMethod.GET, "/api/users/me", "/api/users/**").hasAnyAuthority(ADMIN, USER,USER_PREMIUM)
                        .requestMatchers(HttpMethod.PUT, "/api/users/edit").hasAnyAuthority(ADMIN, USER,USER_PREMIUM)
                        .requestMatchers(HttpMethod.DELETE, "/api/users/deleteMyAccount/**").hasAnyAuthority(ADMIN, USER, USER_PREMIUM)
                        .requestMatchers(HttpMethod.POST, "/api/wifi-spot/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/wifi-spot-visit/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/wifi-spot").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/wifi-spot/getAIWifiSpots/**").hasAnyAuthority(ADMIN, USER, USER_PREMIUM)
                        .requestMatchers(HttpMethod.GET, "/api/wifi-spot-visit/my-visits").hasAnyAuthority(ADMIN, USER, USER_PREMIUM)
                        .requestMatchers(HttpMethod.GET, "/api/review/**").hasAnyAuthority(ADMIN, USER, USER_PREMIUM)
                        .requestMatchers(HttpMethod.GET, "/api/review/review-of-wifi-spot/**").hasAnyAuthority(ADMIN, USER, USER_PREMIUM)
                        .requestMatchers("/api/users", "/api/users/**").hasAuthority(ADMIN)
                        .requestMatchers("/public/**", "/auth/**").permitAll()
                        .requestMatchers("/", "/error", "/csrf", "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs", "/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static final String ADMIN = "ADMIN";
    public static final String USER = "USER";

    public static final String USER_PREMIUM = "USER_PREMIUM";
}
