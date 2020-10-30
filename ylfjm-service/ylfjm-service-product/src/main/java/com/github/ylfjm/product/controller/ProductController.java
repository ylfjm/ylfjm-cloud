package com.github.ylfjm.product.controller;

import com.github.ylfjm.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * @param productId 商品ID
     * @param count     购买的数量
     */
    @GetMapping("/product/deduct")
    public Boolean deduct(@RequestParam("productId") Integer productId,
                          @RequestParam("count") Integer count,
                          @RequestParam("error") Integer error) {
        productService.deduct(productId, count, error);
        return true;
    }

}
