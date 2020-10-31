package com.github.ylfjm.user.po;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

/**
 * 菜单
 *
 * @author YLFJM
 * @date 2018/11/2
 */
@Table(name = "system_menu")
@Getter
@Setter
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    private String url;
    private String icon;//菜单图标
    private Integer pid;//父级菜单id
    private Integer level;//菜单层级
    private Integer sorts;//排序
    private Integer sysType;//所属系统：2-翼猫业务管理系统；3-净水设备互动广告系统；

    private String creator;
    private Date createTime;
    private String updater;
    private Date updateTime;

    @Transient
    private List<Menu> subMenus;//子菜单集合
    @Transient
    private List<Permission> permissions;//权限集合
    @Transient
    private Boolean have;//标识该菜单是否被拥有

}
