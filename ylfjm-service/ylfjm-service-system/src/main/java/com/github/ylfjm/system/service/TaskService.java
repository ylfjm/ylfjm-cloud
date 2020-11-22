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
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional
    public void add(Task task) {
        Date now = new Date();
        //校验
        this.check(task, now);
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
     * 修改任务
     *
     * @param task 任务
     */
    @Transactional
    public void update(Task task) {
        Date now = new Date();
        if (task.getId() == null) {
            throw new BadRequestException("操作失败，请选择任务");
        }
        Task record = taskMapper.selectByPrimaryKey(task.getId());
        if (record == null) {
            throw new BadRequestException("操作失败，任务不存在或已被删除");
        }
        //校验
        this.check(task, now);
        task.setStatus(record.getStatus());
        task.setDeleted(record.getDeleted());
        int result = taskMapper.updateByPrimaryKey(task);
        if (result < 1) {
            throw new YlfjmException("操作失败，修改任务发生错误");
        }
        TaskLog taskLog = new TaskLog();
        taskLog.setContent("修改");
        taskLog.setCreateBy(UserCache.getAccount());
        taskLog.setCreateDate(now);
        taskLogMapper.insert(taskLog);
    }

    /**
     * 查询单个任务
     *
     * @param id 任务ID
     */
    public Task getById(Integer id) {
        Task task = taskMapper.selectByPrimaryKey(id);
        if (task == null) {
            throw new BadRequestException("查询失败，任务不存在或已被删除");
        }
        return task;
    }

    /**
     * 分页查询任务信息，可带查询条件
     *
     * @param status   任务状态：wait-未开始、doing-进行中、done-已完成、pause-已暂停、cancel-已取消、closed-已关闭
     * @param pageNum  第几页
     * @param pageSize 每页大小
     */
    public PageVO<Task> page(String status, int pageNum, int pageSize) {
        // 分页查询
        PageHelper.startPage(pageNum, pageSize);
        Page<Task> page = taskMapper.selectPage(status);
        return new PageVO<>(pageNum, page);
    }

    /**
     * 校验
     */
    private void check(Task task, Date now) {
        if (task.getProjectId() == null) {
            throw new BadRequestException("操作失败，请选择任务所属项目");
        }
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
        task.setPdRequired(StringUtils.hasText(task.getPdDesigner()));
        task.setUiRequired(StringUtils.hasText(task.getUiDesigner()));
        task.setWebRequired(StringUtils.hasText(task.getWebDeveloper()));
        task.setAndroidRequired(StringUtils.hasText(task.getAndroidDeveloper()));
        task.setIosRequired(StringUtils.hasText(task.getIosDeveloper()));
        task.setServerRequired(StringUtils.hasText(task.getServerDeveloper()));
        task.setTestRequired(StringUtils.hasText(task.getTester()));
        task.setLastEditedBy(UserCache.getAccount());
        task.setLastEditedDate(now);
    }

}
