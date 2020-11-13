package com.github.ylfjm.system.web;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.ylfjm.config.feign.FeignRequestInterceptor;
import com.github.ylfjm.config.web.CustomDateFormat;
import com.github.ylfjm.config.web.String2DateConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * web相关配置信息
 *
 * @author YLFJM
 * @date 2020/10/24
 */
@Configuration
public class SystemWebConfig extends WebMvcConfigurationSupport {
// public class SystemWebConfig implements WebMvcConfigurer {

    /**
     * 添加权限拦截器配置
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(feignRequestInterceptor())
                .order(Ordered.HIGHEST_PRECEDENCE)
                .addPathPatterns("/**")
                .excludePathPatterns("/error", "/actuator/**", "/monitor/**");
        registry.addInterceptor(permissionInterceptor())
                .order(Ordered.HIGHEST_PRECEDENCE + 1)
                .addPathPatterns("/**")
                .excludePathPatterns("/error", "/actuator/**", "/monitor/**");
    }

    /**
     * 解决spring.jackson.date-format, json方式全局日期序列化失效
     *
     * @param converters
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        //反序列化日期格式 String2Date
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Date.class, new JsonDeserializer<Date>() {
            @Override
            public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
                return String2DateConverter.parse(jsonParser.getText());
            }
        });
        objectMapper.registerModule(module);

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        //序列化日期格式 Date2String
        objectMapper.setDateFormat(CustomDateFormat.instance);
        converter.setObjectMapper(objectMapper);
        converters.add(converter);
    }

    @Bean
    public PermissionInterceptor permissionInterceptor() {
        return new PermissionInterceptor();
    }

    @Bean
    public FeignRequestInterceptor feignRequestInterceptor() {
        return new FeignRequestInterceptor();
    }

    // @Bean
    // public ObjectMapper objectMapper() {
    //     ObjectMapper objectMapper = new ObjectMapper();
    //     //反序列化扩展日期格式支持 String2Date
    //     objectMapper.setConfig(objectMapper.getDeserializationConfig().with(CustomDateFormat.instance));
    //     return objectMapper;
    // }

}
