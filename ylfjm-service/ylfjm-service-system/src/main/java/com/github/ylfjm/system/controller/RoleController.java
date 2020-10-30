package com.github.ylfjm.system.controller;

import com.github.ylfjm.common.pojo.vo.PageVO;
import com.github.ylfjm.user.feign.UserFeign;
import com.github.ylfjm.user.po.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 角色
 *
 * @author Zhang Bo
 * @date 2018/11/3
 */
@RestController
@RequiredArgsConstructor
public class RoleController {

    private final UserFeign userFeign;

    /**
     * 创建角色
     *
     * @param role 角色信息
     */
    @PostMapping(value = "/role")
    public void add(@RequestBody Role role) {
        userFeign.addRole(role);
    }

    /**
     * 删除角色
     *
     * @param id 角色ID
     */
    @DeleteMapping(value = "/role/{id}")
    public void delete(@PathVariable Integer id) {
        userFeign.deleteRole(id);
    }

    /**
     * 更新角色
     *
     * @param role 角色信息
     */
    @PutMapping(value = "/role")
    public void update(@RequestBody Role role) {
        userFeign.updateRole(role);
    }

    /**
     * 查询角色列表
     *
     * @param pageNum  第几页
     * @param pageSize 每页大小
     * @param name     角色名称
     */
    @GetMapping(value = "/role/{pageNum}/{pageSize}")
    public PageVO<Role> page(@PathVariable Integer pageNum,
                             @PathVariable Integer pageSize,
                             @RequestParam(required = false) String name) {
        return userFeign.getRolePage(pageNum, pageSize, name);
    }

}
