package com.trip.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)  // enable @PreAuthorize annotations
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @SuppressWarnings("removal")
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors()
            .and()
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Secure Trip endpoints with ADMIN role for write operations
                .requestMatchers(HttpMethod.POST, "/api/v1/trips").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/v1/trips/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/trips/**").hasAuthority("ADMIN")

                // Allow get trip by id and search endpoints for authenticated users (adjust if needed)
                .requestMatchers(HttpMethod.GET, "/api/v1/trips/**").permitAll()
                // You can restrict /search endpoint to specific roles by replacing authenticated() with hasAuthority("USER")

//                // Existing secured buses and routes endpoints
//                .requestMatchers(HttpMethod.POST, "/api/v1/buses", "/api/v1/routes").hasAuthority("ADMIN")
//                .requestMatchers(HttpMethod.PUT, "/api/v1/buses/**", "/api/v1/routes/**").hasAuthority("ADMIN")
//                .requestMatchers(HttpMethod.DELETE, "/api/v1/buses/**", "/api/v1/routes/**").hasAuthority("ADMIN")

                // Permit any other requests (adjust based on your app’s needs)
                .anyRequest().permitAll()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
