package com.zhoufu.springbootexcel.generator;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
/**
 * @Author: zhoufu
 * @Date: 2021/02/01 22:59
 * @description:
 */

/**
 * 运行此方法生成mybatis代码
 * 生成代码自动放入对应目录
 * 配置文件targetProject应从项目名称开始到要生成到的classpath
 */
public class MyBatisGeneratorRun {

    public static void main(String[] args) throws Exception{
        System.out.println("==========开始生成===========");
        MyBatisGeneratorRun app = new MyBatisGeneratorRun();
        app.generator();
        System.out.println("=======逆向工程生成结束=======");
    }

    public void generator() throws Exception{

        List<String> warnings = new ArrayList<String>();
        boolean overwrite = true;
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("generator.xml");
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(resourceAsStream);
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        myBatisGenerator.generate(null);

        for(String warning:warnings){
            System.out.println(warning);
        }
    }
}
