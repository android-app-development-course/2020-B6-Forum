package com.example.demo.Service.impl;


import com.example.demo.Entity.Report;
import com.example.demo.Service.ReportService;
import com.example.demo.infra.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
//MARKER 编写User实体的Service实现
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ReportRepository reportRepository;
    //MARKER 重写接口函数
    @Override
    public List<Report> getReportList() {
        /*无需转换
        List<User> reUsers=new ArrayList<User>();
        Iterable<User> iterable=userRepository.findAll();
        iterable.forEach((User user)->{reUsers.add(user);});
        return reUsers;*/
        return reportRepository.findAll();
    }

    @Override
    public Report findReportById(String id) {
        return reportRepository.findById(id).get();
    }
}
