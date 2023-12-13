package com.zhou.util;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

public class GeneralJsonUtil {

    /**
     * 生成json对象，懒得搞一堆对应的类了
     * @param imgHeight
     * @param imgWidth
     * @param rgbArray
     * @return
     */
    public static JSONObject generateJson(int imgHeight, int imgWidth, Integer[][][] rgbArray) {
        JSONObject jsonObject = new JSONObject();
        JSONArray inputArray = new JSONArray();
        JSONObject inputObject = new JSONObject();
        JSONArray shapeArray = new JSONArray();
        shapeArray.add(1);
        shapeArray.add(imgHeight);
        shapeArray.add(imgWidth);
        shapeArray.add(3);
        JSONArray dataArray=new JSONArray();
        JSONArray datajsonArray = JSONArray.parseArray(JSONArray.toJSONString(rgbArray));
        dataArray.add(datajsonArray);
        inputObject.put("name", "INPUT");
        inputObject.put("shape", shapeArray);
        inputObject.put("datatype", "UINT8");
        inputObject.put("data", dataArray);
        inputArray.add(inputObject);
        jsonObject.put("inputs", inputArray);

        JSONArray outputArray = new JSONArray();
        JSONObject outputObject1 = new JSONObject();
        outputObject1.put("name", "rec_texts");
//        JSONObject outputObject2 = new JSONObject();
//        outputObject2.put("name", "rec_scores");
        outputArray.add(outputObject1);
//        outputArray.add(outputObject2);
        jsonObject.put("outputs", outputArray);

        return jsonObject;
    }
}
