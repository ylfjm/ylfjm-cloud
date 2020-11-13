package com.github.ylfjm.user.web;

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
 * @date 2020/10/24
 */
@Configuration
public class UserWebConfig implements WebMvcConfigurer {

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

    @Bean
    public FeignRequestInterceptor feignRequestInterceptor() {
        return new FeignRequestInterceptor();
    }

}
