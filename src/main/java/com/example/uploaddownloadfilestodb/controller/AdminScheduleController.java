package com.example.uploaddownloadfilestodb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("admin/schedule")
public class AdminScheduleController {


    @GetMapping
    public String showSchedule(){
        return "schedule";
    }


}
