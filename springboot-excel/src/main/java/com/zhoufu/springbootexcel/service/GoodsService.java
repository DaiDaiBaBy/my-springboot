package com.zhoufu.springbootexcel.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @Author: zhoufu
 * @Date: 2021/2/2 9:46
 * @description: 商品服务
 */
public interface GoodsService {
    /**
     *  导出 接口
     * @param response
     */
    void excelExport(HttpServletResponse response) throws Exception;

    /**
     *  导入接口
     * @param file
     */
    void excelImport(MultipartFile file) throws IOException;
}
