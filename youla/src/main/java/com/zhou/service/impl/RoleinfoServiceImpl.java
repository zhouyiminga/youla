package com.zhou.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.upyun.UpException;
import com.zhou.entity.MatchRoleVO;
import com.zhou.entity.Roleinfo;
import com.zhou.mapper.RoleinfoMapper;
import com.zhou.service.IRoleinfoService;
import com.zhou.util.CacheUtil;
import com.zhou.util.DealNameAlg;
import com.zhou.util.YouLaParams;
import com.zhou.util.YouPaiYunUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zhouyiming
 * @since 2023-05-10
 */
@Service
public class RoleinfoServiceImpl extends ServiceImpl<RoleinfoMapper, Roleinfo> implements IRoleinfoService {

    @Resource
    private RoleinfoMapper roleinfoMapper;

    @Override
    public List<MatchRoleVO> listMatchRole(List<String> nameList, int searchType, String selectArea) {
        //不检查标志 如果是全部后续就别每次都问一遍是不是等于全部了
        boolean noCheckFlag = false;
        if ("全部".equals(selectArea)) noCheckFlag = true;
        List<MatchRoleVO> matchRoleVOList = new ArrayList<>();

        for (int i = 0; i < nameList.size(); i++) {

            String s = nameList.get(i);

            if (s == null || "".equals(s.trim())) continue;
            if (searchType == 0 || searchType == 1) {
                getMap(selectArea, noCheckFlag, matchRoleVOList, CacheUtil.roleNameMap, i, s, 0);
            }
            if (searchType == 0 || searchType == 2) {
                getMap(selectArea, noCheckFlag, matchRoleVOList, CacheUtil.parentNameMap, i, s, 1);
            }
            //打算给长度为1的特殊符号字符串做一些无脑匹配，但是想想还是没必要。
            /**
            if(s!=null && s.length()==1&& ifSpecialStr(s)){
            }
            **/
        }
        return matchRoleVOList;
    }

    private void getMap(String selectArea, boolean noCheckFlag, List<MatchRoleVO> matchRoleVOList, Map<Integer, String> tempRoleNameMap, int i, String s, int i2) {
        for (Integer integer : tempRoleNameMap.keySet()) {
            Roleinfo roleinfo = CacheUtil.roleinfoMap.get(integer);
            if (noCheckFlag || selectArea.equals(roleinfo.getArea())) {
                String s1 = tempRoleNameMap.get(integer);
                int distance = DealNameAlg.getLevenshteinDistance(s, s1);
                double similarity = 1 - ((double) distance / Math.max(s1.length(), s.length()));
                if (similarity >= 0.34d) {
                    //添加到返回列表
                    //String matchDetail = s + " 匹配度: " + similarity * 100 + "%";
                    //System.out.println(matchDetail);
                    commonExceat(matchRoleVOList, CacheUtil.roleNamePicMap, i, integer, roleinfo, similarity, i2);
                }
            }
        }
    }

    private void commonExceat(List<MatchRoleVO> matchRoleVOList, Map<Integer, String> tempRoleNamePicMap, int i, Integer integer, Roleinfo roleinfo, double similarity, int i2) {
        MatchRoleVO matchRoleVO = new MatchRoleVO();
        matchRoleVO.setMatchRate(similarity);
        matchRoleVO.setBigArea(roleinfo.getArea());
        matchRoleVO.setLittleArea(roleinfo.getLittleArea());
        matchRoleVO.setRoleType(roleinfo.getRoleType());
        matchRoleVO.setMatchRow((i + 1));
        matchRoleVO.setMatchType(i2);
        matchRoleVO.setReason(roleinfo.getReason());
        matchRoleVO.setBigPicUrl(YouLaParams.youPaiYunPre + "/" + tempRoleNamePicMap.get(integer));
        matchRoleVO.setRoleId(integer);
        matchRoleVOList.add(matchRoleVO);
    }

    /**
     * 检测这个长度为1的字符串是否为特殊字符
     * @param s
     * @return
     */
    private boolean ifSpecialStr(String s) {
        return Pattern.matches("^[^A-Za-z0-9\\u4e00-\\u9fa5]$", s);
    }

    /**
     * 拼接合成图片url方法
     *
     * @param roleIdList
     * @return
     * @throws IOException
     * @throws UpException
     */
    @Override
    public String findPicUrlByPicIndexArray(List<Integer> roleIdList) throws IOException, UpException {
        String picUrl = "";
        String indexStr = "";
        for (Integer s : roleIdList) {
            indexStr += s;
        }
        if (!"".equals(indexStr)) picUrl = YouPaiYunUtil.generalCombinePicByRoldidList(roleIdList);
        return picUrl;
    }

}
