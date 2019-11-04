package com.itheima.dao;


import com.itheima.pojo.OrderSetting;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 预约设置dao层
 */
public interface OrderSettingDao {
    long findCountByOrderDate(Date orderDate);

    /**
     * 更新可预约人数
     * @param orderSetting
     */
    void editNumberByOrderDate(OrderSetting orderSetting);

    void add(OrderSetting orderSetting);

    /**
     * 根据日期范围查询预约设置信息
     * @param date
     * @return
     */
    List<OrderSetting> getOrderSettingByMonth(Map date);

    /**
     * 根据预约日期查询预约设置信息
     * @param date
     * @return
     */
    OrderSetting findByOrderDate(@Param("orderDate") Date date);

    /**
     * 更新已预约人数
     * @param orderSetting
     */
    void editReservationsByOrderDate(OrderSetting orderSetting);
}
