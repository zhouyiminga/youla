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
 * @since 2023-05-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("seq_folder")
public class SeqFolder implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "seq", type = IdType.AUTO)
    private Integer seq;

    @TableField("nullValue")
    private String nullValue;

    public SeqFolder(String nullValue) {
        this.nullValue = nullValue;
    }

    public static final String SEQ = "seq";

    public static final String NULLVALUE = "nullValue";

}
