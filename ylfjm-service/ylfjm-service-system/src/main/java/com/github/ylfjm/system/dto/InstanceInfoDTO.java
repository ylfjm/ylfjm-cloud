package com.github.ylfjm.system.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 描述：微服务实例信息
 *
 * @Author Zhang Bo
 * @Date 2020/10/28
 */
@Getter
@Setter
public class InstanceInfoDTO {

    //本实例的ip地址
    private String ipAddr;
    //端口号
    private Integer port;
    //应用名。如ACCOUNT（同一应用可以有N多个实例
    private String appName;
    //实例id。在同一个应用appName的范围内是必须是惟一的
    private String instanceId;
    //实例状态。默认值是InstanceStatus.UP，是个枚举
    //starting：实例初始化状态，此状态主要给实例预留初始化时间
    //down：当健康检查失败时，实例的状态转变到down
    //up：正常服务状态
    //out_of_service：不参与接收服务 。但是服务正常
    //unknown：未知状态
    private String status;
    //上次修改时间
    private Date lastUpdatedDate;
    //上次标记为Dirty的时间
    private Date lastDirtyDate;
    //租约的注册时间
    private Date registrationDate;
    //最近一次的续约时间（服务端记录，用于倒计时的起始值）
    private Date lastRenewalDate;
    //下线时间（服务的上、下线属于比较频繁的操作。但是此时服务实例并未T除去）
    private Date evictionDate;
    //上线时间
    private Date serviceUpDate;
    //续租间隔时间（多长时间续约一次），默认是30s。
    //用于Client客户端：每隔30s上报续约一次
    private int renewalIntervalInSecs;
    //续约持续时间（过期时间），默认是90s。90s倒计时，期间没有收到续约就会执行对应动作
    //用于Server服务端，90s内木有收到心跳，就T除掉对应实例
    private int durationInSecs;

    public InstanceInfoDTO(String ipAddr,
                           String appName,
                           String instanceId,
                           int port,
                           String status,
                           Long lastUpdatedTimestamp,
                           Long lastDirtyTimestamp,
                           long registrationTimestamp,
                           long lastRenewalTimestamp,
                           long evictionTimestamp,
                           long serviceUpTimestamp,
                           int renewalIntervalInSecs,
                           int durationInSecs) {
        this.ipAddr = ipAddr;
        this.appName = appName;
        this.instanceId = instanceId;
        this.port = port;
        this.status = status;
        if (lastUpdatedTimestamp != null) {
            this.lastUpdatedDate = new Date(lastUpdatedTimestamp);
        }
        if (lastDirtyTimestamp != null) {
            this.lastDirtyDate = new Date(lastDirtyTimestamp);
        }
        this.registrationDate = new Date(registrationTimestamp);
        this.lastRenewalDate = new Date(lastRenewalTimestamp);
        this.evictionDate = new Date(evictionTimestamp);
        this.serviceUpDate = new Date(serviceUpTimestamp);
        this.renewalIntervalInSecs = renewalIntervalInSecs;
        this.durationInSecs = durationInSecs;
    }

}
