package com.zhoufu.springboottask.dto;

import lombok.Data;

/**
 * @Author: zhoufu
 * @Date: 2021/3/3 9:30
 * @description:
 */
@Data
public class DemoDTO {

    @Data
    public static class DemoInDto{
        private String staffId;  // 员工ID
        private String ossId;  //站点id
        private String skuCode;  // sku编码
        private String skuListPrice;  // 标价
        private String  startTime;
        private String source; // 来源
        private long skuSalePrice;  // 优惠价
        private long priceSettlement;  // 结算价
        private long priceBreakeven;  // 保本价
    }

}
