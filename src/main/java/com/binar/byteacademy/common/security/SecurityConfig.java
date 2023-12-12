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

import static com.binar.byteacademy.common.util.Constants.BankTransferDetailPats.BANK_TRANSFER_DETAIL_PATS_ALL;
import static com.binar.byteacademy.common.util.Constants.CoursePats.*;
import static com.binar.byteacademy.common.util.Constants.PurchasePats.*;
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
                                .requestMatchers(GET, ADMIN_COURSE_PATS).hasAnyAuthority(ADMIN_READ.getPermission())
                                .requestMatchers(POST, ADMIN_COURSE_PATS).hasAnyAuthority(ADMIN_CREATE.getPermission())
                                .requestMatchers(PUT, ADMIN_COURSE_PATS).hasAnyAuthority(ADMIN_UPDATE.getPermission())
                                .requestMatchers(DELETE, ADMIN_COURSE_PATS).hasAnyAuthority(ADMIN_DELETE.getPermission())
                                .requestMatchers(GET, CUSTOMER_COURSE_PATS).hasAnyAuthority(CUSTOMER_READ.getPermission())
                                .requestMatchers(POST, PURCHASE_PATS_ALL).hasAnyAuthority(CUSTOMER_CREATE.getPermission())
                                .requestMatchers(GET, PURCHASE_PATS_ALL).hasAnyAuthority(CUSTOMER_READ.getPermission())
                                .requestMatchers(GET, ADMIN_PURCHASE_PATS).hasAnyAuthority(ADMIN_READ.getPermission())
                                .requestMatchers(PUT, ADMIN_PURCHASE_PATS_ALL).hasAnyAuthority(ADMIN_UPDATE.getPermission())
                                .requestMatchers(POST, BANK_TRANSFER_DETAIL_PATS_ALL).hasAnyAuthority(ADMIN_CREATE.getPermission())
                                .requestMatchers(PUT, BANK_TRANSFER_DETAIL_PATS_ALL).hasAnyAuthority(ADMIN_UPDATE.getPermission())
                                .requestMatchers(DELETE, BANK_TRANSFER_DETAIL_PATS_ALL).hasAnyAuthority(ADMIN_DELETE.getPermission())
                                .requestMatchers(GET, BANK_TRANSFER_DETAIL_PATS_ALL).hasAnyAuthority(ADMIN_READ.getPermission())
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