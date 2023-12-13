package com.zhou.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置首页
 */
@Configuration
public class MyMvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 注册欢迎页（如果有）
        registry.addViewController("/").setViewName("index.html");
        registry.addViewController("/index").setViewName("index.html");
    }

}
