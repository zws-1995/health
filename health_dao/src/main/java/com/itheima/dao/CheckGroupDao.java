package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * 持久层Dao接口
 */
public interface CheckGroupDao {

    void add(CheckGroup checkGroup);

    void setCheckGroupAndCheckItem(Map<String, Integer> map);

    Page<CheckGroup> selectByCondition(@Param("queryString") String queryString);

    CheckGroup findById(@Param("id") Integer id);

    List<Integer> findCheckItemIdsByCheckGroupId(@Param("id") Integer id);

    void deleteAssociation(@Param("id") Integer id);

    void edit(CheckGroup checkGroup);

    Integer findCountByCheckGroupId(@Param("id") Integer id);

    void deleteById(@Param("id") Integer id);

    List<CheckGroup> findAll();
}
