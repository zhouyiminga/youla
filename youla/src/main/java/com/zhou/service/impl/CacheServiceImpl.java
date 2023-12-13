package com.zhou.service.impl;

import com.zhou.common.DefineParams;
import com.zhou.entity.Picstore;
import com.zhou.entity.Roleinfo;
import com.zhou.service.ICacheService;
import com.zhou.service.IPicstoreService;
import com.zhou.service.IRoleinfoService;
import com.zhou.util.CacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class CacheServiceImpl implements ICacheService {

    @Autowired
    private IRoleinfoService roleinfoService;

    @Autowired
    private IPicstoreService picstoreService;

    @Override
    public void refreshCache() {
        HashMap<Integer, String> newRoleNameMap = new HashMap<>();
        HashMap<Integer, String> newParentNameMap = new HashMap<>();
        HashMap<Integer, Roleinfo> newRoleInfoMap = new HashMap<>();
        HashMap<Integer, String> newRoleNamePicMap = new HashMap<>();
        HashMap<Integer, List<String>> newReasonPicMap = new HashMap<>();
        List<Roleinfo> list = roleinfoService.list();
        for (Roleinfo roleinfo : list) {
            newRoleNameMap.put(roleinfo.getId(), roleinfo.getRoleName());
            newParentNameMap.put(roleinfo.getId(), roleinfo.getRoleParentName());
            newRoleInfoMap.put(roleinfo.getId(), roleinfo);
        }
        //获取角色图片路径信息
        List<Picstore> list1 = picstoreService.list();
        for (Picstore picstore : list1) {
            if (DefineParams.PICTYPE_ROLE.equals(picstore.getPictype())) {
                newRoleNamePicMap.put(picstore.getRoleid(), picstore.getFilePath());
            } else {
                List<String> reasonPicPathList = newReasonPicMap.get(picstore.getRoleid());
                if (reasonPicPathList == null) reasonPicPathList = new ArrayList<>(3);
                reasonPicPathList.add(picstore.getFilePath());
                newReasonPicMap.put(picstore.getRoleid(), reasonPicPathList);
            }
        }
        synchronized (CacheUtil.roleinfoMap) {
            CacheUtil.roleNameMap = newRoleNameMap;
            CacheUtil.parentNameMap = newParentNameMap;
            CacheUtil.roleinfoMap = newRoleInfoMap;
            CacheUtil.roleNamePicMap = newRoleNamePicMap;
            CacheUtil.reasonPicMap = newReasonPicMap;
        }
    }
}
