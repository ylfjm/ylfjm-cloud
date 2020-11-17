package com.github.ylfjm.system.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.ylfjm.common.BadRequestException;
import com.github.ylfjm.common.YlfjmException;
import com.github.ylfjm.common.cache.UserCache;
import com.github.ylfjm.common.pojo.vo.PageVO;
import com.github.ylfjm.system.mapper.TaskLogMapper;
import com.github.ylfjm.system.mapper.TaskMapper;
import com.github.ylfjm.system.po.Task;
import com.github.ylfjm.system.po.TaskLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * 描述：TODO
 *
 * @Author YLFJM
 * @Date 2020/11/6
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class TaskService {

    private final TaskMapper taskMapper;
    private final TaskLogMapper taskLogMapper;

    /**
     * 创建任务
     *
     * @param task 任务
     */
    public void add(Task task) {
        Date now = new Date();
        if (!StringUtils.hasText(task.getName())) {
            throw new BadRequestException("操作失败，任务名称不能为空");
        }
        if (task.getPri() == null) {
            throw new BadRequestException("操作失败，请选择任务优先级");
        }
        if (!StringUtils.hasText(task.getType())) {
            throw new BadRequestException("操作失败，请选择任务类型");
        }
        if (task.getDeadline() == null) {
            throw new BadRequestException("操作失败，请选择任务截止日期");
        }
        if (StringUtils.hasText(task.getUiDesigner())) {
            task.setUiRequired(true);
        } else {
            task.setUiRequired(false);
        }
        if (StringUtils.hasText(task.getWebDeveloper())) {
            task.setWebRequired(true);
        } else {
            task.setWebRequired(false);
        }
        if (StringUtils.hasText(task.getAndroidDeveloper())) {
            task.setAndroidRequired(true);
        } else {
            task.setAndroidRequired(false);
        }
        if (StringUtils.hasText(task.getIosDeveloper())) {
            task.setIosRequired(true);
        } else {
            task.setIosRequired(false);
        }
        if (StringUtils.hasText(task.getServerDeveloper())) {
            task.setServerRequired(true);
        } else {
            task.setServerRequired(false);
        }
        task.setLastEditedBy(UserCache.getAccount());
        task.setLastEditedDate(now);
        task.setId(null);
        task.setStatus("wait");
        task.setDeleted(false);
        int result = taskMapper.insertSelective(task);
        if (result < 1) {
            throw new YlfjmException("操作失败，创建任务发生错误");
        }

        TaskLog taskLog = new TaskLog();
        taskLog.setContent("创建");
        taskLog.setCreateBy(UserCache.getAccount());
        taskLog.setCreateDate(now);
        taskLogMapper.insert(taskLog);
    }

    /**
     * 分页查询任务信息，可带查询条件
     *
     * @param pageNum  第几页
     * @param pageSize 每页大小
     */
    public PageVO<Task> page(String status, int pageNum, int pageSize) {
        // 分页查询
        PageHelper.startPage(pageNum, pageSize);
        Page<Task> page = taskMapper.selectPage(status);
        return new PageVO<>(pageNum, page);
    }

}
