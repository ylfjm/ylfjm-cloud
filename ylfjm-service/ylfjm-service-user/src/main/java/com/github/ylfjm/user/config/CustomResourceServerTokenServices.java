// package com.github.ylfjm.user.config;
//
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.security.core.AuthenticationException;
// import org.springframework.security.oauth2.common.OAuth2AccessToken;
// import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
// import org.springframework.security.oauth2.provider.OAuth2Authentication;
// import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
// import org.springframework.security.oauth2.provider.token.TokenStore;
// import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
// import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
//
// @Slf4j
// public class CustomResourceServerTokenServices implements ResourceServerTokenServices {
//
//     private final TokenStore tokenStore;
//
//     public CustomResourceServerTokenServices(JwtAccessTokenConverter jwtAccessTokenConverter) {
//         this.tokenStore = new JwtTokenStore(jwtAccessTokenConverter);
//     }
//
//     /**
//      * 用于从 accessToken 中加载凭证信息, 并构建出 {@link OAuth2Authentication} 的方法。
//      *
//      * @see ResourceServerTokenServices#loadAuthentication(String)
//      */
//     @Override
//     public OAuth2Authentication loadAuthentication(String accessToken) throws AuthenticationException, InvalidTokenException {
//         log.info("CustomResourceServerTokenServices :: loadAuthentication called ...");
//         log.info("CustomResourceServerTokenServices :: loadAuthentication :: accessToken: {}", accessToken);
//
//         return tokenStore.readAuthentication(accessToken);
//     }
//
//     @Override
//     public OAuth2AccessToken readAccessToken(String accessToken) {
//         log.debug("CustomResourceServerTokenServices :: readAccessToken called ...");
//
//         throw new UnsupportedOperationException("暂不支持 readAccessToken!");
//     }
//
// }