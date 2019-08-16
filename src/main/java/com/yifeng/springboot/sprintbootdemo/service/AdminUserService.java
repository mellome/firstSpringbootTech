package com.yifeng.springboot.sprintbootdemo.service;

import com.yifeng.springboot.sprintbootdemo.entity.AdminUser;

public interface AdminUserService {
    /**
     * 登陆功能
     *
     * @return
     */
    AdminUser updateTokenAndLogin(String userName, String password);
}
