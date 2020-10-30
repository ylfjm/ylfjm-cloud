// package com.github.ylfjm.user.config;
//
// import com.github.ylfjm.common.constant.Constant;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.core.io.ClassPathResource;
// import org.springframework.core.io.Resource;
// import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.jwt.crypto.sign.RsaVerifier;
// import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
// import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
// import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
// import org.springframework.security.oauth2.provider.token.TokenStore;
// import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
// import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
//
// import java.io.BufferedReader;
// import java.io.IOException;
// import java.io.InputStreamReader;
// import java.util.stream.Collectors;
//
// @Configuration
// @EnableResourceServer
// @EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)//激活方法上的PreAuthorize注解
// public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
//
//     // @Autowired
//     // private JwtAccessTokenConverter jwtAccessTokenConverter;
//     @Autowired
//     private CustomOAuth2AuthenticationEntryPoint customOAuth2AuthenticationEntryPoint;
//
//     @Override
//     public void configure(ResourceServerSecurityConfigurer resources) {
//         //当资源请求发送到Resource Server的时候会携带access_token，Resource Server会根据access_token找到client_id，进而找到该client可以访问的resource_ids。
//         //如果client对应的resource_ids包含了ResourceServer设置ResourceID，那ResourceID校验这关就过去了，就可以继续进行其他的权限验证。
//         resources.resourceId(Constant.YLFJM_SERVICE_USER).stateless(true);
//
//         //指定 ResourceServerTokenServices
//         //资源服务器端自定义access_token校验逻辑
//         // resources.tokenServices(new CustomResourceServerTokenServices(jwtAccessTokenConverter));
//
//         //自定义异常处理，实现access_token失效后自动使用refresh_token刷新token
//         resources.authenticationEntryPoint(customOAuth2AuthenticationEntryPoint);
//     }
//
//     @Override
//     public void configure(HttpSecurity http) throws Exception {
//         //放行 用户注册的请求
//         //其他的请求，必须有登录之后才能访问 (校验token合法才可以访问)
//         http
//                 .csrf().disable()
//                 .authorizeRequests()
//                 //"/user/login"请求放行
//                 // .antMatchers("/user/login").permitAll()
//                 //其它请求需要认证授权
//                 .anyRequest().authenticated();
//     }
//
//     @Bean
//     public TokenStore tokenStore() {
//         return new JwtTokenStore(jwtAccessTokenConverter());
//     }
//
//     @Bean
//     public JwtAccessTokenConverter jwtAccessTokenConverter() {
//         JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
//         // converter.setVerifierKey(getPubKey());
//         converter.setVerifier(new RsaVerifier(getPubKey()));
//         return converter;
//     }
//
//     private String getPubKey() {
//         Resource resource = new ClassPathResource("public.key");
//         try {
//             InputStreamReader inputStreamReader = new InputStreamReader(resource.getInputStream());
//             BufferedReader br = new BufferedReader(inputStreamReader);
//             return br.lines().collect(Collectors.joining("\n"));
//         } catch (IOException ioe) {
//             return "";
//         }
//     }
// }