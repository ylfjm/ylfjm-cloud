package com.github.ylfjm.system.controller;

import com.github.ylfjm.common.pojo.vo.PageVO;
import com.github.ylfjm.system.po.Task;
import com.github.ylfjm.system.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述：任务
 *
 * @Author YLFJM
 * @Date 2020/11/6
 */
@RestController
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    /**
     * 创建任务
     *
     * @param task 任务
     */
    @PostMapping(value = "/task")
    public void add(@RequestBody Task task) {
        taskService.add(task);
    }

    /**
     * 查询任务列表
     *
     * @param id 任务ID
     */
    @GetMapping(value = "/task/{id}")
    public Task getById(@PathVariable Integer id) {
        return taskService.getById(id);
    }

    /**
     * 查询任务列表
     *
     * @param status   任务状态：wait-未开始、doing-进行中、done-已完成、pause-已暂停、cancel-已取消、closed-已关闭
     * @param pageNum  第几页
     * @param pageSize 每页大小
     */
    @GetMapping(value = "/task/{pageNum}/{pageSize}")
    public PageVO<Task> page(@RequestParam(required = false) String status,
                             @PathVariable Integer pageNum,
                             @PathVariable Integer pageSize) {
        return taskService.page(status, pageNum, pageSize);
    }
}
