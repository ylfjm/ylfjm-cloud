package com.github.ylfjm.user.feign;

import com.github.ylfjm.common.constant.Constant;
import com.github.ylfjm.common.pojo.vo.PageVO;
import com.github.ylfjm.user.po.Admin;
import com.github.ylfjm.user.po.Department;
import com.github.ylfjm.user.po.Menu;
import com.github.ylfjm.user.po.Permission;
import com.github.ylfjm.user.dto.PermissionCacheDTO;
import com.github.ylfjm.user.po.Role;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

@FeignClient(name = Constant.YLFJM_SERVICE_USER)
public interface UserFeign {

    @GetMapping(value = "/user/reduce")
    Boolean reduce(@RequestParam("userId") Integer userId, @RequestParam("money") Integer money);

    /**
     * 创建管理员
     *
     * @param admin 管理员信息
     */
    @PostMapping(value = "/admin", consumes = MediaType.APPLICATION_JSON_VALUE)
    void addAdmin(@RequestBody Admin admin);

    /**
     * 删除管理员账号
     *
     * @param id 管理员ID
     */
    @DeleteMapping(value = "/admin/{id}")
    void deleteAdmin(@PathVariable(value = "id") Integer id);

    /**
     * 禁用/启用管理员账号
     *
     * @param id 管理员ID
     */
    @PatchMapping(value = "/admin/{id}/forbidden")
    void forbiddenAdmin(@PathVariable(value = "id") Integer id);

    /**
     * 更新管理员
     *
     * @param admin 管理员信息
     */
    @PutMapping(value = "/admin", consumes = MediaType.APPLICATION_JSON_VALUE)
    void updateAdmin(@RequestBody Admin admin);

    /**
     * 查询管理员列表
     *
     * @param pageNum   第几页
     * @param pageSize  每页大小
     * @param roleId    角色ID
     * @param deptId    部门ID
     * @param realName  管理员姓名
     * @param forbidden 禁用状态
     */
    @GetMapping(value = "/admin/{pageNum}/{pageSize}")
    PageVO<Admin> getAdminPage(@PathVariable(value = "pageNum") Integer pageNum,
                               @PathVariable(value = "pageSize") Integer pageSize,
                               @RequestParam(value = "roleId", required = false) Integer roleId,
                               @RequestParam(value = "deptId", required = false) Integer deptId,
                               @RequestParam(value = "realName", required = false) String realName,
                               @RequestParam(value = "forbidden", required = false) Boolean forbidden);

    /**
     * 系统管理员登录
     *
     * @param userName 用户名
     * @param password 密码
     * @param sysType  登录系统类型
     */
    @PostMapping(value = "/admin/login")
    Admin adminLogin(@RequestParam("userName") String userName,
                     @RequestParam("password") String password,
                     @RequestParam("sysType") Integer sysType);

    /**
     * 新增部门
     *
     * @param department 部门信息
     */
    @PostMapping(value = "/department", consumes = MediaType.APPLICATION_JSON_VALUE)
    void addDepartment(@RequestBody Department department);

    /**
     * 删除某个部门
     *
     * @param id 部门ID
     */
    @DeleteMapping(value = "/department/{id}")
    void deleteDepartment(@PathVariable(value = "id") Integer id);

    /**
     * 更新某个部门信息
     *
     * @param department 部门信息
     */
    @PutMapping(value = "/department", consumes = MediaType.APPLICATION_JSON_VALUE)
    void updateDepartment(@RequestBody Department department);

    /**
     * 查询部门列表
     *
     * @param pageNum  第几页
     * @param pageSize 每页大小
     * @param name     部门名称
     */
    @GetMapping(value = "/department/{pageNum}/{pageSize}")
    PageVO<Department> getDepartmentPage(@PathVariable(value = "pageNum") Integer pageNum,
                                         @PathVariable(value = "pageSize") Integer pageSize,
                                         @RequestParam(value = "name", required = false) String name);

