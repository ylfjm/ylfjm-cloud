package com.github.ylfjm.business.service;

import com.github.ylfjm.business.mapper.BusinessMapper;
import com.github.ylfjm.business.po.Business;
import com.github.ylfjm.order.feign.OrderFeign;
import com.github.ylfjm.product.feign.ProductFeign;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class BusinessService {

    private final BusinessMapper businessMapper;
    private final OrderFeign orderFeign;
    private final ProductFeign productFeign;

    public void purchase(Integer userId, Integer productId, Integer count, Integer error) {
        //添加日志
        Business business = new Business();
        business.setContent("测试分布式事务");
        business.setCreateTime(new Date());
        businessMapper.insert(business);
        //调用feign      下单--->扣款
        orderFeign.create(userId, productId, count);
        //调用feign      扣减库存
        productFeign.deduct(productId, count, error);
    }

}
