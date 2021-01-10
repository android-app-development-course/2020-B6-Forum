package com.example.demo.Controller;

import com.example.demo.Entity.Answer;
import com.example.demo.Service.impl.AnswerServiceImpl;
import com.example.demo.State.BackState;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/answer")
public class AnswerController {
    @Resource
    AnswerServiceImpl answerService;
    @RequestMapping("/get")
    public Answer getAnswerById(String id){
        Answer Answer=answerService.findAnswerById(id);
        return Answer;
    }


    @RequestMapping(value = "/praise")
    public BackState Praise(String id){
        return answerService.Praise(id);
    }

    @RequestMapping(value = "/getPraise")
    public Long getPraise(String id){
        return answerService.getPraise(id);
    }

    @RequestMapping(value = "/accuse")
    public BackState accuse(String id){
        return answerService.accuse(id);
    }
}
