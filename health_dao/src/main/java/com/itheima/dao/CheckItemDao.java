package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 持久层Dao接口
 */
public interface CheckItemDao {
    void add(CheckItem checkItem);

    Page<CheckItem> selectByCondition(@Param("queryString") String queryString);

    long findCountByCheckItemId(@Param("checkItemId") Integer checkItemId);

    void deleteById(@Param("id") Integer id);

    CheckItem queryById(@Param("id") Integer id);

    void edit(CheckItem checkItem);

    List<CheckItem> findAll();
}
