package com.zhou.common;

import com.zhou.service.ICacheService;
import com.zhou.util.YouLaParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class StartUpInit implements ApplicationRunner {

    @Autowired
    private ICacheService cacheService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("开始执行启动后初始化操作");
        //初始化缓存
        try {
            cacheService.refreshCache();
        } catch (Exception e) {
            //System.out.println("项目启动初始化缓存失败");
            log.error("项目启动初始化缓存失败");
            e.printStackTrace();
        }
        try {
            Integer redisNowOcrUserCounts = (Integer) redisTemplate.opsForValue().get(DefineParams.REDIS_OCR_USECOUNTS);
            Integer redisNowFindRoleUserCounts = (Integer) redisTemplate.opsForValue().get(DefineParams.REDIS_FINDROLE_USECOUNTS);
            if (redisNowOcrUserCounts == null || redisNowFindRoleUserCounts == null) {
                //System.out.println("项目启动初始化接口查询接口调用次数数据异常:redisNowOcrUserCounts:" + redisNowOcrUserCounts + ",:redisNowFindRoleUserCounts:" + redisNowFindRoleUserCounts);
                log.error("项目启动初始化接口查询接口调用次数数据异常:redisNowOcrUserCounts:{},:redisNowFindRoleUserCounts:{}",redisNowOcrUserCounts,redisNowFindRoleUserCounts);
                return;
            }
            YouLaParams.ocrUseCounts = new AtomicInteger(redisNowOcrUserCounts);
            YouLaParams.findRoleUseCounts = new AtomicInteger(redisNowFindRoleUserCounts);
        } catch (Exception e) {
            //System.out.println("项目启动初始化接口调用次数参数异常");
            log.error("项目启动初始化接口调用次数参数异常");
            e.printStackTrace();
        }
    }
}
