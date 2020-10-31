package com.github.ylfjm.system.controller;

import com.github.ylfjm.common.pojo.vo.PageVO;
import com.github.ylfjm.user.feign.UserFeign;
import com.github.ylfjm.user.po.Menu;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 菜单
 *
 * @author YLFJM
 * @date 2018/11/3
 */
@RestController
@RequiredArgsConstructor
public class MenuController {

    private final UserFeign userFeign;

    /**
     * 添加菜单
     *
     * @param menu 菜单信息
     */
    @PostMapping(value = "/menu")
    public void add(@RequestBody Menu menu) {
        userFeign.addMenu(menu);
    }

    /**
     * 删除菜单
     *
     * @param id 菜单ID
     */
    @DeleteMapping(value = "/menu/{id}")
    public void delete(@PathVariable Integer id) {
        userFeign.deleteMenu(id);
    }

    /**
     * 更新菜单
     *
     * @param menu 菜单信息
     */
    @PutMapping(value = "/menu")
    public void update(@RequestBody Menu menu) {
        userFeign.updateMenu(menu);
    }

    /**
     * 查询菜单列表
     *
     * @param pageNum  第几页
     * @param pageSize 每页大小
     * @param pid      父菜单ID
     * @param name     菜单名称
     * @param level    菜单层级
     */
    @GetMapping(value = "/menu/{pageNum}/{pageSize}")
    public PageVO<Menu> page(@PathVariable Integer pageNum,
                             @PathVariable Integer pageSize,
                             @RequestParam(required = false) Integer pid,
                             @RequestParam(required = false) String name,
                             @RequestParam(required = false) Integer level) {
        return userFeign.getMenuPage(pageNum, pageSize, pid, name, level);
    }

    /**
     * 获取级联菜单结构
     */
    @GetMapping(value = "/menu/tree")
    public List<Menu> listMenuTree() {
        return userFeign.listMenuTree();
    }

    /**
     * 根据角色ID获取所有菜单及权限集合
     *
     * @param roleId 角色ID
     */
    @GetMapping(value = "/menu/permission")
    public List<Menu> listMenuAndPermission(@RequestParam(required = false) Integer roleId) {
        return userFeign.listMenuAndPermission(roleId);
    }

    // /**
    //  * 根据菜单ID获取权限列表
    //  */
    // @GetMapping(value = "/menu/permissions")
    // public List<Permission> listPermissionByMenuId(@RequestParam(required = false) Integer menuId) {
    //     return permissionService.listByMenuId(menuId);
    // }

}
