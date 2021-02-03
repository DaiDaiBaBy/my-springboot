package com.zhoufu.springbootaccesscontrol;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: zhoufu
 * @Date: 2021/2/3 18:06
 * @description:
 */
@RestController
public class TestController {

    @AccessLimit(second = 10, maxCount = 5, needLogin = true)
    @GetMapping("/test")
    public Result test(){
     return Result.ok("牛批plus");
    }
}
