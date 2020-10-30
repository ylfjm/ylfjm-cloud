package com.github.ylfjm.product.service;

import org.springframework.stereotype.Service;

@Service
public class ProductCategoryService {

    public void error(Integer error) {
        if (error == 1) {
            int i = 1 / 0;
        }
    }
}
