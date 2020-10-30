package com.github.ylfjm.user.po;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 角色-权限关联
 *
 * @author Zhang Bo
 * @date 2018/11/7
 */
@Table(name = "system_role__permission")
@Getter
@Setter
public class RolePermission {

    @Id
    private Integer roleId;
    @Id
    private Integer permissionId;

}
