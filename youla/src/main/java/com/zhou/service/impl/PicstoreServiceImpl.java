package com.zhou.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhou.entity.Picstore;
import com.zhou.mapper.PicstoreMapper;
import com.zhou.service.IPicstoreService;
import com.zhou.util.CacheUtil;
import com.zhou.util.YouLaParams;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zhouyiming
 * @since 2023-05-12
 */
@Service
public class PicstoreServiceImpl extends ServiceImpl<PicstoreMapper, Picstore> implements IPicstoreService {

    @Resource
    private PicstoreMapper picstoreMapper;

    @Override
    public void savePic(Integer roleId, String filePath, String picType) throws Exception {
        Picstore picstore = new Picstore(roleId, filePath, picType);
        try {
            picstoreMapper.insert(picstore);
        } catch (Exception e) {
            throw new Exception("插入图片记录报错了.roleId:" + roleId + ",filePath:" + filePath);
        }
    }

    @Override
    public List<String> findReasonImgByRoleId(Integer roleId) throws Exception {
        List<String> reasonImgList = new ArrayList<>();
        try {
//            List<Picstore> picstores = picstoreMapper.selectList(new QueryWrapper<Picstore>().eq("roleId", roleId).eq("pictype", DefineParams.PICTYPE_REASON));
//            if (picstores != null) {
//                for (Picstore picstore : picstores) {
//                    reasonImgList.add(YouLaParams.youPaiYunPre + "/" + picstore.getFilePath());
//                }
//            }
            reasonImgList = CacheUtil.reasonPicMap.get(roleId);
            reasonImgList = reasonImgList.stream().map(s -> YouLaParams.youPaiYunPre + "/" + s).collect(Collectors.toList());
        } catch (Exception e) {
            throw new Exception("获取证据图片异常");
        }
        return reasonImgList;
    }
}
