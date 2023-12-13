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
 * @since 2023-05-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("picstore")
public class Picstore implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("roleid")
    private Integer roleid;

    @TableField("filePath")
    private String filePath;

    @TableField("pictype")
    private String pictype;

    public Picstore(Integer roleId, String filePath, String picType) {
        this.roleid = roleId;
        this.filePath = filePath;
        this.pictype = picType;
    }

    public static final String ID = "id";

    public static final String ROLEID = "roleid";

    public static final String FILEPATH = "filePath";

    public static final String PICTYPE = "pictype";

}