    /**
     * 添加菜单
     *
     * @param menu 菜单信息
     */
    @PostMapping(value = "/menu", consumes = MediaType.APPLICATION_JSON_VALUE)
    void addMenu(@RequestBody Menu menu);

    /**
     * 删除菜单
     *
     * @param id 菜单ID
     */
    @DeleteMapping(value = "/menu/{id}")
    void deleteMenu(@PathVariable(value = "id") Integer id);

    /**
     * 更新菜单
     *
     * @param menu 菜单信息
     */
    @PutMapping(value = "/menu", consumes = MediaType.APPLICATION_JSON_VALUE)
    void updateMenu(@RequestBody Menu menu);

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
    PageVO<Menu> getMenuPage(@PathVariable(value = "pageNum") Integer pageNum,
                             @PathVariable(value = "pageSize") Integer pageSize,
                             @RequestParam(value = "pid", required = false) Integer pid,
                             @RequestParam(value = "name", required = false) String name,
                             @RequestParam(value = "level", required = false) Integer level);

    /**
     * 获取级联菜单结构
     */
    @GetMapping(value = "/menu/tree")
    List<Menu> listMenuTree();

    /**
     * 根据角色ID获取所有菜单及权限集合
     *
     * @param roleId 角色ID
     */
    @GetMapping(value = "/menu/permission")
    List<Menu> listMenuAndPermission(@RequestParam(value = "roleId", required = false) Integer roleId);

    /**
     * 创建权限
     *
     * @param permission 权限信息
     */
    @PostMapping(value = "/permission", consumes = MediaType.APPLICATION_JSON_VALUE)
    void addPermission(@RequestBody Permission permission);

    /**
     * 删除权限账号
     *
     * @param id 权限ID
     */
    @DeleteMapping(value = "/permission/{id}")
    void deletePermission(@PathVariable(value = "id") Integer id);

    /**
     * 更新权限
     *
     * @param permission 权限信息
     */
    @PutMapping(value = "/permission", consumes = MediaType.APPLICATION_JSON_VALUE)
    void updatePermission(@RequestBody Permission permission);

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
    PageVO<Permission> getPermissionPage(@PathVariable(value = "pageNum") Integer pageNum,
                                         @PathVariable(value = "pageSize") Integer pageSize,
                                         @RequestParam(value = "menuId", required = false) Integer menuId,
                                         @RequestParam(value = "name", required = false) String name,
                                         @RequestParam(value = "code", required = false) String code);

    /**
     * 获取权限信息
     *
     * @param sysType 所属系统
     * @param adminId 管理员ID
     */
    @GetMapping(value = "/permissionList")
    Set<PermissionCacheDTO> getPermissionList(@RequestParam(value = "sysType") Integer sysType,
                                              @RequestParam(value = "adminId", required = false) Integer adminId);

    /**
     * 创建角色
     *
     * @param role 角色信息
     */
    @PostMapping(value = "/role", consumes = MediaType.APPLICATION_JSON_VALUE)
    void addRole(@RequestBody Role role);

    /**
     * 删除角色
     *
     * @param id 角色ID
     */
    @DeleteMapping(value = "/role/{id}")
    void deleteRole(@PathVariable(value = "id") Integer id);

    /**
     * 更新角色
     *
     * @param role 角色信息
     */
    @PutMapping(value = "/role", consumes = MediaType.APPLICATION_JSON_VALUE)
    void updateRole(@RequestBody Role role);

    /**
     * 查询角色列表
     *
     * @param pageNum  第几页
     * @param pageSize 每页大小
     * @param name     角色名称
     */
    @GetMapping(value = "/role/{pageNum}/{pageSize}")
    PageVO<Role> getRolePage(@PathVariable(value = "pageNum") Integer pageNum,
                             @PathVariable(value = "pageSize") Integer pageSize,
                             @RequestParam(value = "name", required = false) String name);

}
