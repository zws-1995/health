package com.itheima.service;


import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.RedisConstant;
import com.itheima.dao.SetmealDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.Setmeal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author woaik
 * @version v1.0
 * @date 2019/10/7 22:09
 * @description 体检套餐服务实现类
 **/
@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealDao setmealDao;

    @Autowired
    private JedisPool jedisPool;

    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        setmealDao.add(setmeal);
        if (checkgroupIds != null && checkgroupIds.length > 0) {
            //绑定套餐和检查组的多对多关系
            setSetmealAndCheckGroup(setmeal.getId(), checkgroupIds);
        }
        //将图片名称保存到Redis
        savePic2Redis(setmeal.getImg());
    }

    /**
     * 将图片名称保存到Redis
     * @param pic
     */
    private void savePic2Redis(String pic) {
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, pic);
    }

    /**
     * 绑定套餐和检查组的多对多关系
     * @param id
     * @param checkgroupIds
     */
    private void setSetmealAndCheckGroup(Integer id, Integer[] checkgroupIds) {
        for (Integer checkgroupId : checkgroupIds) {
            Map<String, Integer> map = new HashMap<>();
            map.put("setmeal_id", id);
            map.put("checkgroup_id", checkgroupId);
            setmealDao.setSetmealAndCheckGroup(map);
        }
    }


    /**
     * 分页查询
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    @Override
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage, pageSize);
        Page<Setmeal> page = setmealDao.selectByCondition(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 查询所有套餐
     * @return
     */
    @Override
    public List<Setmeal> finAll() {
        return setmealDao.findAll();
    }


    /**
     * 根据id查询套餐信息
     * @param id
     * @return
     */
    @Override
    public Setmeal findById(int id) {
        return setmealDao.findById(id);
    }

    /**
     * 套餐占比统计
     * @return
     */
    @Override
    public List<Map<String, Object>> findSetmealCount() {
        return setmealDao.findSetmealCount();
    }
}
