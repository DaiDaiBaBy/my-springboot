# my-springboot
完善常见springboot应用集成、插件，方便到时候使用

## springboot + excel
使用easyExcel 插件
EasyExcel优势
   注解式自定义操作。
   输入输出简单，提供输入输出过程的接口
   支持一定程度的单元格合并等灵活化操作
1.  依赖
```xml
依赖
	<!-- easyexcel 主要依赖  这一个基本上就够了-->
<dependency>
   <groupId>com.alibaba</groupId>
   <artifactId>easyexcel</artifactId>
   <version>2.2.7</version>
</dependency>
    <!-- servlet-api -->
<dependency>
   <groupId>javax.servlet</groupId>
   <artifactId>javax.servlet-api</artifactId>
   <version>4.0.1</version>
   <scope>provided</scope>
</dependency>
<dependency>
   <groupId>com.alibaba</groupId>
   <artifactId>fastjson</artifactId>
   <version>1.2.74</version>
</dependency>
```
然后编写一个对应数据库实体类的model，作为excel的模板即可
