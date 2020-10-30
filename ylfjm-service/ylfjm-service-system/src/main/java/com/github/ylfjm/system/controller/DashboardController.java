package com.github.ylfjm.system.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 仪表盘
 */
@RestController
public class DashboardController {

    /**
     * 概览
     */
    @GetMapping(value = "/dashboard")
    public Object dashboard() {
        Map<String, Object> map = new HashMap<>();
        map.put("adminCount", 100);
        map.put("departmentCount", 100);
        map.put("roleCount", 100);
        map.put("menuCount", 100);
        return map;
    }

}
