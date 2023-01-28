package com.kantakap.vote.security.oauth2;

import com.kantakap.vote.model.User;
import com.kantakap.vote.security.domain.UserPrincipal;
import com.kantakap.vote.security.oauth2.user.OAuth2UserInfoFactory;
import com.kantakap.vote.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultReactiveOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomReactiveOAuth2UserService extends DefaultReactiveOAuth2UserService {
    private final UserService userService;
    @Override
    public Mono<OAuth2User> loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        var oAuth2UserMono = super.loadUser(userRequest);
        return processOAuth2User(userRequest, oAuth2UserMono);
    }

    private Mono<OAuth2User> processOAuth2User(OAuth2UserRequest userRequest, Mono<OAuth2User> oAuth2User) {
        var oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(userRequest.getClientRegistration().getRegistrationId(), oAuth2User);
        return oAuth2UserInfo
                .flatMap(userInfo -> userService.findByUsername(userInfo.getUsername()))
                .switchIfEmpty(oAuth2UserInfo.flatMap(userInfo -> {
                    var user = new User(userInfo.getUsername());
                    return userService.save(user);
                }))
                .map(user -> UserPrincipal.create(user, getOAuth2UserAttributes(oAuth2User)));
    }

    private Map<String, Object> getOAuth2UserAttributes(Mono<OAuth2User> oAuth2User) {
        return oAuth2User.map(OAuth2User::getAttributes).block();
    }
}
