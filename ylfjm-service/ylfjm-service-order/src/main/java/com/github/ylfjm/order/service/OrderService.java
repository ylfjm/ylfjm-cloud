package com.github.ylfjm.order.service;

import com.github.ylfjm.order.mapper.OrderMapper;
import com.github.ylfjm.order.po.Order;
import com.github.ylfjm.user.feign.UserFeign;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderMapper orderMapper;
    private final UserFeign userFeign;

    public void create(Integer userId, Integer productId, Integer count) {
        System.out.println("超时重试------");
        try {
            Thread.sleep(15500L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //订单金额
        int money = count * 100;
        Order order = new Order();
        order.setUserId(userId);
        order.setProductId(productId);
        order.setCount(count);
        order.setMoney(money);
        order.setCreateTime(new Date());
        orderMapper.insert(order);
        //扣减余额
        userFeign.reduce(userId, money);
    }
}
