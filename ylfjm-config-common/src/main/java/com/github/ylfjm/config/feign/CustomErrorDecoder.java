package com.github.ylfjm.config.feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ylfjm.common.YlfjmFeignException;
import feign.FeignException;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 描述:远程异常会被Feign Hystrix拦截，HystrixBadRequestException不会被Feign Hystrix拦截
 * <b/>
 * 将业务异常包装成HystrixBadRequestException类型异常，使其能被全局异常处理器捕获。
 *
 * @author YLFJM
 * @Date 2019/1/10
 */
@Configuration
@Slf4j
public class CustomErrorDecoder implements ErrorDecoder {

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            String body = Util.toString(response.body().asReader(StandardCharsets.UTF_8));
            int status = response.status();
            if (400 <= status && status <= 500) {
                log.error("CustomErrorDecoder status->{}, body->{}", status, body);
                Map<String, Object> map = objectMapper.readValue(body, Map.class);
                return new YlfjmFeignException(String.valueOf(map.get("message")));
            }
            return FeignException.errorStatus(methodKey, response);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return new YlfjmFeignException("Feign远程异常解析错误。");
        }
    }

}