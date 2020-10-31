package com.github.ylfjm.system.web;

import com.github.ylfjm.user.feign.UserFeign;
import lombok.RequiredArgsConstructor;
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
public class SystemWebConfig implements WebMvcConfigurer {

    /**
     * 添加权限拦截器配置
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(permissionInterceptor())
                .order(Ordered.HIGHEST_PRECEDENCE + 1)
                .addPathPatterns("/**")
                .excludePathPatterns("/error", "/actuator/**", "/monitor/**");
    }

    @Bean
    public PermissionInterceptor permissionInterceptor() {
        return new PermissionInterceptor();
    }
}
