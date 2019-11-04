package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author woaik
 * @version v1.0
 * @date 2019/10/9 17:09
 * @description 移动端套餐业务处理
 **/
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference
    private SetmealService setmealService;

    private static final Logger log = Logger.getLogger(SetmealController.class);

    /**
     * 获取所有套餐信息
     * @return
     */
    @RequestMapping("/getSetmeal")
    public Result getSetmeal() {
        try {
            List<Setmeal> list = setmealService.finAll();
            return new Result(true, MessageConstant.GET_SETMEAL_LIST_SUCCESS, list);
        } catch (Exception e) {
            log.error("GetSetmeal error in MobileSetmealController",e);
            return new Result(false, MessageConstant.GET_SETMEAL_LIST_FAIL);
        }
    }


    /**
     * 根据id查询套餐信息
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(int id) {
        try {
            Setmeal setmeal = setmealService.findById(id);
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
        } catch (Exception e) {
            log.error("FindById error in MobileSetmealController",e);
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }

}
