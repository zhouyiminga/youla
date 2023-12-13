package com.zhou.controller;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.zhou.common.ResultVO;
import com.zhou.entity.FastDeployJson;
import com.zhou.entity.ReciveParams;
import com.zhou.util.GeneralJsonUtil;
import com.zhou.util.OkHttp;
import com.zhou.util.YouLaParams;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/dealImg")
public class DealImgController {

    @CrossOrigin
    @RequestMapping("/dealImg")
    public ResultVO dealReciveImg(ReciveParams params) throws IOException {
        ResultVO resultVO = new ResultVO();
        if (params.getImageInfo() != null) {
            YouLaParams.ocrUseCountsIncrement();
            JSONObject jsonObject = GeneralJsonUtil.generateJson(params.getImageInfo().getHeight(), params.getImageInfo().getWidth(), params.getImageInfo().getRgbArray());
            String s = OkHttp.fastDeployConnect(jsonObject.toJSONString());
            //System.out.println("s:" + s);
            if (!"".equals(s)) {
                JSONObject parse = JSONObject.parseObject(s);
                List<String> nameList = new ArrayList<>();
                JSONArray outputs = parse.getJSONArray("outputs");
                for (int i = 0; i < outputs.size(); i++) {
                    JSONObject jsonObject1 = outputs.getJSONObject(i);
                    String string = jsonObject1.getString("name");
                    if ("rec_texts".equals(string)) {
                        JSONArray jsonArray1 = jsonObject1.getJSONArray("data");
                        for (int j = 0; j < jsonArray1.size(); j++) {
                            String string1 = jsonArray1.getString(j);
                            nameList.add(string1);
                        }
                        resultVO.setResultCode("0000");
                        resultVO.setResultData(nameList);
                        return resultVO;
                    }
                }
            }
        } else {
            System.out.println("图片数据为空");
            resultVO.setResultMessage("没接收到图片数据");
        }
        resultVO.setResultCode("0001");
        return resultVO;
    }

    /**
     * 旧方法，调用的知乎大佬给的dokcer，不方便拓展修改 zhouyiming 20230508
     *
     * @param params
     * @return
     */
    @CrossOrigin
    @RequestMapping("/oldDealImg")
    public ResultVO testReciveImg(ReciveParams params) {
        ResultVO resultVO = new ResultVO();
        if (params.getImgBase64Str() != null) {
            System.out.println("好！");
            byte[] decode = Base64.getDecoder().decode(params.getImgBase64Str());
            //PictureUtils.byte2file(decode,"H:"+File.separator+"学习"+File.separator+"临时垃圾文件夹"+File.separator+"test.png");
            String s = OkHttp.localConnect(decode);
            if (!"".equals(s)) {
//                String fileName= UUID.randomUUID().toString()+params.getImgType();
//                AliOssUtil.uploadimg(fileName,decode);
                JSONObject parse = JSONObject.parseObject(s);
                //JSONObject meta = parse.getJSONObject("meta");
                List<String> nameList = new ArrayList<>();
                JSONArray meta1 = parse.getJSONArray("meta");
                for (int i = 0; i < meta1.size(); i++) {
                    JSONArray jsonArray = meta1.getJSONArray(i);
                    JSONArray jsonArray1 = jsonArray.getJSONArray(1);
                    String string = jsonArray1.getString(0);
                    System.out.println(string);
                    nameList.add(string);
                }
                resultVO.setResultCode("0000");
                resultVO.setResultData(nameList);
                return resultVO;
            }
        } else {
            System.out.println("日!");
            resultVO.setResultMessage("没接收到图片数据");
        }
        resultVO.setResultCode("0001");
        return resultVO;
    }

    private void testSpeed2(ReciveParams params) throws IOException {
        long startTime = System.currentTimeMillis();
        FastDeployJson fastDeployJson = new FastDeployJson(params.getImageInfo().getHeight(), params.getImageInfo().getWidth(), params.getImageInfo().getRgbArray(), "rec_texts");
        String fastJsonStr =  JSONObject.toJSONString(fastDeployJson);
        //System.out.println(s);
        // 要记录执行时间的代码块
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        System.out.println("代码块执行时间：" + elapsedTime + "毫秒");
        String s = OkHttp.fastDeployConnect(fastJsonStr);

        long finalendTime = System.currentTimeMillis();
        elapsedTime = finalendTime - startTime;
        System.out.println("111代码块执行时间：" + elapsedTime + "毫秒");
        System.out.println(s);
    }

    private void testSpeed(ReciveParams params) throws IOException {
        long startTime = System.currentTimeMillis();
        JSONObject jsonObject = GeneralJsonUtil.generateJson(params.getImageInfo().getHeight(), params.getImageInfo().getWidth(), params.getImageInfo().getRgbArray());
        // 要记录执行时间的代码块
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        System.out.println("代码块执行时间：" + elapsedTime + "毫秒");
        String s = OkHttp.fastDeployConnect(jsonObject.toJSONString());

        long finalendTime = System.currentTimeMillis();
        elapsedTime = finalendTime - startTime;
        System.out.println("111代码块执行时间：" + elapsedTime + "毫秒");
        System.out.println(s);
    }


    /**
     * 下载图片再返回给前端，看gpt大佬说oss服务提供更便利的方式，晚点看看
     *
     * @param id
     * @return
     */
    @RequestMapping("/image")
    public ResponseEntity<byte[]> downloadImage(String id) {
        System.out.println("id:" + id);
        try {
            URL imageUrl = new URL("https://jichouyoula.oss-cn-beijing.aliyuncs.com/" + id); // 图片的URL
            HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
            connection.setRequestMethod("HEAD");
            connection.connect();
            String contentType = connection.getContentType(); // 获取图片的 Content-Type
            System.out.println("看看contenttype:" + contentType);
            byte[] imageBytes = IOUtils.toByteArray(imageUrl);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.valueOf(contentType));
            headers.setContentLength(imageBytes.length);
            return new ResponseEntity<byte[]>(imageBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
