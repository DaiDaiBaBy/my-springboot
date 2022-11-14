package com.zhoufu.springbootqrcode.util;

import org.krysalis.barcode4j.HumanReadablePlacement;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;

import java.awt.image.BufferedImage;
import java.io.*;

/**
 * @ClassName : BarCodeUtil
 * @Author : 15153
 * @Date: 2022/11/14 15:07
 * @Description : 条形码工具类
 */
public class BarCodeUtil {

    /**
     * 生成条形码文件
     * @param msg  条形码的文本内容
     * @param path 生成条形码的文件路径
     * @return
     */
    public static File generateFile(String msg, String path) {
        File file = new File(path);
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            generateBarCode128(msg, 10.0, 0.3, true, false, outputStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return file;
    }

    /**
     * 生成code128条形码
     * @param message       要生成的文本
     * @param height        条形码的高度
     * @param width         条形码的宽度
     * @param withQuietZone 是否两边留白
     * @param hideText      隐藏可读文本
     * @param outputStream  输出流
     */
    public static void generateBarCode128(String message, Double height, Double width, boolean withQuietZone, boolean hideText, OutputStream outputStream) {
        Code128Bean bean = new Code128Bean();

        // 分辨率，越大条形码就越大
        int dpi = 150;

        // 设置两侧是否留白
        bean.doQuietZone(withQuietZone);

        // 设置条形码高度和宽度
        bean.setBarHeight(height);
        if (width != null) {
            bean.setModuleWidth(width);
        }
        // 设置文本位置（包括是否显示）
        if (hideText) {
            bean.setMsgPosition(HumanReadablePlacement.HRP_NONE);
        }
        // 设置图片类型
        String format = "image/png";
        BitmapCanvasProvider canvas = new BitmapCanvasProvider(outputStream, format, dpi,
                BufferedImage.TYPE_BYTE_BINARY, false, 0);

        /* 生产条形码 */
        bean.generateBarcode(canvas, message);
        try {
            canvas.finish();
        } catch (IOException e) {
            //ByteArrayOutputStream won't happen
        }
    }

//    public static void main(String[] args) {
//        String msg = "202211141516";
//        //生成条形码路径
//        String path = "D:\\workspace\\gitResponsity\\my-springboot\\springboot-qrcode\\src\\test\\barcode1.png";
//        generateFile(msg, path);
//    }
}
