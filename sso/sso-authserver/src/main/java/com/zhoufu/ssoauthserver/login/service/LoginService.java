package com.zhoufu.ssoauthserver.login.service;

/**
 * @Author: zhoufu
 * @Date: 2021/2/23 15:46
 * @description:
 */
public interface LoginService {

    String getTicket(String userName);

    String createTicket(String uuid);

    boolean login(String userName, String password);

    boolean loginOut(String userName);
}
