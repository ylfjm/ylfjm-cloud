package com.github.ylfjm.user.controller;

import com.github.ylfjm.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * @param userId 用户ID
     * @param money  用户账户要减少的金额
     */
    @GetMapping(value = "/user/reduce")
    public Boolean reduce(@RequestParam("userId") Integer userId, @RequestParam("money") Integer money) {
        userService.reduce(userId, money);
        return true;
    }

    @GetMapping(value = "/user/login")
    public String login() {
        return "success";
    }

}
