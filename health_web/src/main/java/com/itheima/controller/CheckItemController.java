package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckItemService;
import org.apache.log4j.Logger;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author woaik
 * @version v1.0
 * @date 2019/10/2 18:45
 * @description 体检检查项管理
 **/
@RestController
@RequestMapping("/checkitem")
public class CheckItemController {

    private static final Logger log = Logger.getLogger(CheckItemController.class);

    @Reference
    private CheckItemService checkItemService;

    /**
     * 新增
     * @param checkItem
     * @return
     */
    @PreAuthorize("hasAuthority('CHECKITEM_ADD')")
    @RequestMapping("/add")
    public Result add(@RequestBody CheckItem checkItem){
        try {
            checkItemService.add(checkItem);
        }catch (Exception e){
            log.error("Add error in CheckItemController",e);
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.ADD_CHECKITEM_SUCCESS);
    }

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    @PreAuthorize("hasAuthority('CHECKITEM_QUERY')")
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = checkItemService.pageQuery(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString()
        );
        return pageResult;
    }


    /**
     * 删除
     * @param id
     * @return
     */
    @PreAuthorize("hasAuthority('CHECKITEM_DELETE')")
    @RequestMapping("/delete")
    public Result delete(Integer id) {
        try {
            checkItemService.delete(id);
        } catch (RuntimeException e) {
            return new Result(false, e.getMessage());
        } catch (Exception e) {
            return new Result(false,MessageConstant.DELETE_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }


    /**
     * 根据id查询检查项
     * @param id
     * @return
     */
    @PreAuthorize("hasAuthority('CHECKITEM_QUERY')")
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        try {
            CheckItem checkItem = checkItemService.findById(id);
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, checkItem);
        } catch (Exception e) {
            log.error("Find by id query error", e);
        }
        return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
    }


    /**
     * 编辑
     * @param checkItem
     * @return
     */
    @PreAuthorize("hasAuthority('CHECKITEM_EDIT')")
    @RequestMapping("/edit")
    public Result edit(@RequestBody CheckItem checkItem) {
        try {
            checkItemService.edit(checkItem);
        } catch (Exception e) {
            return new Result(false, MessageConstant.EDIT_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }


    /**
     * 查询所有
     * @return
     */
    @PreAuthorize("hasAuthority('CHECKITEM_QUERY')")
    @RequestMapping("/findAll")
    public Result findAll() {
        List<CheckItem> checkItemList = checkItemService.findAll();
        if (checkItemList != null && checkItemList.size() > 0) {
            Result result = new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS);
            result.setData(checkItemList);
            return result;
        }
        return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
    }
}
