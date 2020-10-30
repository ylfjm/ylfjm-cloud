package com.github.ylfjm.user.po;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 角色-菜单关联
 *
 * @author Zhang Bo
 * @date 2018/11/2
 */
@Table(name = "system_role__menu")
@Getter
@Setter
public class RoleMenu {

    @Id
    private Integer roleId;
    @Id
    private Integer menuId;

}
