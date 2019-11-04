package com.itheima.dao;


import com.github.pagehelper.Page;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.Setmeal;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 体检套餐dao层接口
 */
public interface SetmealDao {

    void add(Setmeal setmeal);

    void setSetmealAndCheckGroup(Map<String, Integer> map);

    Page<Setmeal> selectByCondition(@Param("queryString") String queryString);

    List<Setmeal> findAll();

    Setmeal findById(int id);

    List<Map<String, Object>> findSetmealCount();
}
