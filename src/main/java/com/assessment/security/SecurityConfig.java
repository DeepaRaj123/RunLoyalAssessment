package com.assessment.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import java.util.List;

/**
 * Security Configuration Class
 * Configures authentication, authorization, and security filters for the application.
 */
@Configuration
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final CustomUserDetailsService userDetailsService;

    /**
     * Constructor to inject JwtAuthFilter and CustomUserDetailsService dependencies.
     *
     * @param jwtAuthFilter The JWT authentication filter.
     * @param userDetailsService The service to load user details.
     */
    public SecurityConfig(JwtAuthFilter jwtAuthFilter, CustomUserDetailsService userDetailsService) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Configures HTTP security for the application.
     * 
     * - Disables CSRF for stateless API security.
     * - Defines role-based access to endpoints.
     * - Configures session management as stateless.
     * - Adds JWT authentication filter before UsernamePasswordAuthenticationFilter.
     *
     * @param http The HttpSecurity object.
     * @return The configured SecurityFilterChain.
     * @throws Exception If any security configuration fails.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF for REST APIs
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll() // Allow Swagger UI
                .requestMatchers("/api/auth/signup", "/api/auth/signin").permitAll() // Allow Signup & Signin without authentication
                .requestMatchers("/api/users").hasRole("ADMIN") // Only Admins can access user listing
                .requestMatchers("/api/user/update").authenticated() // Any authenticated user can update their profile
                .anyRequest().authenticated() // Require authentication for all other requests
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Use stateless session
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // Add JWT filter before the default authentication filter

        return http.build();
    }

    /**
     * Configures the authentication manager.
     * 
     * - Uses DaoAuthenticationProvider for authentication.
     * - Sets the password encoder.
     *
     * @return The configured AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(List.of(provider));
    }

    /**
     * Configures the password encoder.
     * 
     * - Uses BCryptPasswordEncoder for secure password hashing.
     *
     * @return The configured PasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
