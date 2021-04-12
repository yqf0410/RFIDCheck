package com.project.rfidCheck.controller;

import com.project.rfidCheck.service.ProduBindService;
import com.project.rfidCheck.service.ProduCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public String selectProdu(){
        return produBindService.selectUnBindProdu();
    }

    @PostMapping("/saveBindProdu")
    @CrossOrigin
    public String saveBindProdu(Map<String, String> map){
        return produBindService.saveBindProdu(map);
    }



}
