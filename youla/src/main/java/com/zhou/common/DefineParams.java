package com.zhou.common;

public class DefineParams {
    public final static String PICTYPE_ROLE="role";
    public final static String PICTYPE_REASON="reason";
    public final static String RedisTempPic="tempPic:";
    public final static String RedisPicPath="picPath:";
    /**
     * redis中记录ocr接口被正常访问次数的key
     */
    public final static String REDIS_OCR_USECOUNTS="ocrusecounts";

    /**
     * redis中记录查询角色接口被正常访问次数的key
     */
    public final static String REDIS_FINDROLE_USECOUNTS="findroleusecounts";
}
