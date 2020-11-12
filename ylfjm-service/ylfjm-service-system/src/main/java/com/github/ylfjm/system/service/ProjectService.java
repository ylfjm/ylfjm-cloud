package com.github.ylfjm.system.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.ylfjm.common.pojo.vo.PageVO;
import com.github.ylfjm.system.mapper.ProjectMapper;
import com.github.ylfjm.system.po.Project;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 描述：TODO
 *
 * @Author YLFJM
 * @Date 2020/11/6
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectMapper projectMapper;

    /**
     * 分页查询项目信息，可带查询条件
     *
     * @param pageNum  第几页
     * @param pageSize 每页大小
     */
    public PageVO<Project> page(String status, int pageNum, int pageSize) {
        // 分页查询
        PageHelper.startPage(pageNum, pageSize);
        Page<Project> page = projectMapper.selectPage(status);
        return new PageVO<>(pageNum, page);
    }

}
