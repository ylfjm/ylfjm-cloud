package com.github.ylfjm.gateway.admin.filter;

import com.github.ylfjm.common.JwtTokenException;
import com.github.ylfjm.common.constant.AuthConstant;
import com.github.ylfjm.common.jwt.JwtHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * 网关权限过滤器
 */
@Configuration
@Slf4j
public class AuthFilter implements GlobalFilter {

    private static List<String> ignores = new ArrayList<>();

    static {
        ignores.add("/api/system/admin/login");
    }

    /**
     * 权限过滤器，针对需要进行权限限制的接口进行权限控制
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        ServerHttpRequest.Builder mutate = request.mutate();

        String path = this.getRequestPath(exchange, request);
        log.info("Gateway AuthFilter start...path=" + path);

        ServerHttpRequest newRequest = mutate.build();
        //放开被忽略的请求（配置在配置文件中），不做权限验证
        if (!CollectionUtils.isEmpty(ignores)) {
            for (String prefix : ignores) {
                if (path.startsWith(prefix)) {
                    return chain.filter(exchange.mutate().request(newRequest).response(response).build());
                }
            }
        }

        // 3.用户权限校验
        String token = this.getToken(request);
        log.info("token:" + token);
        try {
            String newToken = JwtHelper.getGoodToken(token);
            if (StringUtils.hasText(newToken)) {
                //将重新颁发的jwt token设置到响应header里
                HttpHeaders headers = response.getHeaders();
                headers.add(AuthConstant.ADMIN_TOKEN, newToken);
            } else {
                newToken = token;
            }
            // 将认证token设置到请求头传递到后续服务
            mutate.header(AuthConstant.JWTINFO, URLEncoder.encode(JwtHelper.getJWTInfoFromToken(newToken), "UTF-8"));
            newRequest = mutate.build();
            return chain.filter(exchange.mutate().request(newRequest).response(response).build());
        } catch (Exception e) {
            if (e instanceof JwtTokenException) {
                log.error("------JwtTokenException------" + e.getMessage());
                return getVoidMono(exchange, e.getMessage());
            } else {
                log.error("------其它Exception------" + e.getMessage());
                return getVoidMono(exchange, "操作失败【code=50001】");
            }
        } finally {
            log.info("Gateway AuthFilter end...");
        }
    }

    /**
     * 获取token
     * 从请求信息中获取用户认证token
     */
    private String getToken(ServerHttpRequest request) {
        // 从请求header中获取用户权限认证信息
        String token = request.getHeaders().getFirst(AuthConstant.ADMIN_TOKEN);
        // 从Cookie中获取用户权限认证信息
        if (!StringUtils.hasText(token)) {
            HttpCookie httpCookie = request.getCookies().getFirst(AuthConstant.ADMIN_TOKEN);
            token = httpCookie == null ? null : httpCookie.getValue();
        }
        // 从请求参数中获取用户权限认证信息
        if (!StringUtils.hasText(token)) {
            return request.getQueryParams().getFirst(AuthConstant.ADMIN_TOKEN);
        }
        return token;
    }

    /**
     * 获取完整的请求url
     */
    private String getRequestPath(ServerWebExchange exchange, ServerHttpRequest request) {
        // Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        // 获取请求uri
        LinkedHashSet<URI> requiredAttribute = exchange.getRequiredAttribute(ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR);
        Iterator<URI> iterator = requiredAttribute.iterator();
        String path = request.getPath().pathWithinApplication().value();
        while (iterator.hasNext()) {
            URI uri = iterator.next();
            return uri.getPath();
        }
        return path;
    }

    /**
     * 用户权限认证失败，返回无权操作
     */
    private Mono<Void> getVoidMono(ServerWebExchange exchange, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON_UTF8);
        byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        return response.writeWith(Mono.just(buffer));
    }

}
