package com.project.rfidCheck.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.rfidCheck.entity.Task;
import javafx.scene.control.Pagination;
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
public interface TaskMapper extends BaseMapper<Task> {

    @Select("SELECT b.equip_code, b.produ_lot_code, b.qty, a.rfid_uid, a.rfid_data, a.check_state, a.check_date, a.create_date FROM task_bind a JOIN task b ON a.task_id = b.id")
    List<Map<String, Object>> getTaskBind(Page<Map<String,Object>> page);

}

