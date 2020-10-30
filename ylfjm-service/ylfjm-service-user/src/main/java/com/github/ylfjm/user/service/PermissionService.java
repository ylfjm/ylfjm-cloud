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
import com.github.ylfjm.user.mapper.PermissionMapper;
import com.github.ylfjm.user.mapper.RolePermissionMapper;
import com.github.ylfjm.user.po.Permission;
import com.github.ylfjm.user.po.RolePermission;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author Zhang Bo
 * @date 2018/12/20
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class PermissionService {

    private final MenuService menuService;
    private final PermissionMapper permissionMapper;
    private final RolePermissionMapper rolePermissionMapper;
    private final AdminRoleMapper adminRoleMapper;

    /**
     * 新增权限
     *
     * @param permission 权限信息
     */
    public void add(Permission permission) {
        permission.setSysType(UserCache.getJWTInfo().getType());
        // 参数校验
        this.checkParams(permission);
        Permission query = new Permission();
        query.setMethod(permission.getMethod());
        query.setCode(permission.getCode());
        query.setMenuId(permission.getMenuId());
        query.setSysType(permission.getSysType());
        int count = permissionMapper.selectCount(query);
        if (count > 0) {
            throw new BadRequestException("操作失败，权限已存在");
        }
        // 设置创建人
        permission.setCreator(UserCache.getCurrentAdminRealName());
        // 设置创建时间
        permission.setCreateTime(new Date());
        permission.setId(null);
        int result = permissionMapper.insert(permission);
        if (result < 1) {
            throw new YlfjmException("操作失败，新增权限发生错误");
        }
        // 更新缓存
        Set<PermissionCacheDTO> permissions = permissionMapper.selectBySysType(permission.getSysType());
        PermissionCacheHelper.setPList(permission.getSysType(), permissions);
    }

    /**
     * 删除权限
     *
     * @param id 权限ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        Permission permission = permissionMapper.selectByPrimaryKey(id);
        if (permission == null) {
            throw new NotFoundException("操作失败，权限不存在或已被删除");
        }
        Integer sysType = UserCache.getJWTInfo().getType();
        if (!Objects.equals(permission.getSysType(), sysType)) {
            throw new BadRequestException("非法操作");
        }
        int result = permissionMapper.deleteByPrimaryKey(id);
        if (result < 1) {
            throw new YlfjmException("操作失败，删除权限发生错误");
        }
        // 删除权限角色关联数据
        RolePermission rolePermission = new RolePermission();
        rolePermission.setPermissionId(id);
        rolePermissionMapper.delete(rolePermission);
        // 更新缓存
        Set<PermissionCacheDTO> permissions = permissionMapper.selectBySysType(sysType);
        PermissionCacheHelper.setPList(sysType, permissions);
    }

    /**
     * 更新权限
     *
     * @param permission 权限信息
     */
    public void update(Permission permission) {
        // 参数校验
        this.checkParams(permission);
        if (Objects.isNull(permission.getId())) {
            throw new BadRequestException("操作失败，请选择权限");
        }
        Permission record = permissionMapper.selectByPrimaryKey(permission.getId());
        if (record == null) {
            throw new BadRequestException("操作失败，权限不存在或已被删除");
        }
        Integer sysType = UserCache.getJWTInfo().getType();
        if (!Objects.equals(record.getSysType(), sysType)) {
            throw new BadRequestException("非法操作");
        }
        // 校验权限是否已存在
        Example example = new Example(Permission.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andNotEqualTo("id", permission.getId());
        criteria.andEqualTo("method", permission.getMethod());
        criteria.andEqualTo("code", permission.getCode());
        criteria.andEqualTo("menuId", permission.getMenuId());
        criteria.andEqualTo("sysType", record.getSysType());
        int count = permissionMapper.selectCountByExample(example);
        if (count > 0) {
            throw new BadRequestException("操作失败，权限已存在");
        }
        // 设置更新人
        permission.setUpdater(UserCache.getCurrentAdminRealName());
        // 设置更新时间
        permission.setUpdateTime(new Date());
        // 不更新sysType
        permission.setSysType(null);
        permissionMapper.updateByPrimaryKeySelective(permission);
        // 更新缓存
        Set<PermissionCacheDTO> permissions = permissionMapper.selectBySysType(record.getSysType());
        PermissionCacheHelper.setPList(record.getSysType(), permissions);
    }

    /**
     * 分页查询权限信息，可带查询条件
     *
     * @param pageNum  第几页
     * @param pageSize 每页大小
     * @param menuId   菜单ID
     */
    public PageVO<Permission> page(Integer pageNum, Integer pageSize, Integer menuId, String name, String code) {
        Integer sysType = UserCache.getJWTInfo().getType();
        List<Integer> subMenuIdList = null;
        if (menuId != null) {
            subMenuIdList = menuService.getChildMenuIds(sysType, menuId);
        }
        // 分页查询
        PageHelper.startPage(pageNum, pageSize);
        Page<Permission> page = permissionMapper.page(sysType, subMenuIdList, name, code);
        return new PageVO<>(pageNum, page);
    }

    /**
     * 参数校验
     *
     * @param permission 权限信息
     */
    private void checkParams(Permission permission) {
        if (!StringUtils.hasText(permission.getName())) {
            throw new BadRequestException("操作失败，权限名不能为空");
        }
        if (!StringUtils.hasText(permission.getMethod())) {
            throw new BadRequestException("操作失败，方法不能为空");
        }
        if (!StringUtils.hasText(permission.getCode())) {
            throw new BadRequestException("操作失败，权限url不能为空");
        }
        if (Objects.isNull(permission.getMenuId())) {
            throw new BadRequestException("操作失败，权限所属菜单不能为空");
        }
    }

    /**
     * 获取权限信息
     *
     * @param sysType 所属系统
     * @param adminId 管理员ID
     */
    public Set<PermissionCacheDTO> getPermissionList(Integer sysType, Integer adminId) {
        if (adminId == null) {
            return permissionMapper.selectBySysType(sysType);
        } else {
            Set<Integer> roleIds = adminRoleMapper.selectRoleIdsByAdminId(adminId);
            if (CollectionUtils.isEmpty(roleIds)) {
                return null;
            }
            Set<PermissionCacheDTO> permissions = new HashSet<>();
            for (Integer roleId : roleIds) {
                Set<PermissionCacheDTO> tempList = PermissionCacheHelper.getRList(roleId);
                if (CollectionUtils.isEmpty(tempList)) {
                    tempList = permissionMapper.selectCodeAndMethodByRoleId(roleId);
                    PermissionCacheHelper.setRList(roleId, tempList);
                }
                if (!CollectionUtils.isEmpty(tempList)) {
                    permissions.addAll(tempList);
                }
            }
            return permissions;
        }
    }

    // /**
    //  * 根据菜单ID获取权限列表
    //  *
    //  * @param menuId 菜单ID
    //  */
    // public List<Permission> listByMenuId(Integer menuId) {
    //     return permissionMapper.selectPermissionByMenuId(menuId);
    // }

}
