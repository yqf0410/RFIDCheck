package com.project.rfidCheck.controller;

import com.project.rfidCheck.service.ProduBindService;
import com.project.rfidCheck.service.ProduCheckService;
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
    private ProduCheckService produCheckService;

    @PostMapping("/selectUnBindProdu")
    @CrossOrigin
    public String selectProdu() {
        return produBindService.selectUnBindProdu();
    }

    @PostMapping("/checkBindRfid")
    @CrossOrigin
    public String saveUser(@RequestParam Map<String, String> map) {
        return produBindService.checkBindRfid(map);
    }

    @PostMapping("/saveBindProdu")
    @CrossOrigin
    public String saveBindProdu(@RequestParam Map<String, String> map) {
        return produBindService.saveBindProdu(map);
    }


}
