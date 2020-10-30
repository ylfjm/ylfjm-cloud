package com.github.ylfjm.business.controller;

import com.github.ylfjm.business.service.BusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BusinessController {

    private final BusinessService businessService;

    /**
     * 全局事务入口
     */
    @GetMapping(value = "/purchase/commit")
    public String purchaseCommit(@RequestParam("error") Integer error) {
        try {
            businessService.purchase(1000000, 2000000, 1, error);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "全局事务提交";
    }

}
