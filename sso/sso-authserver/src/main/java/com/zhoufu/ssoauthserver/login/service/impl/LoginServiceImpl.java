package com.zhoufu.ssoauthserver.login.service.impl;

import com.zhoufu.ssoauthserver.login.service.LoginService;
import com.zhoufu.ssoauthserver.util.EncryptUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

/**
 * @Author: zhoufu
 * @Date: 2021/2/23 15:47
 * @description:
 */
@Service
@Slf4j
public class LoginServiceImpl implements LoginService {
    @Override
    public String getTicket(String userName) {
        return null;
    }

    @Override
    public String createTicket(String uuid) {
        return DigestUtils.md5DigestAsHex((EncryptUtil.SALT + uuid + System.currentTimeMillis()).getBytes());
    }

    @Override
    public boolean login(String userName, String password) {
        return userName.equalsIgnoreCase(password);
    }

    @Override
    public boolean loginOut(String userName) {
        return false;
    }
}
