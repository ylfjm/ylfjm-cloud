package com.github.ylfjm.system.controller;

import com.github.ylfjm.common.YlfjmException;
import com.github.ylfjm.system.dto.InstanceInfoDTO;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.shared.Application;
import com.netflix.discovery.shared.Applications;
import com.netflix.discovery.shared.resolver.DefaultEndpoint;
import com.netflix.discovery.shared.transport.EurekaHttpClient;
import com.netflix.discovery.shared.transport.EurekaHttpResponse;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.eureka.http.RestTemplateTransportClientFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * 描述：TODO
 *
 * @Author Zhang Bo
 * @Date 2020/10/27
 */
@RestController
public class EurekaHttpClientController implements InitializingBean {

    @Value("${eureka.client.service-url.defaultZone}")
    private String endpoint;

    private EurekaHttpClient eurekaHttpClient;

    @GetMapping(value = "/getApplications")
    public Object getApplications() {
        List<InstanceInfoDTO> instanceInfoList = null;
        EurekaHttpResponse<Applications> applications = eurekaHttpClient.getApplications();
        Applications entity = applications.getEntity();
        if (entity != null) {
            List<Application> registeredApplications = entity.getRegisteredApplications();
            if (!CollectionUtils.isEmpty(registeredApplications)) {
                instanceInfoList = new ArrayList<>();
                for (Application application : registeredApplications) {
                    List<InstanceInfo> instances = application.getInstances();
                    if (!CollectionUtils.isEmpty(instances)) {
                        for (InstanceInfo instanceInfo : instances) {
                            instanceInfoList.add(new InstanceInfoDTO(
                                    instanceInfo.getIPAddr(),
                                    instanceInfo.getAppName(),
                                    instanceInfo.getInstanceId(),
                                    instanceInfo.getPort(),
                                    instanceInfo.getStatus().name(),
                                    instanceInfo.getLastUpdatedTimestamp(),
                                    instanceInfo.getLastDirtyTimestamp(),
                                    instanceInfo.getLeaseInfo().getRegistrationTimestamp(),
                                    instanceInfo.getLeaseInfo().getRenewalTimestamp(),
                                    instanceInfo.getLeaseInfo().getEvictionTimestamp(),
                                    instanceInfo.getLeaseInfo().getServiceUpTimestamp(),
                                    instanceInfo.getLeaseInfo().getRenewalIntervalInSecs(),
                                    instanceInfo.getLeaseInfo().getDurationInSecs()
                            ));
                        }
                    }
                }
            }
        }
        instanceInfoList.sort(new Comparator<InstanceInfoDTO>() {
            @Override
            public int compare(InstanceInfoDTO o1, InstanceInfoDTO o2) {
                if (o1.getAppName().compareTo(o2.getAppName()) == 0) {
                    return o1.getStatus().compareTo(o2.getStatus());
                } else {
                    return o1.getAppName().compareTo(o2.getAppName());
                }
            }
        });
        return instanceInfoList;
    }

    @PutMapping(value = "/statusUpdate")
    public void statusUpdate(@RequestParam String appName, @RequestParam String instanceId, @RequestParam String newStatus) {
        InstanceInfo instanceInfo = getInstanceInfo(instanceId);
        if (instanceInfo == null) {
            throw new YlfjmException("操作失败，未发现应用名为" + appName + "的实例");
        } else {
            eurekaHttpClient.statusUpdate(appName, instanceId, InstanceInfo.InstanceStatus.valueOf(newStatus), instanceInfo);
        }
    }

    private InstanceInfo getInstanceInfo(String instanceId) {
        EurekaHttpResponse<Applications> applications = eurekaHttpClient.getApplications();
        Applications entity = applications.getEntity();
        if (entity != null) {
            List<Application> registeredApplications = entity.getRegisteredApplications();
            if (!CollectionUtils.isEmpty(registeredApplications)) {
                for (Application application : registeredApplications) {
                    List<InstanceInfo> instances = application.getInstances();
                    if (!CollectionUtils.isEmpty(instances)) {
                        for (InstanceInfo instanceInfo : instances) {
                            if (Objects.equals(instanceInfo.getInstanceId(), instanceId)) {
                                return instanceInfo;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        RestTemplateTransportClientFactory restTemplateTransportClientFactory = new RestTemplateTransportClientFactory();
        eurekaHttpClient = restTemplateTransportClientFactory.newClient(new DefaultEndpoint(endpoint));
    }
}
