package com.github.ylfjm.gateway.admin.configuration;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;
import java.util.List;

@Configuration
public class GatewayConfiguration {

    // private static final String ALL = "*";

    private static final String MAX_AGE = "18000L";

    //这里为支持的请求头，如果有自定义的header字段请自己添加（不知道为什么不能使用*）
    private static final String ALLOWED_HEADERS = "x-requested-with,Authorization,authorization,Content-Type,content-type,credential,X-XSRF-TOKEN,token,client,access_token,admin_token,accessKey";

    /**
     * 跨域配置
     *
     * @return WebFilter
     */
    @Bean
    public WebFilter corsFilter() {
        return (ctx, chain) -> {
            ServerHttpRequest request = ctx.getRequest();
            if (CorsUtils.isCorsRequest(request)) {
                HttpHeaders requestHeaders = request.getHeaders();
                HttpMethod requestMethod = requestHeaders.getAccessControlRequestMethod();

                ServerHttpResponse response = ctx.getResponse();
                HttpHeaders headers = response.getHeaders();
                headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, requestHeaders.getOrigin());
                List<String> allowHeadersList = requestHeaders.getAccessControlRequestHeaders();
                headers.addAll(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, allowHeadersList);
                if (requestMethod != null) {
                    headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, requestMethod.name());
                }
                headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
                headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, ALLOWED_HEADERS);
                headers.add(HttpHeaders.ACCESS_CONTROL_MAX_AGE, MAX_AGE);
                if (request.getMethod() == HttpMethod.OPTIONS) {
                    response.setStatusCode(HttpStatus.OK);
                    return Mono.empty();
                }
            }
            return chain.filter(ctx);
        };
    }

    /**
     * 自定义限流标志的key，多个维度可以从这里入手
     * exchange对象中获取服务ID、请求信息，用户信息等
     */
    @Bean
    public KeyResolver remoteAddrKeyResolver() {
        return exchange -> {
            InetSocketAddress remoteAddress = exchange.getRequest().getRemoteAddress();
            return Mono.just(remoteAddress == null ? "127.0.0.1" : remoteAddress.getHostName());
        };
    }

}