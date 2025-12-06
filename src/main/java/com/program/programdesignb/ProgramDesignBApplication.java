package com.program.programdesignb;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Spring Boot 启动入口
@SpringBootApplication
@MapperScan("com.program.programdesignb.mapper")
public class ProgramDesignBApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProgramDesignBApplication.class, args);
    }

}
