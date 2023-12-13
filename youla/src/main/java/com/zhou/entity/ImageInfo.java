package com.zhou.entity;

import lombok.Data;

@Data
public class ImageInfo {
    private Integer[][][] rgbArray;
    private int height;
    private int width;
}
