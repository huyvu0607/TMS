package com.example.backend.config;

import com.example.backend.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

/**
 * Configuration cho Spring Security
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsConfigurationSource corsConfigurationSource;
    private final CustomUserDetailsService customUserDetailsService;

    /**
     * Cấu hình Security Filter Chain
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF vì dùng JWT (stateless)
                .csrf(AbstractHttpConfigurer::disable)

                // Enable CORS - dùng bean từ CorsConfig
                .cors(cors -> cors.configurationSource(corsConfigurationSource))

                // Cấu hình authorization
                .authorizeHttpRequests(auth -> auth
                        // ========== PUBLIC ENDPOINTS (Không cần token) ==========
                        .requestMatchers(
                                "/api/auth/**"
                        ).permitAll()

                        // Swagger/OpenAPI docs
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        // ========== PROTECTED ENDPOINTS (Cần authentication) ==========
                        // Dashboard endpoints - chỉ authenticated users mới access được
                        .requestMatchers("/api/dashboard/**").authenticated()

                        // User profile endpoints
                        .requestMatchers("/api/auth/me", "/api/auth/logout", "/api/auth/validate").authenticated()

                        // Tất cả các request khác đều cần authentication
                        .anyRequest().authenticated()
                )



                // Stateless session (không lưu session)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Cấu hình Authentication Provider
                .authenticationProvider(authenticationProvider());

        return http.build();
    }

    /**
     * Password Encoder - BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Authentication Provider
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Authentication Manager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}