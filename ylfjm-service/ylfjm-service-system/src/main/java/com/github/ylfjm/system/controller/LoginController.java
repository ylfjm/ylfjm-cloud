package com.github.ylfjm.system.controller;

import com.github.ylfjm.user.feign.UserFeign;
import com.github.ylfjm.user.po.Admin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述：登录逻辑控制器
 *
 * @author YLFJM
 * @Date 2019/3/26
 */
@RestController
@Slf4j
@RequiredArgsConstructor
public class LoginController {

    private final UserFeign userFeign;

    /**
     * 系统管理员登录
     *
     * @param userName 用户名
     * @param password 密码
     * @param sysType  登录系统类型
     */
    @PostMapping(value = "/admin/login")
    public Admin login(@RequestParam("userName") String userName,
                       @RequestParam("password") String password,
                       @RequestParam("sysType") Integer sysType) {
        Admin admin = userFeign.adminLogin(userName, password, sysType);
        return admin;
    }

}
