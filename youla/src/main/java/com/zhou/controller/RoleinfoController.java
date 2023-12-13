package com.zhou.controller;


import com.zhou.common.DefineParams;
import com.zhou.common.ResultVO;
import com.zhou.entity.MatchRoleMoreVO;
import com.zhou.entity.MatchRoleVO;
import com.zhou.entity.ReciveParams;
import com.zhou.entity.Roleinfo;
import com.zhou.service.IPicstoreService;
import com.zhou.service.IRoleinfoService;
import com.zhou.service.ISeqFolderService;
import com.zhou.util.CacheUtil;
import com.zhou.util.PictureUtils;
import com.zhou.util.YouLaParams;
import com.zhou.util.YouPaiYunUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.File;
import java.util.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zhouyiming
 * @since 2023-05-10
 */
@Slf4j
@RestController
@RequestMapping("//roleinfo")
public class RoleinfoController {
    @Autowired
    private IRoleinfoService roleinfoService;

    @Autowired
    private ISeqFolderService seqFolderService;

    @Autowired
    private IPicstoreService picstoreService;

    @CrossOrigin
    @RequestMapping("/list")
    public ResultVO listData(@Valid ReciveParams reciveParams) {
        ResultVO resultVO = new ResultVO();
        try {
            if (reciveParams != null) {
                MatchRoleMoreVO matchRoleMoreVO = new MatchRoleMoreVO();
                List<String> nameList = reciveParams.getNameList();
                if (nameList == null) throw new Exception("角色信息为空");
                YouLaParams.findRoleUseCountsIncrement();

                List<MatchRoleVO> matchRoleVOList = roleinfoService.listMatchRole(nameList, reciveParams.getSearchType(), reciveParams.getSelectArea());
                if (matchRoleVOList != null && matchRoleVOList.size() > 0) {
                    Comparator<MatchRoleVO> comparator = Comparator.comparing(MatchRoleVO::getMatchRate);
                    Collections.sort(matchRoleVOList, comparator.reversed());
                    String combinePicUrl = YouPaiYunUtil.generalCombinePic(matchRoleVOList);
                    matchRoleMoreVO.setPicUrl(YouLaParams.youPaiYunPre + combinePicUrl);
                }
                matchRoleMoreVO.setMatchRoleVOList(matchRoleVOList);
                resultVO.setResultCode("0000");
                resultVO.setResultData(matchRoleMoreVO);
            } else throw new Exception("获取参数为空");
        } catch (Exception e) {
            resultVO.setResultCode("0001");
            e.printStackTrace();
        }
        return resultVO;
    }

    /**
     * 根据图片下标数组获取图片Url
     *
     * @return
     */
    @CrossOrigin
    @RequestMapping("/findPicUrlByPicIndexArray")
    public ResultVO findPicUrlByPicIndexArray(@RequestBody ReciveParams reciveParams) {
        ResultVO resultVO = new ResultVO();
        try {
            if (reciveParams != null && reciveParams.getRoleIdList() != null) {
                List<Integer> roleIdList = reciveParams.getRoleIdList();
                String picUrl = roleinfoService.findPicUrlByPicIndexArray(roleIdList);
                resultVO.setResultCode("0000");
                resultVO.setResultData(YouLaParams.youPaiYunPre + picUrl);
            } else throw new Exception("获取参数为空");
        } catch (Exception e) {
            resultVO.setResultCode("0001");
            e.printStackTrace();
        }
        return resultVO;
    }

    /**
     * 保存用户信息
     *
     * @param reciveParams
     * @return
     */
    @CrossOrigin
    @Transactional
    @RequestMapping("/save")
    public ResultVO saveRoleinfo(ReciveParams reciveParams) {
        ResultVO resultVO = new ResultVO();
        try {
            String imgBase64Str = reciveParams.getImgBase64Str();
            if (imgBase64Str != null) {
                Roleinfo roleinfo = reciveParams.getRoleinfo();
                roleinfo.setImagePath("");
                roleinfoService.save(roleinfo);
                Integer id = roleinfo.getId();
                //System.out.println("瞅瞅保存成功的id:" + id);
                byte[] decode = Base64.getDecoder().decode(imgBase64Str);
                String fileName = UUID.randomUUID().toString() + "." + reciveParams.getFileExtension();
                Integer integer = seqFolderService.genralSeq();
                if (integer == null) throw new Exception("生成序列号报错了");
                //图片保存在云的格式 YouPaiYunUtil.roleInfoFolderName/{序列号}/UUID.{原来的图片后缀}
                String fullFileName = YouPaiYunUtil.roleInfoFolderName + "/" + integer + "/" + fileName;
                picstoreService.savePic(id, fullFileName, DefineParams.PICTYPE_ROLE);
                YouPaiYunUtil.uploadFile(decode, fullFileName);
                List<String> reasonImgBase64List = reciveParams.getReasonImgBase64List();
                List<String> reasonImgFileExtensionList = reciveParams.getReasonImgFileExtensionList();
                if (reasonImgBase64List == null || reasonImgFileExtensionList == null) throw new Exception("理由图片数据不对劲");
                for (int i = 0; i < reasonImgBase64List.size(); i++) {
                    fileName = UUID.randomUUID().toString() + "." + reasonImgFileExtensionList.get(i);
                    fullFileName = YouPaiYunUtil.reasonFolderName + "/" + integer + "/" + fileName;
                    decode = Base64.getDecoder().decode(reasonImgBase64List.get(i));
                    YouPaiYunUtil.uploadFile(decode, fullFileName);
                    picstoreService.savePic(id, fullFileName, DefineParams.PICTYPE_REASON);
                }
                CacheUtil.roleNameMap.put(id, roleinfo.getRoleName());
                CacheUtil.parentNameMap.put(id, roleinfo.getRoleParentName());
                CacheUtil.roleinfoMap.put(id, roleinfo);

                resultVO.setResultCode("0000");
                resultVO.setResultData("兄弟们666");
                return resultVO;
            }
        } catch (Exception e) {
            log.debug("报异常了", e);
            e.printStackTrace();
        }
        resultVO.setResultCode("0001");
        return resultVO;
    }
}
