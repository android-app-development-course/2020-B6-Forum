package com.example.demo.Service.impl;


import com.example.demo.Entity.Answer;
import com.example.demo.Entity.Question;
import com.example.demo.Service.QuestionService;
import com.example.demo.State.BackState;
import com.example.demo.infra.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
//MARKER 编写User实体的Service实现
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    private QuestionRepository questionRepository;
    //MARKER 重写接口函数
    @Override
    public List<Question> getQuestionList() {
        /*无需转换
        List<User> reUsers=new ArrayList<User>();
        Iterable<User> iterable=userRepository.findAll();
        iterable.forEach((User user)->{reUsers.add(user);});
        return reUsers;*/
        return questionRepository.findAll();
    }
    @Override
    public Question findQuestionById(String id) {
        return questionRepository.findById(id).get();
    }


    public List<Question> KeywordSearch(String KeyWord){
       return questionRepository.findByKeyWord(KeyWord);
    }

    public BackState accuse(String id){
        try{
            Question question=questionRepository.getOne(id);
            question.setReported(true);
            questionRepository.save(question);
            return new BackState(true,"A Successful Accuse");
        }catch (Exception e){
            return new BackState(false,e.toString());
        }
    }

}
