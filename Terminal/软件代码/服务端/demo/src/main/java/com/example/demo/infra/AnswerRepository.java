package com.example.demo.infra;

import com.example.demo.Entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


//MARKER Answer实体的Repository
public interface AnswerRepository extends JpaRepository<Answer,String>{
    //实现对应User
    Optional<Answer> findById(String id);

}
