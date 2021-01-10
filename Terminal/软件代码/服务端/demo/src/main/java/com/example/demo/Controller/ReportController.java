package com.example.demo.Controller;

import com.example.demo.Entity.Report;
import com.example.demo.Service.impl.ReportServiceImpl;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/report")
public class ReportController {
    @Resource
    ReportServiceImpl reportService;
    @RequestMapping("/get")
    public Report getReportById(String id){
        Report Report=reportService.findReportById(id);
        return Report;
    }
}
