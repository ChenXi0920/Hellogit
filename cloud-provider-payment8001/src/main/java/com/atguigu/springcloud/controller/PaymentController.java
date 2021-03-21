package com.atguigu.springcloud.controller;


import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import com.atguigu.springcloud.service.PaymentService;



import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Controller
@Slf4j
public class PaymentController {
    @Autowired
    private PaymentService paymentService;
    @Value("${server.port}")
    private String serverPort;
    @Resource
    private DiscoveryClient discoveryClient;

    @PostMapping(value = "/payment/create")
    @ResponseBody
    public CommonResult create(@RequestBody Payment payment){
        int result=paymentService.create(payment);
        log.info("~~~~插入结果:"+result);
        if (result>0){
            return new CommonResult(200,"插入数据库成功,serverPort="+serverPort,result);
        }else {
            return new CommonResult(444,"插入失败,serverPort="+serverPort,null);
        }
    }
    @GetMapping(value = "/payment/get/{id}")
    @ResponseBody
    public CommonResult getPaymentById(@PathVariable("id") Long id){
        Payment payment=paymentService.getPaymentById(id);
        log.info("~~~~查询结果:"+payment);
        if (payment!=null){
            return new CommonResult(200,"查询成功哈哈,serverPort="+serverPort,payment);
        }else {
            return new CommonResult(444,"没有对应的记录,serverPort="+serverPort,null);
        }
    }

    @GetMapping(value = "/payment/discovery")
    @ResponseBody
    public Object discovery(){
        List<String> services = discoveryClient.getServices();
        for (String element : services){
            log.info("~~~~~~~~~~~~element:"+element);
        }
        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        for (ServiceInstance instance : instances){
            log.info(instance.getServiceId()+"\t"+instance.getHost()+"\t"+instance.getPort()+"\t"+instance.getUri());
        }
        return this.discoveryClient;
    }
    @GetMapping(value = "/payment/lb")
    @ResponseBody
    public String getPaymentLB(){
        return serverPort;
    }

    //演示提供方超时
    @GetMapping(value = "/payment/feign/timeout")
    @ResponseBody
    public String paymentTimeout(){
        try {
            TimeUnit.SECONDS.sleep(3);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return serverPort;
    }
}
