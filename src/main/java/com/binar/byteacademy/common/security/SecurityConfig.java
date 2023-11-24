package com.binar.byteacademy.common.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.binar.byteacademy.common.util.Constants.CoursePats.COURSE_PATS;
import static com.binar.byteacademy.enumeration.EnumPermission.*;
import static org.springframework.http.HttpMethod.*;

import static com.binar.byteacademy.common.util.Constants.CommonPats.WHITE_LIST_PATS;
import static com.binar.byteacademy.common.util.Constants.CategoryPats.CATEGORY_PATS_ALL;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        authorize -> authorize
                                .requestMatchers(WHITE_LIST_PATS)
                                .permitAll()
                                .requestMatchers(GET, CATEGORY_PATS_ALL).hasAnyAuthority(ADMIN_READ.getPermission(), CUSTOMER_READ.getPermission())
                                .requestMatchers(POST, CATEGORY_PATS_ALL).hasAnyAuthority(ADMIN_CREATE.getPermission())
                                .requestMatchers(PUT, CATEGORY_PATS_ALL).hasAnyAuthority(ADMIN_UPDATE.getPermission())
                                .requestMatchers(DELETE, CATEGORY_PATS_ALL).hasAnyAuthority(ADMIN_DELETE.getPermission())
                                .requestMatchers(GET, COURSE_PATS).hasAnyAuthority(ADMIN_READ.getPermission(), CUSTOMER_READ.getPermission())
                                .requestMatchers(POST, COURSE_PATS).hasAnyAuthority(ADMIN_CREATE.getPermission())
                                .requestMatchers(PUT, COURSE_PATS).hasAnyAuthority(ADMIN_UPDATE.getPermission())
                                .requestMatchers(DELETE, COURSE_PATS).hasAnyAuthority(ADMIN_DELETE.getPermission())
                                .requestMatchers("/api/v1/**")
                                .authenticated()
                )
                .exceptionHandling(
                        exception -> exception
                                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}