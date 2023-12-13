package com.zhou.controller;

import com.zhou.common.DefineParams;
import com.zhou.entity.YouPaiYunCall;
import com.zhou.util.CacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * 处理回调方法
 */
@RestController
@RequestMapping("/call")
public class DealCallRequestController {

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("/youpaiyuncall")
    public void dealYouPaiYunCall(@RequestBody YouPaiYunCall youPaiYunCall) {
        System.out.println("接收到回调");
        if (youPaiYunCall == null || youPaiYunCall.getStatus_code() == null) {
            System.out.println("毛都没还调用我是吧");
        } else {
            if (youPaiYunCall.getStatus_code() == 200) {
                System.out.println(LocalDateTime.now() +"成功啦,图片存储路径:" + youPaiYunCall.getPath());
                //String s = CacheUtil.tempPicMap.get(youPaiYunCall.getPath());
                //CacheUtil.tempPicMap.put(s, youPaiYunCall.getPath());
                try {
                    String s = redisTemplate.opsForValue().get(DefineParams.RedisTempPic + youPaiYunCall.getPath()) + "";
                    //System.out.printf("testRedis111:" + s);
                    redisTemplate.opsForValue().set(s, youPaiYunCall.getPath());
                    redisTemplate.delete(DefineParams.RedisTempPic + youPaiYunCall.getPath());
                } catch (Exception e) {
                    System.out.printf("操作redis发生异常", e);
                    e.printStackTrace();
                }
            } else {
                System.out.println("失败了:" + youPaiYunCall.getError());
            }
        }
    }
}
