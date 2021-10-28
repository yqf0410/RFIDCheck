package com.project.rfidCheck.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.rfidCheck.entity.Task;
import com.project.rfidCheck.entity.TaskBind;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author frank.yang
 * @since 2021-03-31
 */
@Mapper
public interface TaskBindMapper extends BaseMapper<TaskBind> {

}

