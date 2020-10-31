package com.github.ylfjm.config.feign;

import com.github.ylfjm.common.NoLoginException;
import com.github.ylfjm.common.constant.AuthConstant;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Feign请求前置拦截器
 *
 * @author YLFJM
 * @date 2018/8/21
 */
@Configuration
// @ConditionalOnProperty(prefix = "feign.request.interceptor", name = "enabled", havingValue = "true")
@Slf4j
public class FeignInterceptor implements RequestInterceptor {

    private static List<String> ignores = new ArrayList<>();

    static {
        ignores.add("/admin/login");
    }

    /**
     * 描述：除了放开权限的接口，其它接口都要将认证信息（用户）传递至被调用的服务
     *
     * @param requestTemplate requestTemplate
     **/
    @Override
    public void apply(RequestTemplate requestTemplate) {
        String url = requestTemplate.url();
        // log.info("------FeginInterceptor---url:" + url + "------");
        if (!ignores.contains(url)) {
            try {
                // Feign请求传递带上权限认证信息
                String JwtInfo = FeignRequestInterceptor.requestVariable.get();
                log.info("FeignInterceptor JwtInfo->{}", JwtInfo);
                requestTemplate.header(AuthConstant.JWTINFO, JwtInfo);
                // String jwtInfoStr = userCache.getJWTInfoStr();
                // requestTemplate.header(AuthConstants.JWTINFO, jwtInfoStr);
            } catch (NoLoginException e) {
                // log.error("Feign请求前置拦截器，未获取到jwtInfoStr信息。{}", url);
            } catch (IllegalStateException e) {
                // log.error("Feign请求前置拦截器，HystrixRequestContext.getContextForCurrentThread() == null。{}", url);
            } catch (Exception e) {
                // log.error("Feign请求前置拦截器，捕获到异常。{}", url);
            }
        }

        // // feign 不支持 GET 方法传 POJO, json body转query
        // if (Objects.equals(requestTemplate.method(), "GET") && requestTemplate.body() != null) {
        //     try {
        //         JsonNode jsonNode = objectMapper.readTree(requestTemplate.body());
        //         requestTemplate.body(null);
        //
        //         Map<String, Collection<String>> queries = new HashMap<>();
        //         buildQuery(jsonNode, "", queries);
        //         requestTemplate.queries(queries);
        //     } catch (IOException e) {
        //         e.printStackTrace();
        //     }
        // }
    }

    // private void buildQuery(JsonNode jsonNode, String path, Map<String, Collection<String>> queries) {
    //     if (!jsonNode.isContainerNode()) {   // 叶子节点
    //         if (jsonNode.isNull()) {
    //             return;
    //         }
    //         Collection<String> values = queries.get(path);
    //         if (null == values) {
    //             values = new ArrayList<>();
    //             queries.put(path, values);
    //         }
    //         values.add(jsonNode.asText());
    //         return;
    //     }
    //     if (jsonNode.isArray()) {   // 数组节点
    //         Iterator<JsonNode> it = jsonNode.elements();
    //         while (it.hasNext()) {
    //             buildQuery(it.next(), path, queries);
    //         }
    //     } else {
    //         Iterator<Map.Entry<String, JsonNode>> it = jsonNode.fields();
    //         while (it.hasNext()) {
    //             Map.Entry<String, JsonNode> entry = it.next();
    //             if (StringUtils.hasText(path)) {
    //                 buildQuery(entry.getValue(), path + "." + entry.getKey(), queries);
    //             } else {  // 根节点
    //                 buildQuery(entry.getValue(), entry.getKey(), queries);
    //             }
    //         }
    //     }
    // }

}