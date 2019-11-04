package com.itheima.service;


import com.itheima.entity.PageResult;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

/**
 * 体检套餐服务接口
 */
public interface SetmealService {
    /**
     * 新增套餐
     * @param setmeal
     * @param checkgroupIds
     */
    public void add(Setmeal setmeal, Integer[] checkgroupIds);

    /**
     * 套餐分页查询
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);

    /**
     * 查询所有套餐
     * @return
     */
    List<Setmeal> finAll();

    /**
     * 根据id查询套餐信息
     * @param id
     * @return
     */
    Setmeal findById(int id);

    List<Map<String, Object>> findSetmealCount();
}
