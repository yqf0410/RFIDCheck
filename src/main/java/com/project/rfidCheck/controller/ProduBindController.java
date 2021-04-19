package com.project.rfidCheck.controller;


import com.alibaba.druid.support.json.JSONUtils;
import com.project.rfidCheck.service.ProduBindService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.HashMap;
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
@RequestMapping("/pb")
public class ProduBindController {

    @Autowired
    private ProduBindService produBindService;

    @PostMapping("/select")
    @CrossOrigin
    public String selectProdu(@RequestBody Map<String, String> map) {
        String returnMapStr = produBindService.selectProdu(map);
        return returnMapStr;

    }

    @PostMapping("/save")
    @CrossOrigin
    public String saveProdu(@RequestBody Map<String, String> map) {
        return produBindService.saveProdu(map);
    }

    @PostMapping("/delete")
    @CrossOrigin
    public String daleteProdu(@RequestBody Map<String, String> map) {
        return produBindService.daleteProdu(map);
    }

    @PostMapping("/import")
    @CrossOrigin
    public String importProdu(@RequestBody List<Map<String, String>> list) {
        return produBindService.importProdu(list);
    }

    @PostMapping("/selectBindRecord")
    @CrossOrigin
    public String selectBindRecord(@RequestBody Map<String, String> map) {
        String returnMapStr = produBindService.selectBindRecord(map);
        return returnMapStr;
    }

    @PostMapping("/reset")
    @CrossOrigin
    public String resetBind(@RequestBody Map<String, String> map) {
        return produBindService.resetBind(map);
    }


}
