package com.zhou.entity;

import lombok.Data;

@Data
public class YouPaiYunCall {
    private String task_id;
    private String service;
    private Integer status_code;
    private String path;
    private String error;
}
