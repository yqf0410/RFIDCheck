package com.project.rfidCheck.controller;


import com.project.rfidCheck.service.TaskLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author frank.yang
 * @since 2021-04-08
 */
@RestController
@RequestMapping("/tl")
public class TaskLogController {

    @Autowired
    private TaskLogService taskLogService;

    @PostMapping("/select")
    @CrossOrigin
    public String selectTaskLog(@RequestBody Map<String, String> map) {
        String returnMapStr = taskLogService.selectTaskLog(map);
        return returnMapStr;

    }
}
