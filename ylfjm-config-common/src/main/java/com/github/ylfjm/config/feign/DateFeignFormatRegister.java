package com.github.ylfjm.config.feign;

import org.springframework.cloud.openfeign.FeignFormatterRegistrar;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * feign 日期格式转换，feign 接口调用日期只需要传入yyyy-MM-dd HH:mm:ss 字符串
 */
@Configuration
public class DateFeignFormatRegister implements FeignFormatterRegistrar {

    private static final String format = "yyyy-MM-dd HH:mm:ss";

    @Override
    public void registerFormatters(FormatterRegistry registry) {
        registry.addConverter(Date.class, String.class, new Date2StringConverter());
    }

    private class Date2StringConverter implements Converter<Date, String> {
        @Override
        public String convert(Date source) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(source);
        }
    }

}