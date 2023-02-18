package com.kantakap.vote.configuration;

import com.kantakap.vote.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.graphql.server.WebSocketGraphQlInterceptor;
import org.springframework.graphql.server.WebSocketSessionInfo;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class WebSocketConfiguration implements WebSocketGraphQlInterceptor {
    private final TokenProvider tokenProvider;

    @Override
    public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, Chain chain) {
        log.info("Intercepting request {}", request);
        return WebSocketGraphQlInterceptor.super.intercept(request, chain);
    }

    @Override
    public Mono<Object> handleConnectionInitialization(WebSocketSessionInfo sessionInfo, Map<String, Object> connectionInitPayload) {
        log.info("Intercepting connection initialization {}", connectionInitPayload);
        if (connectionInitPayload.get("Authorization") == null) {
            return WebSocketGraphQlInterceptor.super.handleConnectionInitialization(sessionInfo, connectionInitPayload);
        }
        var token = connectionInitPayload.get("Authorization").toString().substring(7);
        log.info("Token: {}", token);
        var username = tokenProvider.getUsernameFromToken(token);
        var claims = tokenProvider.getClaimsFromToken(token);
        List<String> rolesMap = claims.get("role", List.class);
        Authentication auth = new UsernamePasswordAuthenticationToken(
                username,
                null,
                rolesMap.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
        return WebSocketGraphQlInterceptor.super.handleConnectionInitialization(sessionInfo, connectionInitPayload);
    }
}
