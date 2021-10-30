package com.project.rfidCheck.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.project.rfidCheck.entity.Task;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author frank.yang
 * @since 2021-03-31
 */
public interface TaskService extends IService<Task> {

    //WEB-TASK

    String selectTask(Map<String, String> map);

    String saveTask(Map<String, String> map);

    String daleteTask(Map<String, String> map);

    String importTask(List<Map<String, String>> list);

    //WEB-TASKBIND

    String selectTaskBind(Map<String, String> map);

    String deleteTaskBind(Map<String, String> map);

    //APP

    String selectTaskList();

    String saveTaskBind(Map<String, String> map);

    String selectTaskDetail(Map<String, String> map);

    String saveTaskPad(Map<String, String> map);

    String selectMaxTaskByMrl(Map<String, String> map);
}
