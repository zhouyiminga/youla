package com.zhou.service;

import com.upyun.UpException;
import com.zhou.entity.MatchRoleVO;
import com.zhou.entity.Roleinfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhouyiming
 * @since 2023-05-10
 */
public interface IRoleinfoService extends IService<Roleinfo> {

    /**
     * 根据传参获取匹配的角色信息
     * @param nameList
     * @param searchType
     * @param selectArea
     * @return
     */
    List<MatchRoleVO> listMatchRole(List<String> nameList, int searchType, String selectArea);

    /**
     * 根据roleIdList获取合成图片路径
     * @param nameList
     * @return
     * @throws IOException
     * @throws UpException
     */
    String findPicUrlByPicIndexArray(List<Integer> nameList) throws IOException, UpException;

}
