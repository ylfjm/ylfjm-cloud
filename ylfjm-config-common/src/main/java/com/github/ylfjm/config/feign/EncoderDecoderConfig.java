package com.github.ylfjm.config.feign;

import feign.codec.Decoder;
import feign.codec.Encoder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 描述：针对feign文件上传的配置
 *
 * @author YLFJM
 * @Date 2019/1/26
 */
@Configuration
@RequiredArgsConstructor
public class EncoderDecoderConfig {

    private final ObjectFactory<HttpMessageConverters> messageConverters;

    @Bean
    public Encoder feignFormEncoder() {
        return new MultipartFormEncoder(new SpringEncoder(messageConverters));
    }

    @Bean
    public Decoder feignResponseDecoder() {
        return new CustomResponseDecoder(new SpringDecoder(this.messageConverters));
    }

}
