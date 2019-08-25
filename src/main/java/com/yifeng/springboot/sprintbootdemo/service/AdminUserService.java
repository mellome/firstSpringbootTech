package com.yifeng.springboot.sprintbootdemo.service;

import com.yifeng.springboot.sprintbootdemo.entity.AdminUser;
import com.yifeng.springboot.sprintbootdemo.utils.PageResult;
import com.yifeng.springboot.sprintbootdemo.utils.PageUtil;

public interface AdminUserService {

    /**
     * 分页功能
     *
     * @param pageUtil
     * @return
     */
    PageResult getAdminUserPage(PageUtil pageUtil);

    /**
     * 根据用户Token来获取用户
     * @param userToken
     * @return
     */
    AdminUser getAdminUserByToken(String userToken);

    /**
     * 登陆功能
     *
     * @return
     */
    AdminUser updateTokenAndLogin(String userName, String password);

    /**
     * 根据id获取用户记录
     *
     * @return
     */
    AdminUser selectById(Long id);

    /**
     * 根据用户名获取用户记录
     *
     * @return
     */
    AdminUser selectByUserName(String userName);

    /**
     * 新增用户记录
     *
     * @return
     */
    int save(AdminUser user);

    /**
     * 修改密码
     *
     * @return
     */
    int updatePassword(AdminUser user);

    /**
     * 批量删除功能
     */
    int deleteBatch(Integer[] ids);
}
