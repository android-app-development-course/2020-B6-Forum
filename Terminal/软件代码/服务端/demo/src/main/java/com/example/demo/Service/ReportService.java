package com.example.demo.Service;


import com.example.demo.Entity.Report;

import java.util.List;

//MARKER Report的数据访问层接口
public interface ReportService {
    public List<Report> getReportList();
    public Report findReportById(String id);
}
