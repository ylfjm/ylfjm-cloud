package com.github.ylfjm.user.mapper;

import com.github.ylfjm.user.po.RoleMenu;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author YLFJM
 * @date 2018/11/2
 */
public interface RoleMenuMapper extends Mapper<RoleMenu> {

    /**
     * 批量插入
     *
     * @param list
     */
    void batchInsert(@Param("list") List<RoleMenu> list);
}
