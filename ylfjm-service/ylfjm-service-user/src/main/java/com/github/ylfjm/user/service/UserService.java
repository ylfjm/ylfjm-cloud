package com.github.ylfjm.user.service;

import com.github.ylfjm.user.mapper.UserMapper;
import com.github.ylfjm.user.po.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;

    public void reduce(Integer userId, Integer money) {
        User user = userMapper.selectByPrimaryKey(userId);
        User update = new User();
        update.setId(userId);
        update.setMoney(user.getMoney() - money);
        userMapper.updateByPrimaryKey(update);
    }
}
