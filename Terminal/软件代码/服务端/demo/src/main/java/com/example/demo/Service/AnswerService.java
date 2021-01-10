package com.example.demo.Service;


import com.example.demo.Entity.Answer;

import java.util.List;

//MARKER Answer的数据访问层接口
public interface AnswerService {
    public List<Answer> getAnswerList();
    public Answer findAnswerById(String id);
}
