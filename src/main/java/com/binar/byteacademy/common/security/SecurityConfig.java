package com.binar.byteacademy.common.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.binar.byteacademy.common.util.Constants.CategoryPats.ADMIN_CATEGORY_PATS_ALL;
import static com.binar.byteacademy.common.util.Constants.ChatOpenAiPats.CUSTOMER_CHAT_OPEN_AI_PATS_ALL;
import static com.binar.byteacademy.common.util.Constants.CoursePats.ADMIN_COURSE_PATS_ALL;
import static com.binar.byteacademy.common.util.Constants.CoursePats.CUSTOMER_COURSE_PATS_ALL;
import static com.binar.byteacademy.common.util.Constants.MaterialPats.ADMIN_MATERIAL_PATS_ALL;
import static com.binar.byteacademy.common.util.Constants.PromoPats.ADMIN_PROMO_PATS_ALL;
import static com.binar.byteacademy.common.util.Constants.ChapterPats.ADMIN_CHAPTER_PATS_ALL;
import static com.binar.byteacademy.common.util.Constants.PurchasePats.ADMIN_PURCHASE_PATS_ALL;
import static com.binar.byteacademy.common.util.Constants.NotificationPats.ADMIN_NOTIFICATION_PATS_ALL;
import static com.binar.byteacademy.common.util.Constants.CoursePromoPats.ADMIN_COURSE_PROMO_PATS_ALL;
import static com.binar.byteacademy.common.util.Constants.SettingPats.SETTING_PATS_ALL;
import static com.binar.byteacademy.common.util.Constants.PurchasePats.CUSTOMER_PURCHASE_PATS_ALL;
import static com.binar.byteacademy.common.util.Constants.NotificationPats.CUSTOMER_NOTIFICATION_PATS_ALL;
import static com.binar.byteacademy.common.util.Constants.DiscussionPats.CUSTOMER_DISCUSSION_PATS_ALL;
import static com.binar.byteacademy.common.util.Constants.DiscussionPats.ADMIN_DISCUSSION_PATS_ALL;
import static com.binar.byteacademy.common.util.Constants.CommentPats.CUSTOMER_COMMENT_PATS_ALL;
import static com.binar.byteacademy.common.util.Constants.ReplyPats.CUSTOMER_REPLY_PATS_ALL;
import static com.binar.byteacademy.common.util.Constants.MaterialPats.CUSTOMER_MATERIAL_PATS_ALL;
import static com.binar.byteacademy.common.util.Constants.UserPats.CUSTOMER_USER_PATS_ALL;
import static com.binar.byteacademy.common.util.Constants.UserPats.ADMIN_USER_PATS_ALL;

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
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
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
                                .requestMatchers(GET, ADMIN_COURSE_PROMO_PATS_ALL).hasAnyAuthority(ADMIN_READ.getPermission())
                                .requestMatchers(POST, ADMIN_COURSE_PROMO_PATS_ALL).hasAnyAuthority(ADMIN_CREATE.getPermission())
                                .requestMatchers(PUT, ADMIN_COURSE_PROMO_PATS_ALL).hasAnyAuthority(ADMIN_UPDATE.getPermission())
                                .requestMatchers(DELETE, ADMIN_COURSE_PROMO_PATS_ALL).hasAnyAuthority(ADMIN_DELETE.getPermission())
                                .requestMatchers(GET, ADMIN_PURCHASE_PATS_ALL).hasAnyAuthority(ADMIN_READ.getPermission())
                                .requestMatchers(POST, ADMIN_PURCHASE_PATS_ALL).hasAnyAuthority(ADMIN_CREATE.getPermission())
                                .requestMatchers(PUT, ADMIN_PURCHASE_PATS_ALL).hasAnyAuthority(ADMIN_UPDATE.getPermission())
                                .requestMatchers(DELETE, ADMIN_PURCHASE_PATS_ALL).hasAnyAuthority(ADMIN_DELETE.getPermission())
                                .requestMatchers(GET, SETTING_PATS_ALL).hasAnyAuthority(CUSTOMER_READ.getPermission())
                                .requestMatchers(POST, SETTING_PATS_ALL).hasAnyAuthority(CUSTOMER_CREATE.getPermission())
                                .requestMatchers(PUT, SETTING_PATS_ALL).hasAnyAuthority(CUSTOMER_UPDATE.getPermission())
                                .requestMatchers(DELETE, SETTING_PATS_ALL).hasAnyAuthority(CUSTOMER_DELETE.getPermission())
                                .requestMatchers(GET, CUSTOMER_PURCHASE_PATS_ALL).hasAnyAuthority(CUSTOMER_READ.getPermission())
                                .requestMatchers(POST, CUSTOMER_PURCHASE_PATS_ALL).hasAnyAuthority(CUSTOMER_CREATE.getPermission())
                                .requestMatchers(PUT, CUSTOMER_PURCHASE_PATS_ALL).hasAnyAuthority(CUSTOMER_UPDATE.getPermission())
                                .requestMatchers(DELETE, CUSTOMER_PURCHASE_PATS_ALL).hasAnyAuthority(CUSTOMER_DELETE.getPermission())
                                .requestMatchers(GET, CUSTOMER_NOTIFICATION_PATS_ALL).hasAnyAuthority(CUSTOMER_READ.getPermission())
                                .requestMatchers(POST, CUSTOMER_NOTIFICATION_PATS_ALL).hasAnyAuthority(CUSTOMER_CREATE.getPermission())
                                .requestMatchers(PUT, CUSTOMER_NOTIFICATION_PATS_ALL).hasAnyAuthority(CUSTOMER_UPDATE.getPermission())
                                .requestMatchers(DELETE, CUSTOMER_NOTIFICATION_PATS_ALL).hasAnyAuthority(CUSTOMER_DELETE.getPermission())
                                .requestMatchers(GET, CUSTOMER_DISCUSSION_PATS_ALL).hasAnyAuthority(CUSTOMER_READ.getPermission())
                                .requestMatchers(POST, CUSTOMER_DISCUSSION_PATS_ALL).hasAnyAuthority(CUSTOMER_CREATE.getPermission())
                                .requestMatchers(PUT, CUSTOMER_DISCUSSION_PATS_ALL).hasAnyAuthority(CUSTOMER_UPDATE.getPermission())
                                .requestMatchers(DELETE, CUSTOMER_DISCUSSION_PATS_ALL).hasAnyAuthority(CUSTOMER_DELETE.getPermission())
                                .requestMatchers(GET, CUSTOMER_COMMENT_PATS_ALL).hasAnyAuthority(CUSTOMER_READ.getPermission())
                                .requestMatchers(POST, CUSTOMER_COMMENT_PATS_ALL).hasAnyAuthority(CUSTOMER_CREATE.getPermission())
                                .requestMatchers(PUT, CUSTOMER_COMMENT_PATS_ALL).hasAnyAuthority(CUSTOMER_UPDATE.getPermission())
                                .requestMatchers(DELETE, CUSTOMER_COMMENT_PATS_ALL).hasAnyAuthority(CUSTOMER_DELETE.getPermission())
                                .requestMatchers(GET, CUSTOMER_REPLY_PATS_ALL).hasAnyAuthority(CUSTOMER_READ.getPermission())
                                .requestMatchers(POST, CUSTOMER_REPLY_PATS_ALL).hasAnyAuthority(CUSTOMER_CREATE.getPermission())
                                .requestMatchers(PUT, CUSTOMER_REPLY_PATS_ALL).hasAnyAuthority(CUSTOMER_UPDATE.getPermission())
                                .requestMatchers(DELETE, CUSTOMER_REPLY_PATS_ALL).hasAnyAuthority(CUSTOMER_DELETE.getPermission())
                                .requestMatchers(GET, ADMIN_NOTIFICATION_PATS_ALL).hasAnyAuthority(ADMIN_READ.getPermission())
                                .requestMatchers(POST, ADMIN_NOTIFICATION_PATS_ALL).hasAnyAuthority(ADMIN_CREATE.getPermission())
                                .requestMatchers(PUT, ADMIN_NOTIFICATION_PATS_ALL).hasAnyAuthority(ADMIN_UPDATE.getPermission())
                                .requestMatchers(DELETE, ADMIN_NOTIFICATION_PATS_ALL).hasAnyAuthority(ADMIN_DELETE.getPermission())
                                .requestMatchers(GET, ADMIN_USER_PATS_ALL).hasAnyAuthority(ADMIN_READ.getPermission())
                                .requestMatchers(POST, ADMIN_USER_PATS_ALL).hasAnyAuthority(ADMIN_CREATE.getPermission())
                                .requestMatchers(PUT, ADMIN_USER_PATS_ALL).hasAnyAuthority(ADMIN_UPDATE.getPermission())
                                .requestMatchers(DELETE, ADMIN_USER_PATS_ALL).hasAnyAuthority(ADMIN_DELETE.getPermission())
                                .requestMatchers(GET, CUSTOMER_MATERIAL_PATS_ALL).hasAnyAuthority(CUSTOMER_READ.getPermission())
                                .requestMatchers(POST, CUSTOMER_MATERIAL_PATS_ALL).hasAnyAuthority(CUSTOMER_CREATE.getPermission())
                                .requestMatchers(PUT, CUSTOMER_MATERIAL_PATS_ALL).hasAnyAuthority(CUSTOMER_UPDATE.getPermission())
                                .requestMatchers(DELETE, CUSTOMER_MATERIAL_PATS_ALL).hasAnyAuthority(CUSTOMER_DELETE.getPermission())
                                .requestMatchers(GET, CUSTOMER_USER_PATS_ALL).hasAnyAuthority(CUSTOMER_READ.getPermission())
                                .requestMatchers(POST, CUSTOMER_USER_PATS_ALL).hasAnyAuthority(CUSTOMER_CREATE.getPermission())
                                .requestMatchers(PUT, CUSTOMER_USER_PATS_ALL).hasAnyAuthority(CUSTOMER_UPDATE.getPermission())
                                .requestMatchers(DELETE, CUSTOMER_USER_PATS_ALL).hasAnyAuthority(CUSTOMER_DELETE.getPermission())
                                .requestMatchers(GET, CUSTOMER_COURSE_PATS_ALL).hasAnyAuthority(CUSTOMER_READ.getPermission())
                                .requestMatchers(POST, CUSTOMER_COURSE_PATS_ALL).hasAnyAuthority(CUSTOMER_CREATE.getPermission())
                                .requestMatchers(PUT, CUSTOMER_COURSE_PATS_ALL).hasAnyAuthority(CUSTOMER_UPDATE.getPermission())
                                .requestMatchers(DELETE, CUSTOMER_COURSE_PATS_ALL).hasAnyAuthority(CUSTOMER_DELETE.getPermission())
                                .requestMatchers(GET, CUSTOMER_CHAT_OPEN_AI_PATS_ALL).hasAnyAuthority(CUSTOMER_READ.getPermission())
                                .requestMatchers(POST, CUSTOMER_CHAT_OPEN_AI_PATS_ALL).hasAnyAuthority(CUSTOMER_CREATE.getPermission())
                                .requestMatchers(PUT, CUSTOMER_CHAT_OPEN_AI_PATS_ALL).hasAnyAuthority(CUSTOMER_UPDATE.getPermission())
                                .requestMatchers(DELETE, CUSTOMER_CHAT_OPEN_AI_PATS_ALL).hasAnyAuthority(CUSTOMER_DELETE.getPermission())
                                .requestMatchers(GET, ADMIN_DISCUSSION_PATS_ALL).hasAnyAuthority(ADMIN_READ.getPermission())
                                .requestMatchers(POST, ADMIN_DISCUSSION_PATS_ALL).hasAnyAuthority(ADMIN_CREATE.getPermission())
                                .requestMatchers(PUT, ADMIN_DISCUSSION_PATS_ALL).hasAnyAuthority(ADMIN_UPDATE.getPermission())
                                .requestMatchers(DELETE, ADMIN_DISCUSSION_PATS_ALL).hasAnyAuthority(ADMIN_DELETE.getPermission())
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
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .cors(Customizer.withDefaults());
        return http.build();
    }
}