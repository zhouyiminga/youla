package com.zhou.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BaiduConfig {

    public static String ACCESSTOKEN;

    @Value("${baidu.accessToken}")
    public void setAccessToken(String accessToken) {
        ACCESSTOKEN = accessToken;
    }
}
