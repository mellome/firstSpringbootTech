package com.yifeng.springboot.sprintbootdemo.dao;

import com.yifeng.springboot.sprintbootdemo.entity.User;
import java.util.List;
import java.util.Map;


/**
 * @Description:CRUD操作
 */

public interface UserDao {
    /**
     *
     * @Description:返回数据列表
     * @return
     */
    List<User> findAllUsers();


    /**
     * 根据主键查询
     *
     * @param id
     * @return
     */
    User getUserById(Integer id);


    /**
     * @Description:添加
     * @param user
     * @return
     */
    int insertUser (User user);


    /**
     * @Description:修改
     * @return
     */
    int updUser(User user);

    /**
     * @Description:删除
     * @param id
     * @return
     */
    int delUser(Integer id);

}
