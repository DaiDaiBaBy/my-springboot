package com.zhoufu.springbootexcel.controller;

import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.zhoufu.springbootexcel.listener.ExcelListener;
import com.zhoufu.springbootexcel.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Author: zhoufu
 * @Date: 2021/2/1 17:07
 * @description:  接口导入到处
 */
@RestController
public class ExcelController {
    @Autowired
    private GoodsService goodsService;

    /**
     * 导出Excel
     */
    @RequestMapping(value = "/exportExcel", method = RequestMethod.GET)
    public void exportExcel(HttpServletResponse response) throws Exception {
        goodsService.excelExport(response);
    }

    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public String importExcel(@RequestParam(value = "file") MultipartFile file) throws IOException {
        goodsService.excelImport(file);
        return "success";
    }
}
