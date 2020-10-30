package com.github.ylfjm.order.controller;

import com.github.ylfjm.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * @param userId    用户ID
     * @param productId 要购买的商品的ID
     * @param count     购买的数量
     */
    @GetMapping(value = "/order/create")
    public Boolean create(@RequestParam("userId") Integer userId,
                          @RequestParam("productId") Integer productId,
                          @RequestParam("count") Integer count) {
        orderService.create(userId, productId, count);
        return true;
    }

}
