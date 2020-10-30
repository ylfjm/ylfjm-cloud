package com.github.ylfjm.user.mapper;

import com.github.ylfjm.user.po.RolePermission;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Set;

/**
 * @author Zhang Bo
 * @date 2018/11/2
 */
public interface RolePermissionMapper extends Mapper<RolePermission> {

    /**
     * 批量插入
     *
     * @param list 角色-权限关联数据集合
     */
    void batchInsert(@Param("list") List<RolePermission> list);

    /**
     * 根据角色ID集合查询权限ID集合
     *
     * @param roleId 角色ID
     */
    Set<Integer> selectPermissionIdsByRoleId(@Param("roleId") Integer roleId);
}
