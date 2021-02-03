package com.zhoufu.springbootexcel.model;

import java.io.Serializable;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.*;

/**
 *  数据库实体类   excel 导出 导入模板类
 */
@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Goods extends BaseRowModel implements Serializable {
    /**
     * 商品编号id，主键
     */
    @ExcelProperty(value = "商品编号", index = 0)
    private Integer goodsId;

    /**
     * 商品编码
     */
    @ExcelProperty(value = "商品编码", index = 1)
    private String goodsCode;

    /**
     * 商品名称
     */
    @ExcelProperty(value = "商品名称", index = 2)
    private String goodsName;

    /**
     * 库存数量
     */
    @ExcelProperty(value = "库存", index = 3)
    private Integer inventoryQuantity;

    /**
     * 上一次采购价格
     */
    @ExcelProperty(value = "上一次采购价", index = 4)
    private Float lastPurchasingPrice;

    /**
     * 库存下限
     */
    @ExcelProperty(value = "最小库存量", index = 5)
    private Integer minNum;

    /**
     * 商品型号
     */
    @ExcelProperty(value = "商品型号", index = 6)
    private String goodsModel;

    /**
     * 生产厂商
     */
    @ExcelProperty(value = "生产厂商", index = 7)
    private String goodsProducer;

    /**
     * 采购价格
     */
    @ExcelProperty(value = "采购价格", index = 8)
    private Float purchasingPrice;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注信息", index = 9)
    private String remarks;

    /**
     * 出售价格
     */
    @ExcelProperty(value = "出售价格", index = 10)
    private Float sellingPrice;

    /**
     * 0表示初始值,1表示已入库，2表示有进货或销售单据
     */
    @ExcelProperty(value = "初始值(1/期初库存入仓库2/有进货或者销售单据0/初始化状态)", index = 11)
    private Integer state;

    /**
     * 商品单位
     */
    @ExcelProperty(value = "商品单位", index = 12)
    private String goodsUnit;

    /**
     * 商品类别id，外键
     */
    @ExcelProperty(value = "商品类别id", index = 13)
    private Integer goodsTypeId;

    private static final long serialVersionUID = 1L;
}