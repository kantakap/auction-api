//package com.kantakap.bot.security;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.ReactiveSecurityContextHolder;
//import org.springframework.util.StringUtils;
//import org.springframework.web.server.ServerWebExchange;
//import org.springframework.web.server.WebFilter;
//import org.springframework.web.server.WebFilterChain;
//import reactor.core.publisher.Mono;
//
//@RequiredArgsConstructor
//@Slf4j
//public class TokenAuthenticationFilter implements WebFilter {
//    @Autowired
//    private TokenProvider tokenProvider;
//    @Autowired
//    private CustomReactiveUserDetailsService detailsService;
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
//        return Mono.defer(() -> {
//            try {
//                String jwt = getJwtFromRequest(exchange.getRequest());
//                if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
//                    String username = tokenProvider.getUsernameFromToken(jwt);
//                    return detailsService.findByUsername(username)
//                            .doOnNext(ud -> {
//                                if (ud == null) {
//                                    return;
//                                }
//                                var authentication = new UsernamePasswordAuthenticationToken(
//                                        ud,
//                                        ud,
//                                        ud.getAuthorities()
//                                );
//                                authentication.setDetails(exchange.getRequest());
////                                // set authentication to ReactiveSecurityContextHolder
////                                ReactiveSecurityContextHolder.getContext()
////                                        .doOnNext(ctx -> ctx.setAuthentication(authentication))
////                                        .subscribe();
//                            })
//                            .then(chain.filter(exchange));
//                }
//            } catch (Exception ex) {
//                log.error("Could not set user authentication in security context", ex);
//            }
//            return chain.filter(exchange);
//        });
//    }
//
//    private String getJwtFromRequest(ServerHttpRequest request) {
//        var authorizationHeader = request.getHeaders().get("Authorization");
//        if (authorizationHeader == null || authorizationHeader.isEmpty()) {
//            return null;
//        }
//        var bearerToken = authorizationHeader.get(0);
//        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
//            return bearerToken.substring(7);
//        }
//        return null;
//    }
//}
