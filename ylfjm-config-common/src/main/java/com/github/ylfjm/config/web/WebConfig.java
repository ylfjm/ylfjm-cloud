package com.github.ylfjm.config.web;

import com.github.ylfjm.config.feign.FeignRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * web相关配置信息
 *
 * @author YLFJM
 * @date 2018/6/30
 */
@Configuration
public class WebConfig {

    /**
     * WebMvc相关配置
     *
     * @return WebMvcConfigurer
     */
    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            // /**
            //  * 允许跨域
            //  */
            // @Override
            // public void addCorsMappings(CorsRegistry registry) {
            //     registry.addMapping("/**").allowedOrigins("*").allowedHeaders("*").allowedMethods("*").allowCredentials(true);
            // }

            /**
             * 添加权限拦截器配置
             */
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(feignRequestInterceptor())
                        .order(Ordered.HIGHEST_PRECEDENCE)
                        .addPathPatterns("/**")
                        .excludePathPatterns("/error", "/actuator/**", "/monitor/**");
            }
        };
    }

    @Bean
    public FeignRequestInterceptor feignRequestInterceptor() {
        return new FeignRequestInterceptor();
    }

}
