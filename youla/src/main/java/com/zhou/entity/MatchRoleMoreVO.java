package com.zhou.entity;

import lombok.Data;

import java.util.List;

@Data
public class MatchRoleMoreVO {
    /**
     * 当前第一页的图片url
     */
    private String picUrl;

    /**
     * 匹配角色信息
     */
    private List<MatchRoleVO> matchRoleVOList;
}
