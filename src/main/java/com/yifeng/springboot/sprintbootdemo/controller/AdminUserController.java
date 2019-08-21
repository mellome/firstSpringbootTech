package com.yifeng.springboot.sprintbootdemo.controller;

import com.yifeng.springboot.sprintbootdemo.common.Constants;
import com.yifeng.springboot.sprintbootdemo.common.Result;
import com.yifeng.springboot.sprintbootdemo.common.ResultGenerator;
import com.yifeng.springboot.sprintbootdemo.entity.AdminUser;
import com.yifeng.springboot.sprintbootdemo.service.AdminUserService;
import com.yifeng.springboot.sprintbootdemo.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class AdminUserController {

    @Autowired
    private AdminUserService adminUserService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result login(@RequestBody AdminUser user) {

        Result result = ResultGenerator.genFailResult("login failed");

        if (StringUtils.isEmpty(user.getUserName()) || StringUtils.isEmpty(user.getPassword())) {
            result.setMessage("Please fill in the login information！");
        }

        AdminUser loginUser = adminUserService.updateTokenAndLogin(user.getUserName(), user.getPassword());

        if (loginUser != null) {
            result = ResultGenerator.genSuccessResult(loginUser);
        }
        return result;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result list(@RequestParam Map<String, Object> params) {

        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_PARAM_ERROR, "parameter exception！");
        }
        //查询列表数据
        PageUtil pageUtil = new PageUtil(params);
        return ResultGenerator.genSuccessResult(adminUserService.getAdminUserPage(pageUtil));
    }

    /**
     * 用来处理前端请求的用户参数
     * @param user
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Result save(@RequestBody AdminUser user){
        if(StringUtils.isEmpty(user.getUserName())||StringUtils.isEmpty(user.getPassword())){
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_PARAM_ERROR,"parameter exception！");
        }
        AdminUser tempUser = adminUserService.
    }


}
