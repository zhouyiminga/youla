package com.zhou.util;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;

@Component
public class AliOssUtil {
    private static String endpoint;
    private static String accessKeyId;
    private static String accessKeySecret;
    private static String bucketName;

    @Value("${alioss.endpoint}")
    public void setEndpoint(String endpoint) {
        AliOssUtil.endpoint = endpoint;
    }

    @Value("${alioss.accessKeyId}")
    public void setAccessKeyId(String accessKeyId) {
        AliOssUtil.accessKeyId = accessKeyId;
    }

    @Value("${alioss.accessKeySecret}")
    public void setAccessKeySecret(String accessKeySecret) {
        AliOssUtil.accessKeySecret = accessKeySecret;
    }

    @Value("${alioss.bucketName}")
    public void setBucketName(String bucketName) {
        AliOssUtil.bucketName = bucketName;
    }
//static final OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

    public static void uploadimg(String objectName,byte[] img){
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            String content = "Hello OSS";
            ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(img));
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
}
