package edu.batodev.windsurf.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;


/**
 * Security configuration for the application.
 * Enables web security and method-level security, and configures the application as an OAuth2 resource server.
 */
@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwkSetUri;

    /**
     * Configures the main security filter chain for the application.
     * It defines which endpoints are public and which require authentication.
     * It also configures JWT-based authentication for the resource server.
     *
     * @param http The {@link HttpSecurity} to configure.
     * @param jwtAuthenticationConverter The custom {@link JwtAuthenticationConverter} to use.
     * @return The configured {@link SecurityFilterChain}.
     * @throws Exception if an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationConverter jwtAuthenticationConverter) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/greet").permitAll()
                        .requestMatchers("/actuator/prometheus/**").permitAll()
                        .requestMatchers("/api/weather/**").authenticated()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/swagger-ui.html").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> {
                            jwt.jwkSetUri(jwkSetUri);
                            jwt.jwtAuthenticationConverter(jwtAuthenticationConverter);
                        })
                )
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

    /**
     * Creates a custom {@link JwtAuthenticationConverter} to map JWT claims to Spring Security authorities.
     * This implementation is customized to read scope-based authorities from the 'aud' (audience) claim,
     * which is a specific behavior of mock-oauth2-server.
     *
     * @return The configured {@link JwtAuthenticationConverter}.
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        // no.nav.security:mock-oauth2-server puts scopes in 'aud' claim instead of 'scp'
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("aud");
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }
}
