package com.github.ylfjm.user.mapper;

import com.github.pagehelper.Page;
import com.github.ylfjm.user.po.Role;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author Zhang Bo
 * @date 2018/11/2
 */
public interface RoleMapper extends Mapper<Role> {

    Page<Role> page(@Param("name") String name,
                    @Param("sysType") Integer sysType);

}
