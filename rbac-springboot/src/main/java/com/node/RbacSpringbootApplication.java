package com.node;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.node.mapper")
public class RbacSpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(RbacSpringbootApplication.class, args);
    }

}
