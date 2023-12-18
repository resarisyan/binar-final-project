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

import static com.binar.byteacademy.common.util.Constants.CategoryPats.ADMIN_CATEGORY_PATS_ALL;
import static com.binar.byteacademy.common.util.Constants.CoursePats.ADMIN_COURSE_PATS_ALL;
import static com.binar.byteacademy.common.util.Constants.MaterialPats.ADMIN_MATERIAL_PATS_ALL;
import static com.binar.byteacademy.common.util.Constants.PromoPats.ADMIN_PROMO_PATS_ALL;
import static com.binar.byteacademy.common.util.Constants.SettingPats.SETTING_PATS_ALL;
import static com.binar.byteacademy.common.util.Constants.ChapterPats.ADMIN_CHAPTER_PATS_ALL;

import static com.binar.byteacademy.enumeration.EnumPermission.*;
import static org.springframework.http.HttpMethod.*;

import static com.binar.byteacademy.common.util.Constants.CommonPats.SECURE_LIST_PATS;

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
                                .requestMatchers(GET, ADMIN_CATEGORY_PATS_ALL).hasAnyAuthority(ADMIN_READ.getPermission())
                                .requestMatchers(POST, ADMIN_CATEGORY_PATS_ALL).hasAnyAuthority(ADMIN_CREATE.getPermission())
                                .requestMatchers(PUT, ADMIN_CATEGORY_PATS_ALL).hasAnyAuthority(ADMIN_UPDATE.getPermission())
                                .requestMatchers(DELETE, ADMIN_CATEGORY_PATS_ALL).hasAnyAuthority(ADMIN_DELETE.getPermission())
                                .requestMatchers(GET, ADMIN_COURSE_PATS_ALL).hasAnyAuthority(ADMIN_READ.getPermission())
                                .requestMatchers(POST, ADMIN_COURSE_PATS_ALL).hasAnyAuthority(ADMIN_CREATE.getPermission())
                                .requestMatchers(PUT, ADMIN_COURSE_PATS_ALL).hasAnyAuthority(ADMIN_UPDATE.getPermission())
                                .requestMatchers(DELETE, ADMIN_COURSE_PATS_ALL).hasAnyAuthority(ADMIN_DELETE.getPermission())
                                .requestMatchers(GET, ADMIN_CHAPTER_PATS_ALL).hasAnyAuthority(ADMIN_READ.getPermission())
                                .requestMatchers(POST, ADMIN_CHAPTER_PATS_ALL).hasAnyAuthority(ADMIN_CREATE.getPermission())
                                .requestMatchers(PUT, ADMIN_CHAPTER_PATS_ALL).hasAnyAuthority(ADMIN_UPDATE.getPermission())
                                .requestMatchers(DELETE, ADMIN_CHAPTER_PATS_ALL).hasAnyAuthority(ADMIN_DELETE.getPermission())
                                .requestMatchers(GET, ADMIN_MATERIAL_PATS_ALL).hasAnyAuthority(ADMIN_READ.getPermission())
                                .requestMatchers(POST, ADMIN_MATERIAL_PATS_ALL).hasAnyAuthority(ADMIN_CREATE.getPermission())
                                .requestMatchers(PUT, ADMIN_MATERIAL_PATS_ALL).hasAnyAuthority(ADMIN_UPDATE.getPermission())
                                .requestMatchers(DELETE, ADMIN_MATERIAL_PATS_ALL).hasAnyAuthority(ADMIN_DELETE.getPermission())
                                .requestMatchers(GET, ADMIN_PROMO_PATS_ALL).hasAnyAuthority(ADMIN_READ.getPermission())
                                .requestMatchers(POST, ADMIN_PROMO_PATS_ALL).hasAnyAuthority(ADMIN_CREATE.getPermission())
                                .requestMatchers(PUT, ADMIN_PROMO_PATS_ALL).hasAnyAuthority(ADMIN_UPDATE.getPermission())
                                .requestMatchers(DELETE, ADMIN_PROMO_PATS_ALL).hasAnyAuthority(ADMIN_DELETE.getPermission())
                                .requestMatchers(GET, SETTING_PATS_ALL).hasAnyAuthority(CUSTOMER_READ.getPermission())
                                .requestMatchers(POST, SETTING_PATS_ALL).hasAnyAuthority(CUSTOMER_CREATE.getPermission())
                                .requestMatchers(PUT, SETTING_PATS_ALL).hasAnyAuthority(CUSTOMER_UPDATE.getPermission())
                                .requestMatchers(DELETE, SETTING_PATS_ALL).hasAnyAuthority(CUSTOMER_DELETE.getPermission())
                                .requestMatchers(SECURE_LIST_PATS)
                                .authenticated()
                                .anyRequest()
                                .permitAll()
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