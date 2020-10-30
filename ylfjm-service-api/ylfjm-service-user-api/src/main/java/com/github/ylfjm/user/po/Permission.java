package com.github.ylfjm.user.po;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * 权限
 *
 * @author Zhang Bo
 * @date 2018/11/2
 */
@Table(name = "system_permission")
@Getter
@Setter
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String code;
    private String method;
    private Integer menuId;
    private Integer sysType;//所属系统：2-翼猫业务管理系统；3-净水设备互动广告系统；

    private String creator;
    private Date createTime;
    private String updater;
    private Date updateTime;

    @Transient
    private Boolean have;//标识该权限是否被拥有
    @Transient
    private String menuName;

}
