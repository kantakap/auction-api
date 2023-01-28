package com.kantakap.vote.configuration;

import com.kantakap.vote.security.CustomReactiveAuthenticationManager;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.rsocket.server.RSocketServerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.rsocket.EnableRSocketSecurity;
import org.springframework.security.config.annotation.rsocket.RSocketSecurity;
import org.springframework.security.rsocket.core.PayloadSocketAcceptorInterceptor;
import org.springframework.security.rsocket.core.SecuritySocketAcceptorInterceptor;
import org.springframework.web.reactive.socket.WebSocketHandler;

@Configuration
@EnableRSocketSecurity
@EnableReactiveMethodSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class RSocketSecurityConfiguration {
    private final RSocketMessageHandler rsocketMessageHandler;
    private final RSocketSecurity rsocketSecurity;
    private final WebSocketHandler customSubscriptionHandler;
    private final CustomReactiveAuthenticationManager authenticationManager;

    @Bean
    public RSocketServerCustomizer rSocketServerCustomizer(SecuritySocketAcceptorInterceptor interceptor) {
        return (server) -> server.interceptors((registry) -> registry.forSocketAcceptor(interceptor));
    }

    @Bean
    public PayloadSocketAcceptorInterceptor rsocketInterceptor(RSocketSecurity rsocket) {
        rsocket
                .authenticationManager(authenticationManager)
                .jwt(jwtSpec -> jwtSpec.authenticationManager(authenticationManager))
                .authorizePayload((authorize) ->
                        authorize
                                // must have  to make connection
                                .setup().hasRole("ROLE_USER")
                                .anyRequest().authenticated()
                );

        return rsocket.build();
    }

}
