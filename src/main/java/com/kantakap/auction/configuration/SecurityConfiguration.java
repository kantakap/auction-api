package com.kantakap.auction.configuration;

import com.kantakap.auction.security.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final CustomReactiveAuthenticationManager authenticationManager;
    private final CustomReactiveUserDetailsService userDetailsService;
    private final SecurityContextRepository securityContextRepository;
    private final ReactiveOAuth2AuthenticationSuccessHandler authenticationSuccessHandler;
    private final TokenProvider tokenProvider;

    @Bean
    public HttpCookieOAuth2ServerAuthorizationRequestRepository cookieAuthorizationRequestRepository() {
        return new HttpCookieOAuth2ServerAuthorizationRequestRepository();
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(tokenProvider, userDetailsService);
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .cors()
                .configurationSource(request -> corsConfiguration())
                .and()
                .csrf()
                .disable()
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .authenticationManager(authenticationManager)
                .securityContextRepository(securityContextRepository)
                .authorizeExchange()
                .pathMatchers("/graphiql")
                .permitAll()
                .pathMatchers("/graphql")
                .permitAll()
                .anyExchange()
                .permitAll()
                .and()
                .httpBasic()
                .disable()
                .oauth2Login()
                .authorizationRequestRepository(cookieAuthorizationRequestRepository())
                .authenticationSuccessHandler(authenticationSuccessHandler)
                .and()
                .formLogin()
                .disable()
                .addFilterBefore(tokenAuthenticationFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

    @Bean
    public CorsConfiguration corsConfiguration() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of(
                "https://localhost:8080",
                "http://localhost:3000"
        ));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setAllowCredentials(true);
        return corsConfiguration;
    }

}
