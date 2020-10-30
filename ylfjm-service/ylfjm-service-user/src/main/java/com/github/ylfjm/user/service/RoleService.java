package com.github.ylfjm.user.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.ylfjm.common.BadRequestException;
import com.github.ylfjm.common.NotFoundException;
import com.github.ylfjm.common.YlfjmException;
import com.github.ylfjm.common.cache.UserCache;
import com.github.ylfjm.common.pojo.vo.PageVO;
import com.github.ylfjm.user.cache.PermissionCacheHelper;
import com.github.ylfjm.user.dto.PermissionCacheDTO;
import com.github.ylfjm.user.mapper.AdminRoleMapper;
import com.github.ylfjm.user.mapper.MenuMapper;
import com.github.ylfjm.user.mapper.PermissionMapper;
import com.github.ylfjm.user.mapper.RoleMapper;
import com.github.ylfjm.user.mapper.RoleMenuMapper;
import com.github.ylfjm.user.mapper.RolePermissionMapper;
import com.github.ylfjm.user.po.AdminRole;
import com.github.ylfjm.user.po.Menu;
import com.github.ylfjm.user.po.Role;
import com.github.ylfjm.user.po.RoleMenu;
import com.github.ylfjm.user.po.RolePermission;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author Zhang Bo
 * @date 2018/11/22
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class RoleService {

    private final RoleMapper roleMapper;
    private final MenuMapper menuMapper;
    private final PermissionMapper permissionMapper;
    private final AdminRoleMapper adminRoleMapper;
    private final RoleMenuMapper roleMenuMapper;
    private final RolePermissionMapper rolePermissionMapper;

    /**
     * 创建角色信息
     *
     * @param role          角色信息
     * @param permissionIds 权限ID集合
     */
    @Transactional(rollbackFor = Exception.class)
    public void add(Role role, Set<Integer> permissionIds) {
        role.setSysType(UserCache.getJWTInfo().getType());
        if (!StringUtils.hasText(role.getName())) {
            throw new BadRequestException("操作失败，角色名不能为空");
        }
        Role query = new Role();
        query.setName(role.getName());
        query.setSysType(role.getSysType());
        int count = roleMapper.selectCount(query);
        if (count > 0) {
            throw new BadRequestException("操作失败，角色已存在");
        }
        role.setCreator(UserCache.getCurrentAdminRealName());
        role.setCreateTime(new Date());
        role.setId(null);
        int result = roleMapper.insert(role);
        if (result < 1) {
            throw new YlfjmException("操作失败，新增角色发生错误");
        }
        // 为角色绑定权限
        if (!CollectionUtils.isEmpty(permissionIds)) {
            this.addPermissions(role.getId(), permissionIds);
        }
        // 更新缓存
        Set<PermissionCacheDTO> permissions = permissionMapper.selectCodeAndMethodByRoleId(role.getId());
        PermissionCacheHelper.setRList(role.getId(), permissions);
    }

    /**
     * 删除角色
     *
     * @param id 角色ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        Role role = roleMapper.selectByPrimaryKey(id);
        if (role == null) {
            throw new NotFoundException("操作失败，角色不存在或已被删除");
        }
        Integer sysType = UserCache.getJWTInfo().getType();
        if (!Objects.equals(role.getSysType(), sysType)) {
            throw new BadRequestException("非法操作");
        }
        // 角色有没有关联的用户，如果有不能删除
        AdminRole query = new AdminRole();
        query.setRoleId(id);
        int count = adminRoleMapper.selectCount(query);
        if (count > 0) {
            throw new BadRequestException("操作失败，尚有管理员在使用该角色，不能删除");
        }
        int result = roleMapper.deleteByPrimaryKey(id);
        if (result < 1) {
            throw new YlfjmException("操作失败，删除角色发生错误");
        }
        // 删除旧的角色-菜单关联数据
        RoleMenu roleMenu = new RoleMenu();
        roleMenu.setRoleId(id);
        roleMenuMapper.delete(roleMenu);
        // 删除旧的角色-权限关联数据
        RolePermission rolePermission = new RolePermission();
        rolePermission.setRoleId(id);
        rolePermissionMapper.delete(rolePermission);
        // 更新缓存
        Set<PermissionCacheDTO> permissions = permissionMapper.selectCodeAndMethodByRoleId(id);
        PermissionCacheHelper.setRList(id, permissions);
    }

    /**
     * 更新角色
     *
     * @param role          角色信息
     * @param permissionIds 权限ID集合
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(Role role, Set<Integer> permissionIds) {
        if (!StringUtils.hasText(role.getName())) {
            throw new BadRequestException("操作失败，角色名不能为空");
        }
        if (Objects.isNull(role.getId())) {
            throw new BadRequestException("操作失败，请选择角色");
        }
        Role record = roleMapper.selectByPrimaryKey(role.getId());
        if (record == null) {
            throw new BadRequestException("操作失败，角色不存在或已被删除");
        }
        Integer sysType = UserCache.getJWTInfo().getType();
        if (!Objects.equals(record.getSysType(), sysType)) {
            throw new BadRequestException("非法操作");
        }
        Example example = new Example(Role.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andNotEqualTo("id", role.getId());
        criteria.andEqualTo("name", role.getName());
        criteria.andEqualTo("sysType", record.getSysType());
        int count = roleMapper.selectCountByExample(example);
        if (count > 0) {
            throw new BadRequestException("操作失败，角色已存在");
        }
        role.setUpdater(UserCache.getCurrentAdminRealName());
        role.setUpdateTime(new Date());
        // 不更新sysType
        role.setSysType(null);
        int result = roleMapper.updateByPrimaryKeySelective(role);
        if (result < 1) {
            throw new YlfjmException("操作失败，修改角色发生错误");
        }
        // 为角色绑定权限
        if (!CollectionUtils.isEmpty(permissionIds)) {
            this.addPermissions(role.getId(), permissionIds);
        }
        // 更新缓存
        Set<PermissionCacheDTO> permissions = permissionMapper.selectCodeAndMethodByRoleId(role.getId());
        PermissionCacheHelper.setRList(role.getId(), permissions);
    }

    /**
     * 分页查询角色信息
     *
     * @param pageNum  第几页
     * @param pageSize 每页大小
     */
    public PageVO<Role> page(int pageNum, int pageSize, String name) {
        // 分页查询
        PageHelper.startPage(pageNum, pageSize);
        Page<Role> page = roleMapper.page(name, UserCache.getJWTInfo().getType());
        return new PageVO<>(pageNum, page);
    }

    /**
     * 为角色添加权限
     *
     * @param roleId        角色ID
     * @param permissionIds 权限ID集合
     */
    private void addPermissions(Integer roleId, Set<Integer> permissionIds) {
        Role role = roleMapper.selectByPrimaryKey(roleId);
        if (role == null) {
            throw new BadRequestException("操作失败，角色不存在或已被删除");
        }
        boolean haveAllMenu = false;
        if (role.getName().contains("系统超级管理员")) {
            haveAllMenu = true;
        }
        // 1-删除旧的角色-权限关联数据
        RolePermission rolePermission = new RolePermission();
        rolePermission.setRoleId(roleId);
        rolePermissionMapper.delete(rolePermission);
        // 2-删除旧的角色-菜单关联数据
        RoleMenu roleMenu = new RoleMenu();
        roleMenu.setRoleId(roleId);
        roleMenuMapper.delete(roleMenu);
        // 3-新增新的角色-权限关联数据
        List<RolePermission> rolePermissionList = new ArrayList<>();
        for (Integer permissionId : permissionIds) {
            rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(permissionId);
            rolePermissionList.add(rolePermission);
        }
        // 批量插入角色权限映射
        rolePermissionMapper.batchInsert(rolePermissionList);
        // 4-新增新的角色-菜单关联数据
        // 根据权限ID集合获取菜单集合
        Set<Menu> menus = menuMapper.selectMenuBypermissionIds(permissionIds);
        if (!CollectionUtils.isEmpty(menus)) {
            // 定义菜单ID集合
            Set<Integer> menuIds = new HashSet<>();
            for (Menu menu : menus) {
                menuIds.add(menu.getId());
                Integer pid = menu.getPid();
                // 从子菜单一直遍历找到一级菜单，将所有菜单ID存放到menuIds中
                while (pid != 0 && !menuIds.contains(pid)) {
                    menuIds.add(pid);
                    // 根据菜单ID查询菜单pid
                    pid = menuMapper.selectPidById(pid);
                }
            }
            // 如果是超级管理员，则额外设置上所有菜单的可见权限，实际是否能访问还是要看RolePermission
            if (haveAllMenu) {
                Set<Integer> allMenuId = menuMapper.selectAllMenuId(role.getSysType());
                menuIds.addAll(allMenuId);
            }
            List<RoleMenu> roleMenuList = new ArrayList<>();
            for (Integer menuId : menuIds) {
                roleMenu = new RoleMenu();
                roleMenu.setRoleId(roleId);
                roleMenu.setMenuId(menuId);
                roleMenuList.add(roleMenu);
            }
            // 批量插入角色权限映射
            roleMenuMapper.batchInsert(roleMenuList);
        }
    }

}
