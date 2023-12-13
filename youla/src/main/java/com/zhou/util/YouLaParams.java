package com.zhou.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class YouLaParams {

    /**
     * 项目路径
     */
    public static String youlaurl;

    /**
     * 图片前缀路径
     */
    public static String imgPrePath;

    /**
     * 又拍云图片路径前半截
     */
    public static String youPaiYunPre;

    /**
     * 记录ocr接口被正常调用次数
     */
    public static AtomicInteger ocrUseCounts = new AtomicInteger(0);

    /**
     * 每次调用ocr接口，ocrUseCounts+1
     */
    public static void ocrUseCountsIncrement() {
        ocrUseCounts.incrementAndGet();
    }

    public static int ocrUseCountsGetValue() {
        return ocrUseCounts.get();
    }

    /**
     * 记录查询角色接口被正常调用次数
     */
    public static AtomicInteger findRoleUseCounts = new AtomicInteger(0);

    /**
     * 每次调用查找角色接口，findRoleUseCounts+1
     */
    public static void findRoleUseCountsIncrement() {
        findRoleUseCounts.incrementAndGet();
    }

    public static int findRoleUseCountsGetValue() {
        return findRoleUseCounts.get();
    }


    @Value("${youla.imgPrePath}")
    public void setImgPrePath(String imgPrePath) {
        YouLaParams.imgPrePath = imgPrePath;
    }

    @Value("${youla.youlaurl}")
    public void setYoulaurl(String youlaurl) {
        this.youlaurl = youlaurl;
    }

    @Value("${youla.youPaiYunPre}")
    public void setYouPaiYunPre(String youPaiYunPre) {
        YouLaParams.youPaiYunPre = youPaiYunPre;
    }
}
