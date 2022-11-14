package com.zhoufu.springbootqrcode.controller;

import com.zhoufu.springbootqrcode.util.QrCodeUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

/**
 * @ClassName : QRCodeController
 * @Author : 15153
 * @Date: 2022/11/14 14:53
 * @Description : 二维码处理类
 */
@Controller
@RequestMapping("/qrcode-home")
public class QRCodeController {

    /**
     * 生成带logo的二维码到response
     * @param request
     * @param response
     */
    @RequestMapping("/qrcode")
    public void qrcode(HttpServletRequest request, HttpServletResponse response) {
        String requestUrl = "http://www.baidu.com";
        try {
            OutputStream os = response.getOutputStream();
            QrCodeUtil.encode(requestUrl, "/data/springboot2/logo.jpg", os);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成不带logo的二维码到response
     * @param request
     * @param response
     */
    @RequestMapping("/qrnologo")
    public void qrnologo(HttpServletRequest request, HttpServletResponse response) {
        String requestUrl = "http://www.baidu.com";
        try {
            OutputStream os = response.getOutputStream();
            QrCodeUtil.encode(requestUrl, null, os);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 把二维码保存成文件
     * @return
     */
    @RequestMapping("/qrsave")
    @ResponseBody
    public String qrsave() {
        String requestUrl = "http://www.baidu.com";
        try {
            QrCodeUtil.save(requestUrl, "/data/springboot2/logo.jpg", "/data/springboot2/qrcode2.jpg");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "文件已保存";
    }

    /**
     * 解析二维码中的文字
     * @return
     */
    @RequestMapping("/qrtext")
    @ResponseBody
    public String qrtext() {
        //String requestUrl = "http://www.baidu.com";
        String url = "";
        try {
            url = QrCodeUtil.decode("/data/springboot2/qrcode2.jpg");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "解析到的url:"+url;
    }
}
