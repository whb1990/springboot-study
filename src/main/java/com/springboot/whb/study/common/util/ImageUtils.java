package com.springboot.whb.study.common.util;

import com.mortennobel.imagescaling.ResampleOp;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

/**
 * 图片工具类
 *
 * @author: whb
 * @description:实现图片转base64流，base64流转图片；图片压缩
 * @Date 9:10 2018/3/29
 */
public class ImageUtils {
    /**
     * @param imgURL 网络资源位置
     * @return Base64字符串
     * @Title: GetImageStrFromUrl
     * @Description: 将一张网络图片转化成Base64字符串
     */
    public static String getImageStrFromUrl(String imgURL) {
        byte[] data = null;
        try {
            // 创建URL
            URL url = new URL(imgURL);
            // 创建链接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            InputStream inStream = conn.getInputStream();
            data = readInputStream(inStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        // 返回Base64编码过的字节数组字符串
        return encoder.encode(data);
    }

    /**
     * @param imagePath 本地图片路径
     * @return Base64字符串
     * @Title: getImageStrFromPath
     * @Description: 将一张本地图片转化成Base64字符串
     */
    public static String getImageStrFromPath(String imagePath) {
        InputStream in = null;
        byte[] data = null;
        //读取图片字节数组
        try {
            in = new FileInputStream(imagePath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        // 返回Base64编码过的字节数组字符串
        return encoder.encode(data);
    }

    /**
     * @param imgStr      base64字符串
     * @param imgFilePath 图片生成路径
     * @return Base64字符串
     * @Title: generateImage
     * @Description: 将base64字符串转化成图片
     */
    public static boolean generateImage(String imgStr, String imgFilePath) {
        if (imgStr == null) {
            return false;
        }
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            byte[] b = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < b.length; i++) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @param inputStream 文件流
     * @return 字节数组
     * @Title: readInputStream
     * @Description: 将文件流转成字节数组
     */
    public static byte[] readInputStream(InputStream inputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        try {
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.toByteArray();
    }

    /**
     * 接收输入流输生成图片
     *
     * @param input--输入流
     * @param writePath--图片生成路径
     * @param width--宽度
     * @param height--高度
     * @param format--图片格式
     * @return
     */
    public boolean resizeImage(InputStream input, String writePath, Integer width, Integer height, String format) {
        try {
            BufferedImage inputBufImage = ImageIO.read(input);
            //System.err.println("转前图片高度和宽度：" + inputBufImage.getHeight() + ":" + inputBufImage.getWidth());
            if (inputBufImage.getWidth() > 0 && inputBufImage.getWidth() < width) {
                width = inputBufImage.getWidth();
            }
            if (inputBufImage.getHeight() > 0 && inputBufImage.getHeight() < height) {
                height = inputBufImage.getHeight();
            }
            // 转换
            ResampleOp resampleOp = new ResampleOp(width, height);
            BufferedImage rescaledTomato = resampleOp.filter(inputBufImage, null);
            ImageIO.write(rescaledTomato, format, new File(writePath));
            //System.err.println("转后图片高度和宽度：" + rescaledTomato.getHeight() + ":" + rescaledTomato.getWidth());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 接收File输出图片
     *
     * @param file--图片文件
     * @param writePath--图片生成路径
     * @param width-宽度
     * @param height--高度
     * @param format--图片格式
     * @return
     */
    public boolean resizeImage(File file, String writePath, Integer width, Integer height, String format) {
        try {
            BufferedImage inputBufImage = ImageIO.read(file);
            inputBufImage.getType();
            //System.err.println("转前图片高度和宽度：" + inputBufImage.getHeight() + ":" + inputBufImage.getWidth());
            if (inputBufImage.getWidth() > 0 && inputBufImage.getWidth() < width) {
                width = inputBufImage.getWidth();
            }
            if (inputBufImage.getHeight() > 0 && inputBufImage.getHeight() < height) {
                height = inputBufImage.getHeight();
            }
            // 转换
            ResampleOp resampleOp = new ResampleOp(width, height);
            BufferedImage rescaledTomato = resampleOp.filter(inputBufImage, null);
            ImageIO.write(rescaledTomato, format, new File(writePath));
            //System.err.println("转后图片高度和宽度：" + rescaledTomato.getHeight() + ":" + rescaledTomato.getWidth());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 接收字节数组生成图片
     *
     * @param RGBS--图片字节数组
     * @param writePath--图片生成路径
     * @param width--宽度
     * @param height--高度
     * @param format--图片格式
     * @return
     */
    public boolean resizeImage(byte[] RGBS, String writePath, Integer width, Integer height, String format) {
        InputStream input = new ByteArrayInputStream(RGBS);
        return this.resizeImage(input, writePath, width, height, format);
    }

    public byte[] readBytesFromIS(InputStream is) throws IOException {
        int total = is.available();
        byte[] bs = new byte[total];
        is.read(bs);
        return bs;
    }


    public static void main(String[] args) {
        System.out.println("------------begin读取图片," + new Date().toLocaleString() + "------------");
        String localImgStr = getImageStrFromPath("F:\\20180327115414211.jpg");
        String remoteImgStr = getImageStrFromUrl("http://10.42.0.198/UploadFiles/images/2018/3/20180327115414211.jpg");
        generateImage(remoteImgStr, "F:\\tmpImag.jpeg");
        System.out.println("图片流：" + remoteImgStr);
        System.err.println("==========end读取图片，" + new Date().toLocaleString() + "==========");

        System.err.println("---------开始压缩图片" + new Date().toLocaleString() + "----------");
        ImageUtils myImage = new ImageUtils();
        int width = 900;
        int height = 600;
        File inputFile = new File("F://a (45).jpg");
        File outFile = new File("F://myImage.jpg");
        String outPath = outFile.getAbsolutePath();
        myImage.resizeImage(inputFile, outPath, width, height, "jpg");

        // InputStream input = new FileInputStream(inputFile);
        // byte[] byteArrayImage = myImage.readBytesFromIS(input);
        // input.read(byteArrayImage);
        // myImage.resizeImage(byteArrayImage, outPath, width, height, "jpg");
        System.err.println("-------------------结束：" + new Date().toLocaleString() + "---------------------");
    }
}
