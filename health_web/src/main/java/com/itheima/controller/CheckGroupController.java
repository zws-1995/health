package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.service.CheckGroupService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author woaik
 * @version v1.0
 * @date 2019/10/4 22:58
 * @description 检查组管理
 **/
@RestController
@RequestMapping("/checkgroup")
public class CheckGroupController {

    @Reference
    private CheckGroupService checkGroupService;

    /**
     * 新增
     * @param checkGroup
     * @param checkitemIds
     * @return
     */
    @PreAuthorize("hasAuthority('CHECKGROUP_ADD')")
    @RequestMapping("/add")
    public Result add(@RequestBody CheckGroup checkGroup, Integer[] checkitemIds) {
        try {
            checkGroupService.add(checkGroup, checkitemIds);
        } catch (Exception e) {
            //新增失败
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
        //新增成功
        return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }


    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    @PreAuthorize("hasAuthority('CHECKGROUP_QUERY')")
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = checkGroupService.pageQuery(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString()
        );
        return pageResult;
    }


    /**
     * 根据id查询
     * @param id
     * @return
     */
    @PreAuthorize("hasAuthority('CHECKGROUP_QUERY')")
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        CheckGroup checkGroup = checkGroupService.findById(id);
        if (checkGroup != null) {
            Result result = new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS);
            result.setData(checkGroup);
            return result;
        }
        return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
    }


    /**
     * 根据检查组合id查询对应的所有检查项id
     * @param id
     * @return
     */
    @PreAuthorize("hasAuthority('CHECKGROUP_QUERY')")
    @RequestMapping("/findCheckItemIdsByCheckGroupId")
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id) {
        List<Integer> list = checkGroupService.findCheckItemIdsByCheckGroupId(id);
        return list;
    }

    /**
     * 编辑
     * @param checkGroup
     * @param checkitemIds
     * @return
     */
    @PreAuthorize("hasAuthority('CHECKGROUP_EDIT')")
    @RequestMapping("/edit")
    public Result edit(@RequestBody CheckGroup checkGroup, Integer[] checkitemIds) {
        try {
            checkGroupService.edit(checkGroup, checkitemIds);
        } catch (Exception e) {
            return new Result(false, MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
        return new Result(true, MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }


    /**
     * 删除检查项
     * @param id
     * @return
     */
    @PreAuthorize("hasAuthority('CHECKGROUP_DELETE')")
    @RequestMapping("/deleteById")
    public Result deleteById(@RequestParam("id") Integer id) {
        try {
            checkGroupService.deleteById(id);
            return new Result(true, MessageConstant.DELETE_CHECKGROUP_SUCCESS);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
    }


    /**
     * 查询所有检查组
     * @return
     */
    @PreAuthorize("hasAuthority('CHECKGROUP_QUERY')")
    @RequestMapping("/findAll")
    public Result findAll() {
        List<CheckGroup> checkGroupList = checkGroupService.findAll();
        if (checkGroupList != null && checkGroupList.size() > 0) {
            Result result = new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS);
            result.setData(checkGroupList);
            return result;
        }
        return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
    }
}
