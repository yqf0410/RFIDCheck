package com.project.rfidCheck.controller;


import com.project.rfidCheck.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author frank.yang
 * @since 2021-03-31
 */
@RestController
@RequestMapping("/t")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("/selectTask")
    @CrossOrigin
    public String selectTask(@RequestBody Map<String, String> map) {
        String returnMapStr = taskService.selectTask(map);
        return returnMapStr;

    }

    @PostMapping("/saveTask")
    @CrossOrigin
    public String saveTask(@RequestBody Map<String, String> map) {
        return taskService.saveTask(map);
    }

    @PostMapping("/daleteTask")
    @CrossOrigin
    public String daleteTask(@RequestBody Map<String, String> map) {
        return taskService.daleteTask(map);
    }

    @PostMapping("/importTask")
    @CrossOrigin
    public String importTask(@RequestBody List<Map<String, String>> list) {
        return taskService.importTask(list);
    }

    @PostMapping("/selectTaskBind")
    @CrossOrigin
    public String selectTaskBind(@RequestBody Map<String, String> map) {
        String returnMapStr = taskService.selectTaskBind(map);
        return returnMapStr;
    }

    @PostMapping("/deleteTaskBind")
    @CrossOrigin
    public String deleteTaskBind(@RequestBody Map<String, String> map) {
        String returnMapStr = taskService.deleteTaskBind(map);
        return returnMapStr;
    }

}
