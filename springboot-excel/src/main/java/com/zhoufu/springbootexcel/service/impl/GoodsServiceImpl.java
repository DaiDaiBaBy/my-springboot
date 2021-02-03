package com.zhoufu.springbootexcel.service.impl;

import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.zhoufu.springbootexcel.listener.ExcelListener;
import com.zhoufu.springbootexcel.mapper.GoodsMapper;
import com.zhoufu.springbootexcel.model.Goods;
import com.zhoufu.springbootexcel.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @Author: zhoufu
 * @Date: 2021/2/2 9:48
 * @description:  实现层
 */
@Service
public class GoodsServiceImpl implements GoodsService{
    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public void excelExport(HttpServletResponse response) throws Exception {
        List<Goods> list = goodsMapper.queryAllGoods();
        String fileName = "商品信息表";
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1") + ".xls");
        ServletOutputStream out = response.getOutputStream();
        ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLS, true);
        Sheet sheet = new Sheet(1, 0, Goods.class);
        // 设置自适应宽度
        sheet.setAutoWidth(Boolean.TRUE);
        sheet.setSheetName("商品信息表");
        writer.write(list, sheet);
        writer.finish();
        out.flush();
        response.getOutputStream().close();
        out.close();
    }

    @Override
    public void excelImport(MultipartFile file) throws IOException {
        if (!file.getOriginalFilename().equals("商品信息表.xls") && !file.getOriginalFilename().equals("商品信息表.xlsx")){
            throw new RuntimeException("没找到文件");
        }
        InputStream inputStream = new BufferedInputStream(file.getInputStream());
        //实例化实现了AnalysisEventListener接口的类
        ExcelListener listener = new ExcelListener(goodsMapper);
        ExcelReader reader = new ExcelReader(inputStream, null, listener);
        // 读取信息
        reader.read(new Sheet(1, 1, Goods.class));
    }
}
