package com.zhou.service;

import com.zhou.entity.SeqFolder;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhouyiming
 * @since 2023-05-03
 */
public interface ISeqFolderService extends IService<SeqFolder> {

    /**
     * 生成序列号代码
     * @return
     */
    Integer genralSeq();
}
