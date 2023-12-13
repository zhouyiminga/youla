package com.zhou.util;

import com.zhou.entity.ImageInfo;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
//java将图片的url转换成File，File转换成二进制流byte
public class PictureUtils {
    //将Url转换为File
    public static File UrltoFile(String url) throws Exception {
        HttpURLConnection httpUrl = (HttpURLConnection) new URL(url).openConnection();
        httpUrl.connect();
        InputStream ins=httpUrl.getInputStream();
        File file = new File(System.getProperty("java.io.tmpdir") + File.separator + "xie");//System.getProperty("java.io.tmpdir")缓存
        if (file.exists()) {
            file.delete();//如果缓存中存在该文件就删除
        }
        OutputStream os = new FileOutputStream(file);
        int bytesRead;
        int len = 8192;
        byte[] buffer = new byte[len];
        while ((bytesRead = ins.read(buffer, 0, len)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        os.close();
        ins.close();
        return file;

    }
    //将File对象转换为byte[]的形式
    public static byte[] FileTobyte(File file){
        FileInputStream fileInputStream = null;
        byte[] imgData = null;

        try {

            imgData = new byte[(int) file.length()];

            //read file into bytes[]
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(imgData);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return imgData;
    }

    /**
     * File转byte[]数组
     *
     * @param fileFullPath
     * @return
     */
    public static byte[] file2byte(String fileFullPath) {
        if (fileFullPath == null || "".equals(fileFullPath)) {
            return null;
        }
        return file2byte(new File(fileFullPath));
    }

    /**
     * File转byte[]数组
     *
     * @param file
     * @return
     */
    public static byte[] file2byte(File file) {
        if (file == null) {
            return null;
        }
        FileInputStream fileInputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fileInputStream.read(b)) != -1) {
                byteArrayOutputStream.write(b, 0 , n);
            }
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * byte[]数组转File
     *
     * @param bytes
     * @param fileFullPath
     * @return
     */
    public static File byte2file(byte[] bytes, String fileFullPath) {
        if (bytes == null) {
            return null;
        }
        FileOutputStream fileOutputStream = null;
        try {
            File file = new File(fileFullPath);
            //判断文件是否存在
            if (file.exists()) {
                file.mkdirs();
            }
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bytes);
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                }  catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 返回图片的RGB三维数组
     * @param path 图片路径
     * @return
     * @throws IOException
     */
    public static int[][][] readImagePath(String path) throws IOException {
        BufferedImage image = ImageIO.read(new File(path));
        int height = image.getHeight();
        int width = image.getWidth();
        System.out.println(height+","+width);
        int[][][] rgbArray = new int[height][width][3];
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int pixel = image.getRGB(col, row);
                rgbArray[row][col][0] = (pixel >> 16) & 0xff; // R
                rgbArray[row][col][1] = (pixel >> 8) & 0xff; // G
                rgbArray[row][col][2] = pixel & 0xff; // B
            }
        }
        return rgbArray;
    }

    /**
     * 返回图片的RGB三维数组
     * @param file 入参Byte数组
     * @return
     * @throws IOException
     */
    public static ImageInfo readImageByte(byte[] file) throws IOException {
        ImageInfo imageInfo=new ImageInfo();
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(file));
        int imgHeight = image.getHeight();
        int imgWidth = image.getWidth();
        //System.out.println(imgHeight+","+imgWidth);
        Integer[][][] rgbArray = new Integer[imgHeight][imgWidth][3];
        for (int row = 0; row < imgHeight; row++) {
            for (int col = 0; col < imgWidth; col++) {
                int pixel = image.getRGB(col, row);
                rgbArray[row][col][0] = (pixel >> 16) & 0xff; // R
                rgbArray[row][col][1] = (pixel >> 8) & 0xff; // G
                rgbArray[row][col][2] = pixel & 0xff; // B
            }
        }
        imageInfo.setHeight(imgHeight);
        imageInfo.setWidth(imgWidth);
        imageInfo.setRgbArray(rgbArray);
        return imageInfo;
    }
}
