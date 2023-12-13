package com.zhou.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhouyiming
 * @since 2023-05-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("roleinfo")
public class Roleinfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("roleName")
    private String roleName;

    @TableField("roleParentName")
    private String roleParentName;

    @TableField("roleType")
    private String roleType;

    @TableField("imagePath")
    private String imagePath;

    @TableField("reason")
    private String reason;

    @TableField("area")
    private String area;

    @TableField("bigArea")
    private String bigArea;

    @TableField("littleArea")
    private String littleArea;

    @TableField("memo")
    private String memo;


    public static final String ID = "id";

    public static final String ROLENAME = "roleName";

    public static final String ROLEPARENTNAME = "roleParentName";

    public static final String ROLETYPE = "roleType";

    public static final String IMAGEPATH = "imagePath";

    public static final String REASON = "reason";

    public static final String AREA = "area";

    public static final String BIGAREA = "bigArea";

    public static final String LITTLEAREA = "littleArea";

    public static final String MEMO = "memo";

}
