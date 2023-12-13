package com.zhou.entity;

import lombok.Data;

@Data
public class MatchRoleVO {

    /**
     * 跨区信息
     */
    private String bigArea;

    /**
     * 小区
     */
    private String littleArea;

    /**
     * 职业
     */
    private String roleType;

    /**
     * 匹配第几栏位
     */
    private Integer matchRow;

    /**
     * 匹配度
     */
    private Double matchRate;

    /**
     * 匹配类型，0角色还是1冒险团名称
     */
    private Integer matchType;

    /**
     * 合成大图Url
     */
    private String bigPicUrl;

    /**
     * 角色id
     */
    private Integer roleId;

    /**
     * 匹配几率
     */
    private String matchRateStr;

    /**
     * 记录原因
     */
    private String reason;
}
