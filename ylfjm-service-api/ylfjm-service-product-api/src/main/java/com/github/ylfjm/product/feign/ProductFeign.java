package com.github.ylfjm.product.feign;

import com.github.ylfjm.common.constant.Constant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = Constant.YLFJM_SERVICE_PRODUCT)
public interface ProductFeign {

    @GetMapping("/product/deduct")
    Boolean deduct(@RequestParam("productId") Integer productId,
                   @RequestParam("count") Integer count,
                   @RequestParam("error") Integer error);

}
