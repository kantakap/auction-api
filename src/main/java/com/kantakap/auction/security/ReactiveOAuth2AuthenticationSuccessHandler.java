package com.kantakap.auction.security;

import com.kantakap.auction.properties.AuthProperties;
import com.kantakap.auction.utils.CookieUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReactiveOAuth2AuthenticationSuccessHandler extends RedirectServerAuthenticationSuccessHandler {
    private final TokenProvider tokenProvider;
    private final AuthProperties authProperties;
    private final HttpCookieOAuth2ServerAuthorizationRequestRepository httpCookieOAuth2ServerAuthorizationRequestRepository;

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        var determinedTargetUrl = determineTargetUrl(webFilterExchange.getExchange().getRequest(), webFilterExchange.getExchange().getResponse(), authentication);
        log.info("Determined target URL: " + determinedTargetUrl);
        super.setLocation(determinedTargetUrl);

        clearAuthenticationAttributes(webFilterExchange.getExchange().getRequest(), webFilterExchange.getExchange().getResponse());
        return super.onAuthenticationSuccess(webFilterExchange, authentication);
    }

    protected URI determineTargetUrl(ServerHttpRequest request, ServerHttpResponse response, Authentication authentication) {
        Optional<String> redirectUri = CookieUtils.getCookie(request, HttpCookieOAuth2ServerAuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(HttpCookie::getValue);

        if(redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new RuntimeException("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication");
        }

        String targetUrl = redirectUri.orElse("/");

        String token = tokenProvider.generate(authentication);

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("token", token)
                .build()
                .toUri();
    }

    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);

        return authProperties.getOauth2().getAuthorizedRedirectUris()
                .stream()
                .anyMatch(authorizedRedirectUri -> {
                    // Only validate host and port. Let the clients use different paths if they want to
                    URI authorizedURI = URI.create(authorizedRedirectUri);
                    return authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                            && authorizedURI.getPort() == clientRedirectUri.getPort();
                });
    }

    protected void clearAuthenticationAttributes(ServerHttpRequest request, ServerHttpResponse response) {
        httpCookieOAuth2ServerAuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }


//    @Override
//    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
//        var token = tokenProvider.generate(authentication);
//        this.setRedirectStrategy((exchange, url) ->
//                Mono.justOrEmpty(exchange.getResponse().setStatusCode(HttpStatusCode.valueOf(302)))
//                        .then(Mono.defer(() -> {
//                            exchange.getResponse()
//                                    .getHeaders()
//                                    .setLocation(UriComponentsBuilder.fromUriString("http://localhost:8080")
//                                            .queryParam("token", token)
//                                            .build()
//                                            .toUri());
////                            exchange.getResponse()
////                                    .getCookies()
////                                    .add("access_token",
////                                            ResponseCookie.from("access_token", token)
////                                                    .httpOnly(true)
////                                                    .path("/")
////                                                    .secure(true)
////                                                    .maxAge(30)
////                                                    .domain("localhost")
////                                                    .sameSite("Strict")
////                                                    .build()
////                                    );
//                            return exchange.getResponse().setComplete();
//                        })));
//        return super.onAuthenticationSuccess(webFilterExchange, authentication);
//    }

}
