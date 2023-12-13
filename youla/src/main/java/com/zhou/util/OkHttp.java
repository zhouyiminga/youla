package com.zhou.util;

import com.zhou.config.BaiduConfig;
import okhttp3.*;
import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Component
public class OkHttp {

    static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();
    public static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("text/x-markdown; charset=utf-8");
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private static final MediaType FROM_DATA = MediaType.parse("multipart/form-data");

    //不同环境fastdeploy地址不同
    private static String staticFastDeployUrl;
    @Value("${fastdeployhost}")
    private String FastDeployUrl;

    @PostConstruct
    private void init() {
        staticFastDeployUrl = FastDeployUrl;
        System.out.println("当前staticFastDeployUrl值为:" + staticFastDeployUrl);
    }

    /**
     * 连接新方式部署的ocr服务
     *
     * @param jsonParamStr
     * @return
     */
    public static String fastDeployConnect(String jsonParamStr) throws IOException {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, jsonParamStr);

        Request request = new Request.Builder()
                .url("http://" + staticFastDeployUrl + ":8000/v2/models/pp_ocr/versions/1/infer")
                .post(body)
                .build();

        try (Response response = HTTP_CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String str = response.body().string();
                System.out.println(str);
                String exMessage = response.code() == 400 ? "服务识别不了此图片内容，请尝试手动输入" : "文字识别出现异常";
                throw new IOException(exMessage);
            }
            String str = response.body().string();
            //System.out.println(str);
//            JSONObject jsonObject=JSONObject.parseObject(str);
//            if(jsonObject!=null){
//
//            }

            return str;
        } catch (IOException e) {
            throw e;
        }
    }


    public static String localConnect(byte[] file) {
        RequestBody requestBody = new RequestBody() {
            public MediaType contentType() {
                return MEDIA_TYPE_PNG;
            }

            public long contentLength() {
                return file.length;
            }

            public void writeTo(BufferedSink sink) throws IOException {
                Source source = null;

                try {
                    source = Okio.source(new ByteArrayInputStream(file));
                    sink.writeAll(source);
                } finally {
                    Util.closeQuietly(source);
                }

            }
        };

        File file1 = new File("C:" + File.separator + "Users" + File.separator + "76963" + File.separator + "Desktop" + File.separator + "QZ@MIMGKOP)HB{8VV@1]3LG.png");

        Request request = new Request.Builder()
                .addHeader("Content-Type", "charset=utf-8")
                .url("http://192.168.2.6:6370/OCR")
                .post(new MultipartBody.Builder()
                        .setType(FROM_DATA)
                        .addFormDataPart("pic", "test", requestBody)
                        //.addFormDataPart("pic","test",RequestBody.create(MEDIA_TYPE_PNG, file1))
                        .build())
                .build();

        try (Response response = HTTP_CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            String str = response.body().string();
            System.out.println(str);
//            JSONObject jsonObject=JSONObject.parseObject(str);
//            if(jsonObject!=null){
//
//            }
            return str;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String okConnect(String imgBase64) {
        String encode = "";
        try {
            //zhou 20230305 base64还得url编码再传给百度，不然会报图片不对
            encode = URLEncoder.encode(imgBase64, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "image=" + encode);
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/rest/2.0/ocr/v1/general_basic?access_token=" + BaiduConfig.ACCESSTOKEN)
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Accept", "application/json")
                .build();
        try {
            Response response = HTTP_CLIENT.newCall(request).execute();
            String reciveStr = response.body().string();
            System.out.println(reciveStr);
            return reciveStr;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
}
