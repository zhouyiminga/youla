package com.zhou;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@MapperScan(basePackages = {"com.zhou.mapper"})
@SpringBootApplication
@EnableScheduling
@EnableAsync
public class YouLaMain {

    public static void main(String[] args) {
        SpringApplication.run(YouLaMain.class, args);
    }
}
