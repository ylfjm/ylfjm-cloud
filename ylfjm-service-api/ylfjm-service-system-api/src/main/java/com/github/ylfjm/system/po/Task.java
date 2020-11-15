package com.github.ylfjm.system.po;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 描述：TODO
 *
 * @Author Zhang Bo
 * @Date 2020/11/15
 */
@Table(name = "task")
@Getter
@Setter
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    //所属项目
    private Integer projectId;
    //所属模块
    private Integer module;
    //任务名称
    private String name;
    //任务类型
    private String type;
    //优先级
    private Integer pri;
    //最初预计
    private Integer estimate;
    //总消耗
    private Integer consumed;
    //预计剩余
    private Integer left;
    //截止日期
    private Date deadline;
    //任务状态：'wait','doing','done','pause','cancel','closed'
    private String status;
    //color
    private String color;
    //抄送给
    private String mailto;
    //任务描述
    private String desc;
    //由谁创建
    private Integer openedBy;
    //创建日期
    private Date openedDate;
    //指派给
    private Integer assignedTo;
    //指派日期
    private Date assignedDate;
    //预计开始
    private Date estStarted;
    //实际开始
    private Date realStarted;
    //由谁完成
    private String finishedBy;
    //完成时间
    private Date finishedDate;
    //完成者列表
    private String finishedList;
    //由谁取消
    private String canceledBy;
    //取消时间
    private Date canceledDate;
    //由谁关闭
    private String closedBy;
    //关闭时间
    private Date closedDate;
    //关闭原因
    private String closedReason;
    //最后修改
    private String lastEditedBy;
    //最后修改日期
    private Date lastEditedDate;
    //已删除(0-否,1-是)
    private Boolean deleted;

}
