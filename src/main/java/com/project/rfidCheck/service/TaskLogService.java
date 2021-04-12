package com.project.rfidCheck.service;

import com.project.rfidCheck.entity.TaskLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author frank.yang
 * @since 2021-04-08
 */
public interface TaskLogService extends IService<TaskLog> {

    String selectTaskLog(Map<String, String> map);

    void saveLog(String interfaceName, Integer flag, String data, String message);
}
