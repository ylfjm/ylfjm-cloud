package com.github.ylfjm.product.service;

import com.github.ylfjm.product.mapper.ProductMapper;
import com.github.ylfjm.product.po.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductMapper productMapper;
    private final ProductCategoryService productCategoryService;

    public void deduct(Integer productId, Integer count, Integer error) {
        Product product = productMapper.selectByPrimaryKey(productId);
        Product update = new Product();
        update.setId(productId);
        update.setCount(product.getCount() - count);
        productMapper.updateByPrimaryKey(update);
        //手动抛出异常
        // productCategoryService.error(error);
        this.error(error);
    }

    private void error(Integer error) {
        if (error == 1) {
            int i = 1 / 0;
        }
    }

}
