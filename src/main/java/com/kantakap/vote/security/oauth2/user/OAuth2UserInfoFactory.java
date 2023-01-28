package com.kantakap.vote.security.oauth2.user;

import org.springframework.security.oauth2.core.user.OAuth2User;
import reactor.core.publisher.Mono;

public class OAuth2UserInfoFactory {

    public static Mono<OAuth2UserInfo> getOAuth2UserInfo(String registrationId, Mono<OAuth2User> oAuth2UserMono) {
        return oAuth2UserMono.map(oAuth2User -> new OsuOAuth2UserInfo(oAuth2User.getAttributes()));
    }
}
