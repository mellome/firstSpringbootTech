package com.yifeng.springboot.sprintbootdemo.controller;

import com.yifeng.springboot.sprintbootdemo.common.Constants;
import com.yifeng.springboot.sprintbootdemo.common.Result;
import com.yifeng.springboot.sprintbootdemo.common.ResultGenerator;
import com.yifeng.springboot.sprintbootdemo.entity.AdminUser;
import com.yifeng.springboot.sprintbootdemo.service.AdminUserService;
import com.yifeng.springboot.sprintbootdemo.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class AdminUserControler {

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
     * 使用 @RequestBody 将前端传过来的参数封装为 AdminUser 对象，之后判断参数是否符合规范，并检查是否已经存在相同用户，如果所有参数校验都成功通过，则调用 save() 方法进行实际的入库操作
     * 用来处理前端请求的用户参数, 添加新的 user 信息
     * @param user
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Result save(@RequestBody AdminUser user) {

        //检查前端请求的user是否具备基本信息
        if (StringUtils.isEmpty(user.getUserName()) || StringUtils.isEmpty(user.getPassword())) {
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_PARAM_ERROR, "parameter exception！");
        }

        //尝试从 DB 获取相应的 user 资料
        AdminUser tempUser = adminUserService.selectByUserName(user.getUserName());

        if (tempUser != null) {
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_PARAM_ERROR, "user already exists in DB, do not repeat to add!");
        }

        /**
         *  trim() 去掉字符串首尾空格
         *  endsWith() 测试字符串是否以指定的后缀结束
         */

        if ("admin".endsWith(user.getUserName().trim())) {
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_PARAM_ERROR, "can't add admin in DB!");
        }
        if (adminUserService.save(user) > 0) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("add failed");
        }
    }


    /**
     * @param user
     * @return
     */
    @RequestMapping(value = "/updatePassword", method = RequestMethod.PUT)
    public Result update(@RequestBody AdminUser user) {
        if (StringUtils.isEmpty(user.getPassword())) {
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_PARAM_ERROR, "please enter the password!");
        }
        AdminUser tempUser = adminUserService.selectById(user.getId());
        if (tempUser == null) {
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_PARAM_ERROR, "user doesn't exist! ");
        }
        if ("admin".endsWith(tempUser.getUserName().trim())) {
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_PARAM_ERROR, "can not modify admin user!");
        }
        tempUser.setPassword(user.getPassword());
        if (adminUserService.updatePassword(user) > 0) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("password update failed!");
        }
    }

}
