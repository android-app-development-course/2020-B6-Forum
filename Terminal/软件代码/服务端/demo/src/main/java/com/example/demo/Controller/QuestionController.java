package com.example.demo.Controller;

import com.example.demo.Entity.Question;
import com.example.demo.Entity.User;
import com.example.demo.Service.impl.QuestionServiceImpl;
import com.example.demo.State.BackState;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/question")
public class QuestionController {
    @Resource
    QuestionServiceImpl questionService;
    @RequestMapping("/get")
    public Question getQuestionById(String id){
        Question question=questionService.findQuestionById(id);
        question.setViewNumber(question.getViewNumber()+1);
        return question;
    }

    @RequestMapping("/getAll")
    public List<Question> getAllQuestions(){
        return questionService.getQuestionList();
    }

    @RequestMapping("/keywordSearch")
    public List<Question> KeywordSearch(String keyword){
        return questionService.KeywordSearch(keyword);
    }

    @RequestMapping(value = "/accuse")
    public BackState accuse(String id){
        return questionService.accuse(id);
    }

}
