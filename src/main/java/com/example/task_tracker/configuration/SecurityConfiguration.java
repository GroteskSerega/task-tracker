package com.example.task_tracker.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@EnableAspectJAutoProxy
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public ReactiveAuthenticationManager authenticationManager(ReactiveUserDetailsService userDetailsService,
                                                               PasswordEncoder passwordEncoder) {
        var reactiveAuthenticationManager =
                new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);

        reactiveAuthenticationManager.setPasswordEncoder(passwordEncoder);

        return reactiveAuthenticationManager;
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http,
                                                            ReactiveAuthenticationManager authenticationManager) {
        return buildDefaultHttpSecurity(http)
                .authenticationManager(authenticationManager)
                .build();
    }

    private ServerHttpSecurity buildDefaultHttpSecurity(ServerHttpSecurity http) {
        return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(auth -> auth
                        .pathMatchers(HttpMethod.POST,
                                "/api/v1/user")
                        .hasRole("ADMIN")
                        .pathMatchers(HttpMethod.GET,
                                "/api/v1/user")
                        .hasRole("ADMIN")
                        .pathMatchers("/api/v1/user/**")
                        .authenticated()
                        .pathMatchers(HttpMethod.POST,
                                "/api/v1/task")
                        .hasAnyRole("MODERATOR", "ADMIN")
                        .pathMatchers(HttpMethod.PUT,
                                "/api/v1/task/*")
                        .hasAnyRole("MODERATOR", "ADMIN")
                        .pathMatchers(HttpMethod.DELETE,
                                "/api/v1/task/*")
                        .hasAnyRole("MODERATOR", "ADMIN")
                        .pathMatchers("/api/v1/task/**")
                        .authenticated()
                                .anyExchange().authenticated()
                )
                .httpBasic(Customizer.withDefaults());
    }
}
