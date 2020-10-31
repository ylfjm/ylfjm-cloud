package com.github.ylfjm.config.feign;

import com.github.ylfjm.common.constant.AuthConstant;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariableDefault;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 解决Hystrix多线程模式request header参数传递
 *
 * @author YLFJM
 * @date 2018/8/8
 */
@Slf4j
public class FeignRequestInterceptor extends HandlerInterceptorAdapter {

    public static final HystrixRequestVariableDefault<String> requestVariable = new HystrixRequestVariableDefault<>();

    public static void initHystrixRequestContext(String jwtStr) {
        if (!HystrixRequestContext.isCurrentThreadInitialized()) {
            HystrixRequestContext.initializeContext();
        }
        FeignRequestInterceptor.requestVariable.set(jwtStr);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String jwtStr = request.getHeader(AuthConstant.JWTINFO);
        // log.info("FeignRequestInterceptor url: " + request.getRequestURI() + ", JWTINFO: " + jwtStr);
        FeignRequestInterceptor.initHystrixRequestContext(jwtStr);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        FeignRequestInterceptor.shutdownHystrixRequestContext();
    }

    private static void shutdownHystrixRequestContext() {
        if (HystrixRequestContext.isCurrentThreadInitialized()) {
            HystrixRequestContext.getContextForCurrentThread().shutdown();
        }
    }

}
