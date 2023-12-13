package com.zhou.entity;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import java.util.List;

@Data
public class ReciveParams {

    /**
     * 图片二进制数据
     */
    private byte[] img;

    /**
     * 图片base64格式
     */
    @Length(max = 420000)
    private String imgBase64Str;

    /**
     * 图片格式/后缀
     */
    private String fileExtension;

    /**
     * 理由图片集合
     */
    private List<String> reasonImgBase64List;

    /**
     * 理由图片的后缀集合
     */
    private List<String> reasonImgFileExtensionList;

    /**
     * 处理方式
     */
    private int dealType;

    /**
     * 收到的角色信息
     */
    private Roleinfo roleinfo;

    /**
     * 图片的rgb三维数组数据，java获取太慢，让前端处理好过来
     */
    private ImageInfo imageInfo;

    /**
     * 查询名字名单
     */
    private List<@Length( max = 18,message = "角色名字长度过长") String> nameList;

    /**
     * 查询方式0全部 1只查角色 2只查冒险团
     */
    private int searchType;

    /**
     * 选择跨区
     */
    private String selectArea;

    /**
     * 角色ID列表
     */
    private List<Integer> roleIdList;

    /**
     * 单个roleId
     */
    private Integer roleId;
}
