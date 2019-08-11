package com.yifeng.springboot.sprintbootdemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.yifeng.springboot.sprintbootdemo.dao")
public class SprintbootDemoApplication {

    public static void main(String[] args) {
        System.out.println("starting Spring Boot...");
        SpringApplication.run(SprintbootDemoApplication.class, args);
    }

}
