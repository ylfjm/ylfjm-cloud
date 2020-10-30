package com.github.ylfjm.order.feign;

import com.github.ylfjm.common.constant.Constant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = Constant.YLFJM_SERVICE_ORDER)
public interface OrderFeign {

    @GetMapping(value = "/order/create")
    Boolean create(@RequestParam("userId") Integer userId,
                   @RequestParam("productId") Integer productId,
                   @RequestParam("count") Integer count);

}
