package com.example.backendtemplate;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.example.backendtemplate.Mapper")
@SpringBootApplication
public class BackendTemplateApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendTemplateApplication.class, args);
    }

}
