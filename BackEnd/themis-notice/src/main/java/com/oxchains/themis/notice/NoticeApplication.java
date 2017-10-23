package com.oxchains.themis.notice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by Luo_xuri on 2017/10/20.
 */
@SpringBootApplication
@EnableScheduling
public class NoticeApplication {
    public static void main(String[] args){
        SpringApplication.run(NoticeApplication.class, args);
    }
}
