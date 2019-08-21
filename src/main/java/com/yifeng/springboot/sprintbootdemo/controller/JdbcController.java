package com.yifeng.springboot.sprintbootdemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class JdbcController {

    // @Autowired通过'byType'的方式来装载某种class类型的bean
    @Autowired
    JdbcTemplate jdbcTemplate;

    @GetMapping("/users/queryAll")
    public List<Map<String, Object>> queryAll(){
        List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from tb_user");
        return list;
    }

    @GetMapping("/users/insert")
    public Object insert(String name, String password){
        if(StringUtils.isEmpty(name) || StringUtils.isEmpty(password) ){
            return false;
        }
        jdbcTemplate.execute("insert into tb_user(`name`,`password`) value (\"" + name + "\",\"" + password + "\")");
        return true;
    }

}
