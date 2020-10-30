// package com.github.ylfjm.user.config;
//
// import com.github.ylfjm.common.constant.Constant;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.cloud.client.ServiceInstance;
// import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
// import org.springframework.http.HttpEntity;
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.HttpMethod;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.MediaType;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.core.AuthenticationException;
// import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
// import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
// import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
// import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
// import org.springframework.stereotype.Component;
// import org.springframework.util.Base64Utils;
// import org.springframework.util.LinkedMultiValueMap;
// import org.springframework.util.MultiValueMap;
// import org.springframework.web.client.RestTemplate;
//
// import javax.servlet.ServletException;
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;
// import java.io.IOException;
// import java.util.Map;
//
// @Component
// @Slf4j
// public class CustomOAuth2AuthenticationEntryPoint extends OAuth2AuthenticationEntryPoint {
//
//     @Autowired
//     private LoadBalancerClient loadBalancerClient;
//
//     private WebResponseExceptionTranslator<?> exceptionTranslator = new DefaultWebResponseExceptionTranslator();
//
//     @Override
//     public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
//         try {
//             //无效的token
//             Throwable cause = authException.getCause();
//             if (cause instanceof InvalidTokenException) {
//                 log.info("------invalid_token------");
//                 //选中认证服务的地址
//                 ServiceInstance serviceInstance = loadBalancerClient.choose(Constant.YLFJM_SERVICE_AUTH);
//                 if (serviceInstance == null) {
//                     throw new RuntimeException("找不到对应的服务");
//                 }
//                 //获取令牌的url
//                 String path = serviceInstance.getUri().toString() + "/oauth/token";
//                 String clientId = "ylfjmc";
//                 String clientSecret = "123456";
//                 String refreshToken = request.getHeader("refresh_token");
//
//                 log.info("refresh_token=" + refreshToken);
//
//                 MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
//                 formData.add("client_id", clientId);
//                 formData.add("client_secret", clientSecret);
//                 formData.add("grant_type", "refresh_token");
//                 formData.add("refresh_token", refreshToken);
//                 HttpHeaders headers = new HttpHeaders();
//                 headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//                 headers.add("Authorization", httpbasic(clientId, clientSecret));
//                 Map map = new RestTemplate().exchange(path, HttpMethod.POST, new HttpEntity<>(formData, headers), Map.class).getBody();
//                 log.info("" + map);
//                 //如果刷新异常,则坐进一步处理
//                 if (map == null || map.get("error") != null) {
//                     // 返回指定格式的错误信息
//                     response.setStatus(HttpStatus.UNAUTHORIZED.value());
//                     response.setHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
//                     response.getWriter().print("{\"code\":10002,\"message\":\"invalid_token.\"}");
//                     response.getWriter().flush();
//                     //如果是网页,跳转到登陆页面
//                     //response.sendRedirect("login");
//                 } else {
//                     //如果刷新成功则存储cookie并且跳转到原来需要访问的页面
//                     for (Object key : map.keySet()) {
//                         response.setHeader(key.toString(), map.get(key).toString());
//                     }
//                     request.getRequestDispatcher(request.getRequestURI()).forward(request, response);
//                 }
//             } else {
//                 //401未认证
//                 ResponseEntity<?> result = exceptionTranslator.translate(authException);
//                 if (result.getStatusCode() == HttpStatus.UNAUTHORIZED) {
//                     // 返回指定格式的错误信息
//                     response.setStatus(HttpStatus.UNAUTHORIZED.value());
//                     response.setHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
//                     response.getWriter().print("{\"code\":10001,\"message\":\"无权操作！\"}");
//                     response.getWriter().flush();
//                 } else {
//                     //如果不是401异常，则以默认的方法继续处理其他异常
//                     super.commence(request, response, authException);
//                 }
//             }
//
//         } catch (Exception e) {
//             e.printStackTrace();
//             super.commence(request, response, authException);
//         }
//     }
//
//     private String httpbasic(String clientId, String clientSecret) {
//         //将客户端id和客户端密码拼接，按“客户端id:客户端密码”
//         String string = clientId + ":" + clientSecret;
//         //进行base64编码
//         byte[] encode = Base64Utils.encode(string.getBytes());
//         return "Basic " + new String(encode);
//     }
// }
