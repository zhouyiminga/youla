package com.zhou.entity;

import lombok.Data;

import java.util.List;

@Data
public class ReturnMatchVO {

    /**
     * 图片路径
     */
    private String imgUrl;

    /**
     *
     */
    private List<MatchRoleVO> matchRoleVOList;
}
