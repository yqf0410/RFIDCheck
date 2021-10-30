package com.project.rfidCheck.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.druid.support.json.JSONUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.rfidCheck.entity.ProduBind;
import com.project.rfidCheck.entity.ProduCheck;
import com.project.rfidCheck.entity.TaskLog;
import com.project.rfidCheck.mapper.TaskLogMapper;
import com.project.rfidCheck.service.TaskLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author frank.yang
 * @since 2021-04-08
 */
@Service
public class TaskLogServiceImpl extends ServiceImpl<TaskLogMapper, TaskLog> implements TaskLogService {

    @Autowired
    private TaskLogMapper taskLogMapper;

    @Override
    public String selectTaskLog(Map<String, String> map) {
        QueryWrapper<TaskLog> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(map.get("requestDateS"))) {
            queryWrapper.gt("request_date", DateUtil.parse(map.get("requestDateS")));
        }
        if (StringUtils.isNotEmpty(map.get("requestDateE"))) {
            queryWrapper.le("request_date", DateUtil.parse(map.get("requestDateE")));
        }
        if (StringUtils.isNotEmpty(map.get("responseDateS"))) {
            queryWrapper.gt("response_date", DateUtil.parse(map.get("responseDateS")));
        }
        if (StringUtils.isNotEmpty(map.get("responseDateE"))) {
            queryWrapper.le("response_date", DateUtil.parse(map.get("responseDateE")));
        }
        String sortStr = map.get("sort");
        queryWrapper.orderBy(true, sortStr.subSequence(0, 1).equals("+"), sortStr.substring(1));
        IPage<TaskLog> result = taskLogMapper.selectPage(
                new Page<>(Integer.parseInt(map.get("page")), Integer.parseInt(map.get("limit"))),
                queryWrapper
        );
        Map returnMap = new HashMap();
        List<Map<String, String>> gridData = new ArrayList<>();
        for (TaskLog tl : result.getRecords()) {
            Map<String, String> rowData = new HashMap<>();
            rowData.put("id", tl.getId());
            rowData.put("name", tl.getName());
            rowData.put("flag", tl.getFlag().toString());
            rowData.put("requestDate", DateUtil.format(tl.getRequestDate(), "yyyy/MM/dd HH:mm:ss"));
            rowData.put("responseDate", DateUtil.format(tl.getResponseDate(), "yyyy/MM/dd HH:mm:ss"));
            rowData.put("message", tl.getMessage());
            rowData.put("data", tl.getData());
            gridData.add(rowData);
        }
        returnMap.put("items", gridData);
        returnMap.put("total", result.getTotal());
        return JSONUtils.toJSONString(returnMap);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveLog(String interfaceName, Integer flag, String data, String message, Date sDate) {
        TaskLog taskLog = new TaskLog();
        taskLog.setName(interfaceName);
        taskLog.setFlag(flag);
        taskLog.setData(data);
        taskLog.setMessage(message);
        taskLog.setResponseDate(new Date());
        taskLog.setId(IdUtil.fastUUID().replace("-", ""));
        taskLog.setRequestDate(sDate);
        taskLog.setRequestDate(new Date());
        taskLogMapper.insert(taskLog);
    }
}

