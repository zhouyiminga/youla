package com.zhou.controller;


import com.zhou.common.ResultVO;
import com.zhou.entity.ReciveParams;
import com.zhou.entity.ReturnReasonVo;
import com.zhou.service.IPicstoreService;
import com.zhou.util.CacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zhouyiming
 * @since 2023-05-12
 */
@RestController
@RequestMapping("//picstore")
public class PicstoreController {

    @Autowired
    private IPicstoreService picstoreService;

    /**
     * 根据roleId获取证据图片
     *
     * @return
     */
    @CrossOrigin
    @RequestMapping("/findReasonImgByRoleId")
    public ResultVO findReasonImgByRoleId(ReciveParams reciveParams) {
        ResultVO resultVO = new ResultVO();
        try {
            Integer roleId = reciveParams.getRoleId();
            if (reciveParams == null || roleId == null) throw new Exception("获取参数为空");
            ReturnReasonVo returnReasonVo = new ReturnReasonVo();
            String memoStr = CacheUtil.roleinfoMap.get(roleId).getMemo();
            List<String> reasonImgList = picstoreService.findReasonImgByRoleId(roleId);
            returnReasonVo.setMemo(memoStr);
            returnReasonVo.setReasonImgList(reasonImgList);
            resultVO.setResultCode("0000");
            resultVO.setResultData(returnReasonVo);
        } catch (Exception e) {
            resultVO.setResultCode("0001");
            e.printStackTrace();
        }
        return resultVO;
    }
}
