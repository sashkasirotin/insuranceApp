package com.insurance.insuranceApp.services.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.insurance.insuranceApp.dto.ApiError;
import com.insurance.insuranceApp.services.security.filter.implementation.JwtAuthenticationFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    private static final String[] WHITE_LIST_URL = {
            "/api/v1/auth/**",
            "/v2/api-docs", "/v3/api-docs", "/v3/api-docs/**",
            "/swagger-resources", "/swagger-resources/**",
            "/configuration/ui", "/configuration/security",
            "/swagger-ui/**", "/webjars/**", "/swagger-ui.html",
            "/api/auth/**", "/api/test/**", "/authentication",
            "/api/v1/auth/newUser", "/api/v1/auth/authenticate", "/swagger-ui/index.html**"
    };

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    /**
     * Custom Authentication Entry Point for 401 Unauthorized
     * Handles missing or invalid JWT tokens
     */
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new AuthenticationEntryPoint() {
            @Override
            public void commence(
                    HttpServletRequest request,
                    HttpServletResponse response,
                    AuthenticationException authException) throws IOException, ServletException {

                log.warn("Unauthorized access attempt: {} - {}",
                        request.getServletPath(), authException.getMessage());

                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

                ApiError error = new ApiError(
                        "AUTHENTICATION_FAILED",
                        authException.getMessage() != null ?
                                authException.getMessage() :
                                "Authentication failed: Invalid or missing JWT token"
                );

                try {
                    ObjectMapper mapper = new ObjectMapper();
                    response.getWriter().write(mapper.writeValueAsString(error));
                } catch (Exception e) {
                    log.error("Error writing error response", e);
                    response.getWriter().write("{\"code\":\"AUTHENTICATION_FAILED\",\"message\":\"Authentication failed\"}");
                }
            }
        };
    }

    /**
     * Custom Access Denied Handler for 403 Forbidden
     * Handles access control violations (user tries to access restricted resource)
     */
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new AccessDeniedHandler() {
            @Override
            public void handle(
                    HttpServletRequest request,
                    HttpServletResponse response,
                    org.springframework.security.access.AccessDeniedException accessDeniedException) throws IOException, ServletException {

                log.warn("Access denied: {} - {}",
                        request.getServletPath(), accessDeniedException.getMessage());

                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);

                ApiError error = new ApiError(
                        "ACCESS_DENIED",
                        accessDeniedException.getMessage() != null ?
                                accessDeniedException.getMessage() :
                                "Access denied: You do not have permission to access this resource"
                );

                try {
                    ObjectMapper mapper = new ObjectMapper();
                    response.getWriter().write(mapper.writeValueAsString(error));
                } catch (Exception e) {
                    log.error("Error writing error response", e);
                    response.getWriter().write("{\"code\":\"ACCESS_DENIED\",\"message\":\"Access denied\"}");
                }
            }
        };
    }

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            JwtAuthenticationFilter jwtFilter
    ) throws Exception {

        http
                // Disable CSRF for REST API
                .csrf(csrf -> csrf.disable())

                // ⭐ CRITICAL: Configure exception handling - THIS WAS MISSING!
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(authenticationEntryPoint())  // 401 Unauthorized
                        .accessDeniedHandler(accessDeniedHandler())           // 403 Forbidden
                )

                // Configure authorization rules
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(WHITE_LIST_URL).permitAll()
                        .anyRequest().authenticated()
                )

                // Stateless sessions
                .sessionManagement(sess ->
                        sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Add JWT filter
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}