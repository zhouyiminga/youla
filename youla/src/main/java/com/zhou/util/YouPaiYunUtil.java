package com.zhou.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.upyun.*;
import com.zhou.common.DefineParams;
import com.zhou.entity.MatchRoleVO;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class YouPaiYunUtil {
    public static String roleInfoFolderName = "roleinfo";
    public static String reasonFolderName = "reason";

    @Value("${youpaiyun.bucketName}")
    private String youpaiyunBucketName;

    @Value("${youpaiyun.username}")
    private String youpaiyunUsername;

    @Value("${youpaiyun.password}")
    private String youpaiyunPassword;
    public static RestManager manager;
    public static JigsawHandler handle;

    private static RedisTemplate redisTemplate;

    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Value("${youpaiyuncallhost}")
    private String youpaiyuncallhost;

    private static String staticyoupaiyuncallhost;

    @PostConstruct
    private void init() {
        staticyoupaiyuncallhost = youpaiyuncallhost;
        System.out.println("当前staticyoupaiyuncallhost值为:" + staticyoupaiyuncallhost);

        manager = new RestManager(youpaiyunBucketName, youpaiyunUsername, youpaiyunPassword);
        handle = new JigsawHandler(youpaiyunBucketName, youpaiyunUsername, youpaiyunPassword);
    }


    public static void uploadFile(byte[] file, String filePath) throws IOException, UpException {
        // 例1：上传纯文本内容，自动创建父级目录
        String str = "Hello RestManager";
        Map<String, String> params = new HashMap<String, String>();
        // 设置待上传文件的 Content-MD5 值
        // 如果又拍云服务端收到的文件MD5值与用户设置的不一致，将回报 406 NotAcceptable 错误
        //params.put(RestManager.PARAMS.CONTENT_MD5.getValue(), UpYunUtils.md5(file, 1024));

        // 设置待上传文件的"访问密钥"
        // 注意：
        // 仅支持图片空！，设置密钥后，无法根据原文件URL直接访问，需带URL后面加上（缩略图间隔标志符+密钥）进行访问
        // 举例：
        // 如果缩略图间隔标志符为"!"，密钥为"bac"，上传文件路径为"/folder/test.jpg"，
        // 那么该图片的对外访问地址为：http://空间域名 /folder/test.jpg!bac
        //params.put(RestManager.PARAMS.CONTENT_SECRET.getValue(), "bac");
        Response result = manager.writeFile(filePath, file, null);
    }

    /**
     * 合成图片并且返回合成图片的位置
     *
     * @param matchRoleVOList
     * @return
     * @throws IOException
     * @throws UpException
     */
    public static String generalCombinePic(List<MatchRoleVO> matchRoleVOList) throws IOException, UpException {
        String combinePicName = null;
        if (matchRoleVOList != null) {
            //缓存区域
            //String tempStr = "";
            StringBuilder tempSb = new StringBuilder("");
            for (MatchRoleVO matchRoleVO : matchRoleVOList) {
                tempSb.append("|").append(matchRoleVO.getRoleId());
                //tempStr += matchRoleVO.getRoleId();
            }
            // redis接管hashmap zhou 20230726
            //String s = CacheUtil.tempPicMap.get(tempStr);
            String s = redisTemplate.opsForValue().get(tempSb.toString()) + "";
            //System.out.printf("?!:" + s);
            if (s != null && !"null".equals(s)) return s;

            int size = matchRoleVOList.size() >= 10 ? 10 : matchRoleVOList.size();
            //初始化参数组 Map
            Map<String, Object> paramsMap = new HashMap<String, Object>();

            paramsMap.put(JigsawHandler.Params.BUCKET_NAME, "jichou");
            paramsMap.put(JigsawHandler.Params.NOTIFY_URL, staticyoupaiyuncallhost + "/call/youpaiyuncall");
            paramsMap.put(JigsawHandler.Params.APP_NAME, "jigsaw");
            //已json格式生成任务信息
            JSONArray array = new JSONArray();
            JSONObject json = new JSONObject();

            String[][] pigs = new String[size][1];
            for (int i = 0; i < size; i++) {
                Integer roleId = matchRoleVOList.get(i).getRoleId();
                pigs[i][0] = CacheUtil.roleNamePicMap.get(roleId);
            }

            combinePicName = "/combine" + "/" + UUID.randomUUID().toString() + ".png";

            //添加处理参数
            json.put(JigsawHandler.Params.IMAGE_MATRIX, pigs);
            json.put(JigsawHandler.Params.SAVE_AS, combinePicName);

            array.add(json);

            //添加任务信息
            paramsMap.put(JigsawHandler.Params.TASKS, array);

            Result process = handle.process(paramsMap);
            if (process.isSucceed()) {
                System.out.println(LocalDateTime.now() + "111发送图片合并请求成功，本次任务ID:" + process.getMsg() + ",图片保存路径为:" + combinePicName);
                //CacheUtil.tempPicMap.put(combinePicName, tempStr);
                redisTemplate.opsForValue().set(DefineParams.RedisTempPic + combinePicName, tempSb.toString());
            } else {
                System.out.println(process.getMsg());
            }
        }
        return combinePicName;
    }

    /**
     * 只需要传roleIdlist的版本
     *
     * @param roleIdList
     * @return
     * @throws IOException
     * @throws UpException
     */
    public static String generalCombinePicByRoldidList(List<Integer> roleIdList) throws IOException, UpException {
        String combinePicName = null;
        if (roleIdList != null) {
            //缓存区域
            //String tempStr = "";
            StringBuilder tempSb = new StringBuilder("");
            for (Integer integer : roleIdList) {
                tempSb.append("|").append(integer);
                //tempStr += integer;
            }
            //String s = CacheUtil.tempPicMap.get(tempStr);
            String s = redisTemplate.opsForValue().get(tempSb.toString()) + "";
            //System.out.printf("?:" + s);
            if (s != null && !"null".equals(s)) return s;

            int size = roleIdList.size();
            //初始化参数组 Map
            Map<String, Object> paramsMap = new HashMap<String, Object>();

            paramsMap.put(JigsawHandler.Params.BUCKET_NAME, "jichou");
            paramsMap.put(JigsawHandler.Params.NOTIFY_URL, staticyoupaiyuncallhost + "/call/youpaiyuncall");
            paramsMap.put(JigsawHandler.Params.APP_NAME, "jigsaw");
            //已json格式生成任务信息
            JSONArray array = new JSONArray();
            JSONObject json = new JSONObject();

            String[][] pigs = new String[size][1];
            for (int i = 0; i < size; i++) {
                pigs[i][0] = CacheUtil.roleNamePicMap.get(roleIdList.get(i));
            }

            combinePicName = "/combine" + "/" + UUID.randomUUID().toString() + ".png";

            //添加处理参数
            json.put(JigsawHandler.Params.IMAGE_MATRIX, pigs);
            json.put(JigsawHandler.Params.SAVE_AS, combinePicName);

            array.add(json);

            //添加任务信息
            paramsMap.put(JigsawHandler.Params.TASKS, array);

            Result process = handle.process(paramsMap);
            if (process.isSucceed()) {
                System.out.println(LocalDateTime.now() + "222发送图片合并请求成功，本次任务ID:" + process.getMsg() + ",图片保存路径为:" + combinePicName);
                //CacheUtil.tempPicMap.put(combinePicName, tempStr);
                redisTemplate.opsForValue().set(DefineParams.RedisTempPic + combinePicName, tempSb.toString());
            } else {
                System.out.println(process.getMsg());
            }
        }
        return combinePicName;
    }
}
