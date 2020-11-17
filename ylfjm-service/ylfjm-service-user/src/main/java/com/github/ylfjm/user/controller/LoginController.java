package com.github.ylfjm.user.controller;

import com.github.ylfjm.common.constant.AuthConstant;
import com.github.ylfjm.common.jwt.JWTInfo;
import com.github.ylfjm.common.jwt.JwtHelper;
import com.github.ylfjm.common.utils.IpUtil;
import com.github.ylfjm.user.po.Admin;
import com.github.ylfjm.user.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 描述：各端登录逻辑控制器
 *
 * @author YLFJM
 * @Date 2019/3/26
 */
@RestController
@Slf4j
@RequiredArgsConstructor
public class LoginController {

    private final AdminService adminService;

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
                       @RequestParam("sysType") Integer sysType,
                       HttpServletRequest request, HttpServletResponse response) {
        String ip = IpUtil.getIp(request);
        Admin admin = adminService.login(userName, password, sysType, ip);
        JWTInfo jwtInfo = new JWTInfo();
        jwtInfo.setId(admin.getId());
        jwtInfo.setAccount(admin.getUserName());
        jwtInfo.setRealName(admin.getRealName());
        jwtInfo.setType(sysType);
        String jwtToken = JwtHelper.createJWTToken(jwtInfo);
        response.setHeader(AuthConstant.ADMIN_TOKEN, jwtToken);
        return admin;
    }

}
