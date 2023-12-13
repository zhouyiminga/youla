package com.zhou.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhou.entity.SeqFolder;
import com.zhou.mapper.SeqFolderMapper;
import com.zhou.service.ISeqFolderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zhouyiming
 * @since 2023-05-03
 */
@Service
public class SeqFolderServiceImpl extends ServiceImpl<SeqFolderMapper, SeqFolder> implements ISeqFolderService {

    @Resource
    private SeqFolderMapper seqFolderMapper;

    @Override
    public Integer genralSeq() {
        SeqFolder seqFolder = new SeqFolder("");
        seqFolderMapper.insert(seqFolder);
        return seqFolder.getSeq();
    }
}
