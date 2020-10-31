package com.github.ylfjm.config.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Spring MVC String转Date类型全局转换器
 *
 * @author YLFJM
 * @date 2018/11/27
 */
@Configuration
public class DateConvertConfig implements Converter<String, Date> {

    private static final String format1 = "yyyy-MM";
    private static final String format2 = "yyyy-MM-dd";
    private static final String format3 = "yyyy-MM-dd HH:mm";
    private static final String format4 = "yyyy-MM-dd HH:mm:ss";

    /**
     * Spring MVC String转Date类型全局转换器
     *
     * @param source 接受到的字符串类型日期
     */
    @Override
    public Date convert(String source) {
        String value = source.trim();
        if ("".equals(value)) {
            return null;
        }
        if (source.matches("^\\d{4}-\\d{1,2}$")) {
            return parseDate(source, format1);
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")) {
            return parseDate(source, format2);
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}$")) {
            return parseDate(source, format3);
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
            return parseDate(source, format4);
        } else {
            throw new IllegalArgumentException("Invalid boolean value '" + source + "'");
        }
    }

    /**
     * @param dateStr 字符类型的日期
     * @param pattern 转换格式
     */
    private Date parseDate(String source, String pattern) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            return format.parse(source);
        } catch (Exception e) {

        }
        return null;
    }

}
