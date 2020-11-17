package com.github.ylfjm.system.po;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 描述：开发任务实体类
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
    //任务名称
    private String name;
    //任务类型
    private String type;
    //优先级
    private Integer pri;
    //截止日期
    private Date deadline;
    //任务状态：'wait','doing','done','pause','cancel','closed'
    private String status;
    //任务描述
    private String content;
    //由谁创建
    private String openedBy;
    //创建日期
    private Date openedDate;
    //由谁完成
    private String finishedBy;
    //完成时间
    private Date finishedDate;
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
    //是否需要UI设计
    private Boolean uiRequired;
    //UI设计者
    private String uiDesigner;
    //UI预计完成日期
    private Date uiEstimateDate;
    //UI实际完成日期
    private Date uiFinishedDate;
    //是否需要web端开发
    private Boolean webRequired;
    //web端开发
    private String webDeveloper;
    //web端预计完成日期
    private Date webEstimateDate;
    //web端实际完成日期
    private Date webFinishedDate;
    //是否需要安卓端开发
    private Boolean androidRequired;
    //安卓端开发
    private String androidDeveloper;
    //安卓端预计完成日期
    private Date androidEstimateDate;
    //安卓端实际完成日期
    private Date androidFinishedDate;
    //是否需要苹果端开发
    private Boolean iosRequired;
    //苹果端开发
    private String iosDeveloper;
    //苹果端预计完成日期
    private Date iosEstimateDate;
    //苹果端实际完成日期
    private Date iosFinishedDate;
    //是否需要后端开发
    private Boolean serverRequired;
    //后端开发
    private String serverDeveloper;
    //后端预计完成日期
    private Date serverEstimateDate;
    //后端实际完成日期
    private Date serverFinishedDate;
    //已删除(0-否,1-是)
    private Boolean deleted;

}
