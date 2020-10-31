package com.github.ylfjm.system.controller;

import com.github.ylfjm.common.pojo.vo.PageVO;
import com.github.ylfjm.user.feign.UserFeign;
import com.github.ylfjm.user.po.Permission;
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
 * 权限
 *
 * @author YLFJM
 * @date 2018/11/3
 */
@RestController
@RequiredArgsConstructor
public class PermissionController {

    private final UserFeign userFeign;

    /**
     * 创建权限
     *
     * @param permission 权限信息
     */
    @PostMapping(value = "/permission")
    public void add(@RequestBody Permission permission) {
        userFeign.addPermission(permission);
    }

    /**
     * 删除权限账号
     *
     * @param id 权限ID
     */
    @DeleteMapping(value = "/permission/{id}")
    public void delete(@PathVariable Integer id) {
        userFeign.deletePermission(id);
    }

    /**
     * 更新权限
     *
     * @param permission 权限信息
     */
    @PutMapping(value = "/permission")
    public void update(@RequestBody Permission permission) {
        userFeign.updatePermission(permission);
    }

    /**
     * 查询权限列表
     *
     * @param pageNum  第几页
     * @param pageSize 每页大小
     * @param menuId   菜单ID
     * @param name     权限名称
     * @param code     权限CODE
     */
    @GetMapping(value = "/permission/{pageNum}/{pageSize}")
    public PageVO<Permission> page(@PathVariable Integer pageNum,
                                   @PathVariable Integer pageSize,
                                   @RequestParam(required = false) Integer menuId,
                                   @RequestParam(required = false) String name,
                                   @RequestParam(required = false) String code) {
        return userFeign.getPermissionPage(pageNum, pageSize, menuId, name, code);
    }

    // /**
    //  * 查询权限列表
    //  *
    //  * @param sysType 系统类型
    //  */
    // @GetMapping(value = "/permissions")
    // public List<PermissionCache> listPermissionBySysType(@RequestParam Integer sysType) {
    //     return permissionMapper.selectBySysType(sysType);
    // }

}
