package com.zhoufu.ssoauthserver.constant;

/**
 * @Author: zhoufu
 * @Date: 2021/2/23 14:24
 * @description:
 */
public class AppConstant {
    public static final String REDIS_TICKET_PREFIX = "TICKET:";

    public static final int REDIS_TICKET_ALIVE_SECONDS = 60;

    public static final boolean ENABLE_DISPOSABLE_TICKET = false;
}
