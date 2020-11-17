package com.github.ylfjm.system.mapper;

import com.github.pagehelper.Page;
import com.github.ylfjm.system.po.Task;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface TaskMapper extends Mapper<Task> {
    Page<Task> selectPage(@Param("status") String status);
}
