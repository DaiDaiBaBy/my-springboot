package com.zhoufu.springboottask.util;

import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @Author: zhoufu
 * @Date: 2021/3/5 15:52
 * @description:
 */
@Slf4j
public class DateUtils {
    /**
     * 对日期的【秒】进行加/减
     *
     * @param date
     *            日期
     * @param seconds
     *            秒数，负数为减
     * @return 加/减几秒后的日期
     */
    public static Date addDateSeconds(Date date, int seconds) {

        return org.apache.commons.lang3.time.DateUtils.addSeconds(date, seconds);
    }

}
