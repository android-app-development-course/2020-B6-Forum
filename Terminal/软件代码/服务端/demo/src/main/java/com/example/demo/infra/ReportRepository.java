package com.example.demo.infra;

import com.example.demo.Entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


//MARKER Report 实体的Repository
public interface ReportRepository extends JpaRepository<Report,String> {
    //实现对应Report
    Optional<Report> findById(String id);

}
