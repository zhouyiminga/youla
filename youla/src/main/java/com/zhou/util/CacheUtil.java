package com.zhou.util;

import com.zhou.entity.Roleinfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CacheUtil {
    /**
     * 角色名称缓存
     */
    public static Map<Integer, String> roleNameMap = new HashMap<>();

    /**
     * 冒险团名称缓存
     */
    public static Map<Integer, String> parentNameMap = new HashMap<>();

    /**
     * 角色信息缓存
     */
    public static Map<Integer, Roleinfo> roleinfoMap = new HashMap<>();

    /**
     * 角色名称图片路径缓存
     */
    public static Map<Integer, String> roleNamePicMap = new HashMap<>();

    /**
     * 原因图片路径缓存
     */
    public static Map<Integer, List<String>> reasonPicMap = new HashMap<>();

    /**
     * 合成图片临时缓存（随时清空的）
     */
    public static Map<String, String> tempPicMap = new HashMap<>();
}
