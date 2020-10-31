package com.github.ylfjm.config.feign;

import com.github.ylfjm.common.constant.AuthConstant;
import feign.FeignException;
import feign.Response;
import feign.Util;
import feign.codec.DecodeException;
import feign.codec.Decoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;

/**
 * 描述：TODO
 *
 * @author YLFJM
 * @Date 2020/10/23
 */
@Slf4j
public class CustomResponseDecoder implements Decoder {

    private final Decoder delegate;

    public CustomResponseDecoder(Decoder delegate) {
        this.delegate = delegate;
    }

    @Override
    public Object decode(Response response, Type type) throws IOException, DecodeException, FeignException {
        // 判断是否返回参数是否是异常
        String body = Util.toString(response.body().asReader(StandardCharsets.UTF_8));
        // 拿到返回值，进行自定义逻辑处理
        log.info("CustomResponseDecoder body->{}", body);

        Map<String, Collection<String>> headerMap = response.headers();
        if (!CollectionUtils.isEmpty(headerMap)) {
            Collection<String> headers = headerMap.get(AuthConstant.ADMIN_TOKEN);
            if (!CollectionUtils.isEmpty(headers)) {
                String token = headers.stream().findFirst().get();
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                if (attributes != null) {
                    HttpServletResponse httpServletResponse = attributes.getResponse();
                    httpServletResponse.setHeader(AuthConstant.ADMIN_TOKEN, token);
                }
            }
        }

        // 回写body,因为response的流数据只能读一次，这里回写后重新生成response
        return delegate.decode(response.toBuilder().body(body, StandardCharsets.UTF_8).build(), type);
    }

}
