package com.itheima.controller;

import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author woaik
 * @version v1.0
 * @date 2019/10/13 16:28
 * @description 用户管理
 **/
@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger log = Logger.getLogger(UserController.class);

    /**
     * 获取当前登录用户的用户名
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/getUsername")
    public Result getUsername() throws Exception {
        try{
            org.springframework.security.core.userdetails.User user =
                    (org.springframework.security.core.userdetails.User)
                            SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,user.getUsername());
        }catch (Exception e){
            return new Result(false, MessageConstant.GET_USERNAME_FAIL);
        }
    }
}
