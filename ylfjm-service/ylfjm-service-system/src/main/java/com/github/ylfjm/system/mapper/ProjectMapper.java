package com.github.ylfjm.system.mapper;

import com.github.pagehelper.Page;
import com.github.ylfjm.system.po.Project;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface ProjectMapper extends Mapper<Project> {
    Page<Project> selectPage(@Param("status") String status);
}
