package com.yifeng.springboot.sprintbootdemo.service;

import com.yifeng.springboot.sprintbootdemo.entity.AdminUser;
import com.yifeng.springboot.sprintbootdemo.utils.PageResult;
import com.yifeng.springboot.sprintbootdemo.utils.PageUtil;

public interface AdminUserService {
    /**
     * 登陆功能
     *
     * @return
     */
    AdminUser updateTokenAndLogin(String userName, String password);

    PageResult getAdminUserPage(PageUtil pageUtil);
}
