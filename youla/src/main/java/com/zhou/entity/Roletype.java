package com.zhou.entity;

import com.baomidou.mybatisplus.annotation.TableName;
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
 * @since 2023-03-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("roletype")
public class Roletype implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("id")
    private Integer id;

    @TableField("typeName")
    private String typeName;


    public static final String ID = "id";

    public static final String TYPENAME = "typeName";

}
