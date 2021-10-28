package com.project.rfidCheck.controller;

import com.project.rfidCheck.service.ProduBindService;
import com.project.rfidCheck.service.ProduCheckService;
import com.project.rfidCheck.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/pad")
public class PadController {

    @Autowired
    private ProduBindService produBindService;

    @Autowired
    private TaskService taskService;

    //PRODU

    @PostMapping("/selectUnBindProdu")
    @CrossOrigin
    public String selectProdu() {
        return produBindService.selectUnBindProdu();
    }

    @PostMapping("/checkBindRfid")
    @CrossOrigin
    public String checkBindRfid(@RequestParam Map<String, String> map) {
        return produBindService.checkBindRfid(map);
    }

    @PostMapping("/saveBindProdu")
    @CrossOrigin
    public String saveBindProdu(@RequestParam Map<String, String> map) {
        return produBindService.saveBindProdu(map);
    }

    //TASK

    @PostMapping("/selectTaskList")
    @CrossOrigin
    public String selectTaskList(){
        return taskService.selectTaskList();
    }

    @PostMapping("/saveTaskBind")
    @CrossOrigin
    public String saveTaskBind(@RequestParam Map<String, String> map) {
        return taskService.saveTaskBind(map);
    }

    @PostMapping("/selectTaskDetail")
    @CrossOrigin
    public String selectTaskDetail(@RequestParam Map<String, String> map) {
        return taskService.selectTaskDetail(map);
    }
}
