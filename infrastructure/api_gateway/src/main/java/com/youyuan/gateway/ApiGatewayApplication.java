package com.youyuan.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 类名称：ApiGatewayApplication <br>
 * 类描述： 网关启动类 <br>
 *
 * @author zhangyu
 * @version 1.0.0
 * @date 创建时间：2021/9/20 21:58<br>
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class,args);
    }

}
