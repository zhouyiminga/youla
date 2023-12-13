package com.zhou.service;

import com.zhou.entity.Picstore;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhouyiming
 * @since 2023-05-12
 */
public interface IPicstoreService extends IService<Picstore> {

    /**
     * 保存图片
     * @param roleId
     * @param filePath
     * @param picType
     */
    void savePic(Integer roleId ,String filePath,String picType) throws Exception;

    /**
     * 根据roleId获取证据图片
     * @param roleId
     * @return
     * @throws Exception
     */
    List<String> findReasonImgByRoleId(Integer roleId) throws Exception;
}
