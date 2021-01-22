package com.springcloud.controller;



import com.springcloud.entites.CommonResult;
import com.springcloud.entites.Payment;
import com.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class PaymentController {

    @Resource
    private PaymentService paymentService;

    @Value("${server.port}")
    private String serverPort;

    //用于发现和获取服务信息，获取到已经注册成功服务得 (ip，服务名称，端口号)
    @Resource
    private DiscoveryClient discoveryClient;

    @PostMapping(value = "/payment/create")
    public CommonResult create(@RequestBody Payment payment) {
        int result = paymentService.create(payment);
        System.out.println("插入结果:" + result);
        if (result > 0) {
            return new CommonResult(200, "插入成功,serverPort: " + serverPort, result);
        } else {
            return new CommonResult(444, "插入失败", null);
        }
    }

    @GetMapping(value = "/payment/get/{id}")
    public CommonResult getPaymentById(@PathVariable("id") Long id) {
        Payment paymentById = paymentService.getPaymentById(id);
        System.out.println("查询结果1:" + paymentById);
        if (paymentById != null) {
            return new CommonResult(200, "查询成功,serverPort: "+ serverPort, paymentById);
        } else {
            return new CommonResult(444, "没有对应记录，查询id:" + id, null);
        }
    }


    @GetMapping(value = "/payment/discovery")
    public Object discovery(){
        List<String> services = discoveryClient.getServices();
        for (String element : services) {
            System.out.println("****element: " +element);
        }
        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        for (ServiceInstance instance:instances) {
            System.out.println(instance.getServiceId()+"\t"+instance.getHost()+"\t"+instance.getPort()+"\t"+instance.getUri());
        }
        return this.discoveryClient;
    }


    @GetMapping(value = "/payment/lb")
    public String getPaymentLB(){
        return serverPort;
    }


    @GetMapping("/payment/feign/timeout")
    public String paymentFeignTimeOut(){
        try {
            TimeUnit.SECONDS.sleep(3);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return serverPort;
    }


    @GetMapping("/payment/zipkin")
    public String paymentZipkin(){
        return "hi,i'am paymentzipkin server fall back, welcome to";
    }
}
