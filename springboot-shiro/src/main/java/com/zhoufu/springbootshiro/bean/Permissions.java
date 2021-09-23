package com.zhoufu.springbootshiro.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author: zhoufu
 * @Date: 2021/3/1 9:46
 * @description:
 */
@Data
@AllArgsConstructor
public class Permissions {
    private String id;
    private String permissionsName;
}
