package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Order;
import com.itheima.service.OrderService;
import com.itheima.utils.SMSUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * @author woaik
 * @version v1.0
 * @date 2019/10/10 11:07
 * @description 体检预约
 **/
@RestController
@RequestMapping("/order")
public class OrderController {

    @Reference
    private OrderService orderService;

    @Autowired
    private JedisPool jedisPool;

    private static final Logger log = Logger.getLogger(OrderController.class);

    /**
     * 体检预约
     * @param map
     * @return
     */
    @RequestMapping("/submit")
    public Result submitOrder(@RequestBody Map map) {
        //获取用户输入的手机号
        String telephone = (String) map.get("telephone");
        //从Redis中获取缓存的验证码，key为手机号+RedisConstant.SENDTYPE_ORDER
        String codeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);

        //获取用户输入的验证码
        String validateCode = (String) map.get("validateCode");
        //校验手机验证码
        if (codeInRedis == null || !codeInRedis.equals(validateCode)) {
            //如果为验证码过期，或者验证码校验错误
//            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        Result result = null;
        //调用体检预约服务
        try {
            map.put("orderType", Order.ORDERTYPE_WEIXIN);
            result = orderService.order(map);
        } catch (Exception e) {
            log.error("Submit error in OrderController",e);
            //预约失败
            return result;
        }

        if (result.isFlag()) {
            //预约成功后，发送短信通知
            String orderDate = (String) map.get("orderDate");
            try {
                SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE, telephone, orderDate);
            } catch (ClientException e) {
                log.error("Submit error in OrderController",e);
            }
        }
        return result;
    }


    /**
     * 根据id查询预约信息，包括套餐信息和会员信息
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        Result result = null;
        try {
            result = orderService.findById4Detail(id);
            //查询预约信息成功
            return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, result.getData());
        } catch (Exception e) {
            log.error("FindById error in OrderController",e);
            //查询预约信息失败
            return new Result(false, MessageConstant.QUERY_ORDER_FAIL);
        }
    }
}
