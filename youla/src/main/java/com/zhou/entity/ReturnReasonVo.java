package com.zhou.entity;

import lombok.Data;

import java.util.List;

@Data
public class ReturnReasonVo {

    /**
     * roleId,再塞回去备用
     */
    private Integer roleId;

    /**
     * 备注
     */
    private String memo;

    /**
     * 证据图片集合
     */
    private List<String> reasonImgList;
}
