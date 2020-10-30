// package com.github.ylfjm.eureka.configuration;
//
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//
// /**
//  * @author ylfjm
//  * @date 2018/6/30
//  */
// @Configuration
// public class EurekaConfiguration {
//
//     /**
//      * Spring boot 2.0 Spring Security配置
//      */
//     @EnableWebSecurity
//     public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//         @Override
//         protected void configure(HttpSecurity http) throws Exception {
//             //Spring Security默认开启了所有CSRF攻击防御，禁用CSRF防御。使用basic auth认证方式
//             http.authorizeRequests().anyRequest().authenticated()
//                     .and().httpBasic()
//                     .and().csrf().disable();
//         }
//     }
//
// }
