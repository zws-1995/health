package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.OrderSettingDao;
import com.itheima.pojo.OrderSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author woaik
 * @version v1.0
 * @date 2019/10/8 16:43
 * @description 预约设置服务
 **/
@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    /**
     * 批量添加
     * @param orderSettingList
     */
    @Override
    public void add(List<OrderSetting> orderSettingList) {
        if (orderSettingList != null && orderSettingList.size() > 0) {
            for (OrderSetting orderSetting : orderSettingList) {
                //检查此数据（日期）是否存在
                long count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
                if (count > 0) {
                    //已经存在，执行更新操作
                    orderSettingDao.editNumberByOrderDate(orderSetting);
                } else {
                    //不存在，执行添加操作
                    orderSettingDao.add(orderSetting);
                }
            }
        }
    }


    /**
     * 根据日期查询预约设置数据
     * @param date
     * @return
     */
    @Override
    public List<Map> getOrderSettingByMonth(String date) {
        //月份2019-10拼接==》》2019-10-1  2019-10-31
        //定义月份第一天
        String dateBegin = date + "-1";
        //定义月份最后一天
        String dateEnd = date + "-31";
        //将第一天和最后一天 封装进map集合中
        Map map = new HashMap();
        map.put("dateBegin", dateBegin);
        map.put("dateEnd", dateEnd);
        //根据月份查询出当月的数据
        List<OrderSetting> list = orderSettingDao.getOrderSettingByMonth(map);

        List<Map> data = new ArrayList<>();
        //遍历list，将数据封装到map集合中
        for (OrderSetting orderSetting : list) {
            Map orderSettingMap = new HashMap();
            orderSettingMap.put("date", orderSetting.getOrderDate().getDate());
            orderSettingMap.put("number", orderSetting.getNumber());
            orderSettingMap.put("reservations", orderSetting.getReservations());

            //将map集合中的数据封装到data集合中
            data.add(orderSettingMap);
        }
        return data;
    }


    /**
     * 根据日期修改可预约人数
     * @param orderSetting
     */
    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        long count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
        if (count > 0) {
            //当前日期已经进行了预约设置，需要进行修改操作
            orderSettingDao.editNumberByOrderDate(orderSetting);
        } else {
            //当前日期没有进行预约设置，进行添加操作
            orderSettingDao.add(orderSetting);
        }
    }
}
