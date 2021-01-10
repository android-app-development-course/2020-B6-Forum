package com.example.demo.Service.impl;


import com.example.demo.Entity.Answer;
import com.example.demo.Service.AnswerService;
import com.example.demo.State.BackState;
import com.example.demo.infra.AnswerRepository;
import com.example.demo.infra.AnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
//MARKER 编写Answer实体的Service实现
public class AnswerServiceImpl implements AnswerService {
    @Autowired
    private AnswerRepository answerRepository;
    //MARKER 重写接口函数
    @Override
    public List<Answer> getAnswerList() {
        /*无需转换
        List<Answer> reAnswers=new ArrayList<Answer>();
        Iterable<Answer> iterable=AnswerRepository.findAll();
        iterable.forEach((Answer Answer)->{reAnswers.add(Answer);});
        return reAnswers;*/
        return answerRepository.findAll();
    }

    @Override
    public Answer findAnswerById(String id) {
        return answerRepository.findById(id).get();
    }

    public BackState Praise(String id){
        try{
            Answer answer=answerRepository.getOne(id);
            Long a=answer.getThumpsUpNumber()+1;
            answer.setThumpsUpNumber(a);
            answerRepository.save(answer);
            return new BackState(true,"A Successful Praise");
        }catch (Exception e){
            return new BackState(false,e.toString());
        }
    }

    public Long getPraise(String id){
        return answerRepository.getOne(id).getThumpsUpNumber();
    }

    public BackState accuse(String id){
        try{
        Answer answer=answerRepository.getOne(id);
        answer.setReported(true);
        answerRepository.save(answer);
        return new BackState(true,"A Successful Accuse");
    }catch (Exception e){
        return new BackState(false,e.toString());
    }
    }
}
