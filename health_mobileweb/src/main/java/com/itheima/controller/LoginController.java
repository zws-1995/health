package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * @author woaik
 * @version v1.0
 * @date 2019/10/10 21:38
 * @description 用户登录
 **/
@RestController
@RequestMapping("/login")
public class LoginController {

    @Reference
    private MemberService memberService;

    @Autowired
    private JedisPool jedisPool;

    /**
     * 使用手机号和验证码登录
     * @param response
     * @param map
     * @return
     */
    @RequestMapping("/check")
    public Result check(HttpServletResponse response, @RequestBody Map map) {
        String telephone = (String) map.get("telephone");
        String validateCode = (String) map.get("validateCode");
        //从Redis中获取缓存的验证码
        String codeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);

        if (codeInRedis == null || !codeInRedis.equals(validateCode)) {
            //验证码错误
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
            //return null;
        } else {
            //验证码输入正确
            //判断当前用户是否为会员
            Member member = memberService.findByTelephone(telephone);
            if (member == null) {
                //当前用户不是会员，自动完成注册
                member = new Member();
                member.setPhoneNumber(telephone);
                member.setRegTime(new Date());
                memberService.add(member);
            }
            //注册完成，登录成功
            //写入Cookie，跟踪用户
            Cookie cookie = new Cookie("login_member_telephone", telephone);
            //路径
            cookie.setPath("/");
            //有效期30天
            cookie.setMaxAge(60*60*24*30);
            response.addCookie(cookie);
            return new Result(true, MessageConstant.LOGIN_SUCCESS);
        }
    }
}
