package com.github.ylfjm.system.controller;

import com.github.ylfjm.common.pojo.vo.PageVO;
import com.github.ylfjm.user.feign.UserFeign;
import com.github.ylfjm.user.po.Department;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 部门
 *
 * @author Zhang Bo
 * @date 2018/11/7
 */
@RestController
@RequiredArgsConstructor
public class DepartmentController {

    private final UserFeign userFeign;

    /**
     * 新增部门
     *
     * @param department 部门信息
     */
    @PostMapping(value = "/department")
    public void add(@RequestBody Department department) {
        userFeign.addDepartment(department);
    }

    /**
     * 删除某个部门
     *
     * @param id 部门ID
     */
    @DeleteMapping(value = "/department/{id}")
    public void delete(@PathVariable("id") Integer id) {
        userFeign.deleteDepartment(id);
    }

    /**
     * 更新某个部门信息
     *
     * @param department 部门信息
     */
    @PutMapping(value = "/department")
    public void update(@RequestBody Department department) {
        userFeign.updateDepartment(department);
    }

    /**
     * 查询部门列表
     *
     * @param pageNum  第几页
     * @param pageSize 每页大小
     * @param name     部门名称
     */
    @GetMapping(value = "/department/{pageNum}/{pageSize}")
    public PageVO<Department> page(@PathVariable(value = "pageNum") Integer pageNum,
                                   @PathVariable(value = "pageSize") Integer pageSize,
                                   @RequestParam(value = "name", required = false) String name) {
        return userFeign.getDepartmentPage(pageNum, pageSize, name);
    }

}
