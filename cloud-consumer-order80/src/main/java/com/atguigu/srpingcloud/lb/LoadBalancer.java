package com.atguigu.srpingcloud.lb;

import org.springframework.cloud.client.ServiceInstance;

import java.util.List;

public interface LoadBalancer {
    ServiceInstance intances(List<ServiceInstance>serviceInstances);
}
