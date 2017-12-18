package com.oxchains.themis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import zipkin.server.EnableZipkinServer;

/**
 * @author luoxuri
 * @create 2017-12-15 13:41
 **/
@SpringBootApplication
@EnableZipkinServer
@EnableEurekaClient
public class SleuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(SleuthApplication.class, args);
    }
}
