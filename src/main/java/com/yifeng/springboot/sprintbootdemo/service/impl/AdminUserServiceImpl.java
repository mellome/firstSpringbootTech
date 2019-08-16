package com.yifeng.springboot.sprintbootdemo.service.impl;

import com.yifeng.springboot.sprintbootdemo.dao.AdminUserDao;
import com.yifeng.springboot.sprintbootdemo.entity.AdminUser;
import com.yifeng.springboot.sprintbootdemo.service.AdminUserService;
import com.yifeng.springboot.sprintbootdemo.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("adminUserService")
public class AdminUserServiceImpl implements AdminUserService {

    @Autowired
    private AdminUserDao adminUserDao;

    @Override
    public AdminUser updateTokenAndLogin(String userName, String password) {
        AdminUser adminUser = adminUserDao.getAdminUserByUserNameAndPassword(userName, MD5Util.MD5Encode(password, "UTF-8"));
        if (adminUser != null) {
            //登录后即执行修改token的操作
            String token = getNewToken(System.currentTimeMillis() + "", adminUser.getId());
            if (adminUserDao.updateUserToken(adminUser.getId(), token) > 0) {
                //返回数据时带上token
                adminUser.setUserToken(token);
                return adminUser;
            }
        }
        return null;
    }

    /**
     * 获取token值
     *
     * @param sessionId
     * @param userId
     * @return
     */
    private String getNewToken(String sessionId, Long userId) {
        String src = sessionId + userId + com.lou.springboot.utils.NumberUtil.genRandomNum(4);
        return com.lou.springboot.utils.SystemUtil.genToken(src);
    }
}
