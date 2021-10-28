package com.project.rfidCheck.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.druid.support.json.JSONUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.rfidCheck.entity.Task;
import com.project.rfidCheck.entity.TaskBind;
import com.project.rfidCheck.mapper.TaskBindMapper;
import com.project.rfidCheck.mapper.TaskMapper;
import com.project.rfidCheck.service.TaskService;
import javafx.scene.control.Pagination;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author frank.yang
 * @since 2021-03-31
 */
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService {


    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private TaskBindMapper taskBindMapper;

    @Override
    public String selectTask(Map<String, String> map) {
        QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(map.get("equipCode"))) {
            queryWrapper.eq("equip_code", map.get("equipCode"));
        }
        if (StringUtils.isNotEmpty(map.get("produLotCode"))) {
            queryWrapper.eq("produ_lot_code", map.get("produLotCode"));
        }
        if (StringUtils.isNotEmpty(map.get("createDateS"))) {
            queryWrapper.gt("create_date", DateUtil.parse(map.get("createDateS")));
        }
        if (StringUtils.isNotEmpty(map.get("createDateE"))) {
            queryWrapper.le("create_date", DateUtil.parse(map.get("createDateE")));
        }
        String sortStr = map.get("sort");
        queryWrapper.orderBy(true, sortStr.subSequence(0, 1).equals("+"), sortStr.substring(1, sortStr.length()));
        IPage<Task> result = taskMapper.selectPage(
                new Page<>(Integer.parseInt(map.get("page").toString()), Integer.parseInt(map.get("limit").toString())),
                queryWrapper
        );
        Map returnMap = new HashMap();
        List<Map<String, String>> gridData = new ArrayList<>();
        for (Task t : result.getRecords()) {
            Map<String, String> rowData = new HashMap<>();
            rowData.put("id", t.getId());
            rowData.put("equipCode", t.getEquipCode());
            rowData.put("produLotCode", t.getProduLotCode());
            rowData.put("qty", t.getQty().toString());
            Integer bindQty = taskBindMapper.selectList(new QueryWrapper<TaskBind>().eq("task_id",t.getId())).size();
            rowData.put("bindQty",bindQty.toString());
            Integer finishQty = taskBindMapper.selectList(new QueryWrapper<TaskBind>().eq("task_id",t.getId()).eq("check_state",1)).size();;
            rowData.put("finishQty", finishQty.toString());
            rowData.put("createDate", DateUtil.format(t.getCreateDate(), "yyyy/MM/dd HH:mm:ss"));
            gridData.add(rowData);
        }
        returnMap.put("items", gridData);
        returnMap.put("total", result.getTotal());
        return JSONUtils.toJSONString(returnMap);
    }

    @Override
    public String saveTask(Map<String, String> map) {
        Task task = new Task();
        task.setEquipCode(map.get("equipCode"));
        task.setProduLotCode(map.get("produLotCode"));
        task.setQty(Integer.parseInt(map.get("qty")));
        task.setCreateDate(new Date());
        if (map.containsKey("id") && map.get("id") != null && map.get("id") != "") {
            task.setId(map.get("id"));
            taskMapper.updateById(task);
        } else {
            //校验
            QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("produ_lot_code",map.get("produLotCode"));
            Task hisProduBind = taskMapper.selectOne(queryWrapper);
            if(hisProduBind != null){
                return "零件批次号已存在！";
            }
            task.setId(IdUtil.fastUUID().replace("-", ""));
            taskMapper.insert(task);
        }
        return "success";
    }

    @Override
    public String daleteTask(Map<String, String> map) {
        taskMapper.deleteById(map.get("id"));
        return "删除成功";
    }

    @Override
    public String importTask(List<Map<String, String>> list) {
        for (Map<String, String> map : list) {
            QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("produ_lot_code",map.get("produLotCode"));
            Task task = taskMapper.selectOne(queryWrapper);
            if (task == null) {
                task = new Task();
                task.setId(IdUtil.fastUUID().replace("-", ""));
                task.setEquipCode(map.get("机床编号"));
                task.setProduLotCode(map.get("零件批次号"));
                task.setQty(Integer.parseInt(map.get("加工数量")));
                task.setCreateDate(new Date());
                taskMapper.insert(task);
            } else {
                task.setEquipCode(map.get("机床编号"));
                task.setProduLotCode(map.get("零件批次号"));
                task.setQty(Integer.parseInt(map.get("加工数量")));
                taskMapper.updateById(task);
            }
        }
        return "保存成功！";
    }

    @Override
    public String selectTaskBind(Map<String, String> map) {
        QueryWrapper<TaskBind> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(map.get("equipCode"))) {
            queryWrapper.eq("equip_code", map.get("equipCode"));
        }
        if (StringUtils.isNotEmpty(map.get("produLotCode"))) {
            queryWrapper.eq("produ_lot_code", map.get("produLotCode"));
        }
        if (StringUtils.isNotEmpty(map.get("rfidUid"))) {
            queryWrapper.eq("rfid_uid", map.get("rfidUid"));
        }
        if (StringUtils.isNotEmpty(map.get("createDateS"))) {
            queryWrapper.gt("create_date", DateUtil.parse(map.get("createDateS")));
        }
        if (StringUtils.isNotEmpty(map.get("createDateE"))) {
            queryWrapper.le("create_date", DateUtil.parse(map.get("createDateE")));
        }
        String sortStr = map.get("sort");
        queryWrapper.orderBy(true, sortStr.subSequence(0, 1).equals("+"), sortStr.substring(1));
        IPage<TaskBind> result = taskBindMapper.selectPage(
                new Page<>(Integer.parseInt(map.get("page").toString()), Integer.parseInt(map.get("limit").toString())),
                queryWrapper
        );
        Map returnMap = new HashMap();
        List<Map<String, String>> gridData = new ArrayList<>();
        for (TaskBind taskBind : result.getRecords()) {
            Map<String, String> rowData = new HashMap<>();
            rowData.put("id", taskBind.getId());
            Task task = taskMapper.selectById(taskBind.getTaskId());
            rowData.put("equipCode", task.getEquipCode());
            rowData.put("produLotCode", task.getProduLotCode());
            rowData.put("qty",task.getQty().toString());
            rowData.put("rfidUid",taskBind.getRfidUid());
            rowData.put("rfidData",taskBind.getRfidData());
            rowData.put("checkState",taskBind.getCheckState().toString());
            rowData.put("checkDate",DateUtil.format(taskBind.getCheckDate(), "yyyy/MM/dd HH:mm:ss"));
            rowData.put("createDate", DateUtil.format(taskBind.getCreateDate(), "yyyy/MM/dd HH:mm:ss"));
            gridData.add(rowData);
        }
        returnMap.put("items", gridData);
        returnMap.put("total", result.getTotal());
        return JSONUtils.toJSONString(returnMap);
    }

    @Override
    public String deleteTaskBind(Map<String, String> map) {
        taskBindMapper.deleteById(map.get("id"));
        return "删除成功";
    }

    @Override
    public String selectTaskList() {
        QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderBy(true, true, "create_date");
        List<Task> list = taskMapper.selectList(queryWrapper);
        List<Map<String, String>> returnList = new ArrayList<>();
        for (Task t : list) {
            Map<String, String> rowData = new HashMap<>();
            rowData.put("id", t.getId());
            rowData.put("equipCode", t.getEquipCode());
            rowData.put("produLotCode", t.getProduLotCode());
            rowData.put("qty", t.getQty().toString());
            Integer bindQty = taskBindMapper.selectList(new QueryWrapper<TaskBind>().eq("task_id",t.getId())).size();
            rowData.put("bindQty",bindQty.toString());
            if(bindQty == t.getQty()){
                continue;
            }
            Integer finishQty = taskBindMapper.selectList(new QueryWrapper<TaskBind>().eq("task_id",t.getId()).eq("check_state",1)).size();;
            rowData.put("finishQty", finishQty.toString());
            rowData.put("createDate", DateUtil.format(t.getCreateDate(), "yyyy/MM/dd HH:mm:ss"));
            returnList.add(rowData);
        }
        return JSONUtils.toJSONString(returnList);
    }

    @Override
    public String saveTaskBind(Map<String, String> map) {
        Map<String, String> returnMap = new HashMap<>();
        QueryWrapper<TaskBind> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("rfid_uid",map.get("rfidUid"));
        queryWrapper.eq("check_state", 0);
        List<TaskBind> taskBinds = taskBindMapper.selectList(queryWrapper);
        if(taskBinds.size() > 0){
            returnMap.put("flag","false");
            returnMap.put("data","此RFID已绑定且未校验！");
            return JSONUtils.toJSONString(returnMap);
        }
        Task task;
        if(StringUtils.isNotEmpty(map.get("taskId"))) {
            task = taskMapper.selectById(map.get("taskId"));
        }else {
            task = new Task();
            task.setProduLotCode(map.get("produLotCode"));
            task.setEquipCode(map.get("equipCode"));
            task.setQty(Integer.parseInt(map.get("qty")));
            task.setBindType(Integer.parseInt(map.get("bindType")));
            task.setBarCode(map.get("barCode"));
            taskMapper.insert(task);
        }

        TaskBind taskBind = new TaskBind();
        taskBind.setId(IdUtil.fastUUID().replace("-", ""));
        taskBind.setTaskId(task.getId());
        taskBind.setRfidUid(map.get("rfidUid"));
        taskBind.setRfidData(map.get("rfidData"));
        taskBindMapper.insert(taskBind);

        returnMap.put("flag","true");
        returnMap.put("data","绑定成功！");
        return JSONUtils.toJSONString(returnMap);
    }
    @Override
    public String selectTaskDetail(Map<String, String> map) {
        Task task = taskMapper.selectById(map.get("taskId"));
        Map<String, String> rowData = new HashMap<>();
        rowData.put("id", task.getId());
        rowData.put("equipCode", task.getEquipCode());
        rowData.put("produLotCode", task.getProduLotCode());
        rowData.put("qty", task.getQty().toString());
        Integer bindQty = taskBindMapper.selectList(new QueryWrapper<TaskBind>().eq("task_id",task.getId())).size();
        rowData.put("bindQty",bindQty.toString());
        Integer finishQty = taskBindMapper.selectList(new QueryWrapper<TaskBind>().eq("task_id",task.getId()).eq("check_state",1)).size();;
        rowData.put("finishQty", finishQty.toString());
        rowData.put("createDate", DateUtil.format(task.getCreateDate(), "yyyy/MM/dd HH:mm:ss"));
        return JSONUtils.toJSONString(rowData);
    }

}
