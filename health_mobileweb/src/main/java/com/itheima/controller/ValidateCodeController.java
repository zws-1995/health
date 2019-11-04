package com.itheima.controller;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.utils.SMSUtils;
import com.itheima.utils.ValidateCodeUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

/**
 * @author woaik
 * @version v1.0
 * @date 2019/10/10 10:54
 * @description 短信验证码
 **/
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Autowired
    private JedisPool jedisPool;

    private static final Logger log = Logger.getLogger(ValidateCodeController.class);


    /**
     * 体检预约时发送手机验证码
     * @param telephone
     * @return
     */
    @RequestMapping("/send4Order")
    public Result send4Order(String telephone) {

        //生成4位数字验证码
        Integer code = ValidateCodeUtils.generateValidateCode(4);
        try {
            //发送短信
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, code.toString());
        } catch (ClientException e) {
            log.error("Send4Order error in ValidateCodeController",e);
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        System.out.println("发送的手机验证码为：" + code);
        //将生成的验证码缓存到redis
        jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_ORDER, 5 * 60, code.toString());
        //验证码发送成功
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }


    /**
     * 手机快速登陆时发送手机验证码
     * @param telephone
     * @return
     */
    @RequestMapping("/send4Login")
    public Result send4Login(String telephone) {
        //生成6位数字验证码
        Integer code = ValidateCodeUtils.generateValidateCode(6);
        try {
            //发送短信
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, code.toString());
        } catch (ClientException e) {
            log.error("Send4Login error in ValidateCodeController",e);
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        System.out.println("发送的手机验证码为：" + code);
        //将生成的验证码缓存到redis
        jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_LOGIN,5*60,code.toString());
        //验证码发送成功
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
}
