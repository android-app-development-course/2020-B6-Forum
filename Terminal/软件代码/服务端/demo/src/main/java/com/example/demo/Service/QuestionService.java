package com.example.demo.Service;


import com.example.demo.Entity.Question;

import java.util.List;

//MARKER Question的数据访问层接口
public interface QuestionService {
    public List<Question> getQuestionList();
    public Question findQuestionById(String id);
}
