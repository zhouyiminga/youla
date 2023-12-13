package com.zhou.controller;

import com.zhou.common.ResultVO;
import com.zhou.service.ICacheService;
import com.zhou.util.CacheUtil;
import com.zhou.util.YouLaParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/cache")
@RestController
public class CacheController {

    @Autowired
    private ICacheService cacheService;

    /**
     * 刷新角色名称和冒险团名称缓存缓存
     * @return
     */
    @RequestMapping("/refreshCache")
    public ResultVO refreshCache(){
        ResultVO resultVO = new ResultVO();
        try {
            cacheService.refreshCache();
            resultVO.setResultCode("0000");
            resultVO.setResultMessage("更新缓存完毕");
        }catch (Exception e){
            resultVO.setResultCode("0001");
            resultVO.setResultMessage("更新缓存失败");
        }
        return resultVO;
    }

    @RequestMapping("/cleanPicCache")
    public ResultVO cleanTempPicCache(){
        ResultVO resultVO = new ResultVO();
        try {
            CacheUtil.tempPicMap.clear();
            resultVO.setResultCode("0000");
            resultVO.setResultMessage("清空缓存完毕");
        }catch (Exception e){
            resultVO.setResultCode("0001");
            resultVO.setResultMessage("清空缓存失败");
        }
        return resultVO;
    }

    /**
     * 展示当前累计接口被调用次数
     * @return
     */
    @RequestMapping("/showUserCounts")
    public ResultVO showUserCounts(){
        ResultVO resultVO = new ResultVO();
        try {
            StringBuilder stringBuilder=new StringBuilder();
            stringBuilder.append("ocr截至当前调用次数:").append(YouLaParams.ocrUseCountsGetValue()).append(",findRole截止当前调用次数:").append(YouLaParams.findRoleUseCountsGetValue());
            resultVO.setResultCode("0000");
            resultVO.setResultData(stringBuilder);
        }catch (Exception e){
            resultVO.setResultCode("0001");
            resultVO.setResultMessage("返回当前累计接口被调用次数方法异常");
        }
        return resultVO;
    }
}
