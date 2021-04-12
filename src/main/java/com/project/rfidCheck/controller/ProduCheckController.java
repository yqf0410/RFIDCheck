package com.project.rfidCheck.controller;


import com.project.rfidCheck.entity.ProduCheck;
import com.project.rfidCheck.service.ProduBindService;
import com.project.rfidCheck.service.ProduCheckService;
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
 * @since 2021-03-31
 */
@RestController
@RequestMapping("/pc")
public class ProduCheckController {

    @Autowired
    private ProduCheckService produCheckService;

    @PostMapping("/select")
    @CrossOrigin
    public String selectCheckRecord(@RequestBody Map<String, String> map) {
        String returnMapStr = produCheckService.selectCheckRecord(map);
        return returnMapStr;
    }
}
