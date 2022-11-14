package com.zhoufu.springbootqrcode.controller;

import com.zhoufu.springbootqrcode.util.BarCodeUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

/**
 * @ClassName : BarCodeController
 * @Author : 15153
 * @Date: 2022/11/14 15:09
 * @Description : 条形码处理类
 */
@Controller
@RequestMapping("/barCode-home")
public class BarCodeController {

    /**
     *  生成条形码
     * @param msg  条形码文字内容
     * @param request
     * @param response
     */
    @GetMapping("/barcode")
    public void barCode(String msg,HttpServletRequest request, HttpServletResponse response) {
        try {
            OutputStream os = response.getOutputStream();
            BarCodeUtil.generateBarCode128(msg,10.0,0.5,true, false, os);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
