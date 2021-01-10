package com.example.demo.Controller;

import com.example.demo.Entity.Question;
import com.example.demo.Entity.User;
import com.example.demo.Service.QuestionService;
import com.example.demo.Service.impl.AnswerServiceImpl;
import com.example.demo.Service.impl.QuestionServiceImpl;
import com.example.demo.Service.impl.ReportServiceImpl;
import com.example.demo.Service.impl.UserServiceImpl;
import com.example.demo.State.BackState;
import com.example.demo.infra.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

//MARKER 允许跨域请求
@CrossOrigin(origins = "*",maxAge = 3600)
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    UserServiceImpl userService;

    @RequestMapping("/getAll")
    public List<User> getAllUsers(){
        return userService.getUserList();
    }
    @RequestMapping("/get")
    public User getUserById(Long id){
        return userService.findUserById(id);
    }
    @RequestMapping("/register")
//    public BackState Register(String id,String nickName,String password,String name,String studentId,String college,String grade,String phoneNumber,String email,int isAdmin){
//        return userService.Register(id, nickName, password, name, studentId, college, grade, phoneNumber, email, isAdmin);
//          return userService.Register(nickName,password,email,);
//    }
    public BackState Register(String account,String password,String email,int isAdmin){
        return userService.Register(account,password,email,isAdmin);
    }
    @RequestMapping(value = "/update")
    public BackState Update(Long id,String account,String name,String studentId,String college,String grade,String phoneNumber,String email){
        return userService.Update(id, account,name, studentId, college, grade, phoneNumber, email);
    }
    @RequestMapping(value = "/ask")
    public BackState askQuestion(Long userId,String title,String content,String picUrl){
        return userService.askQuestion(userId,title,content,picUrl);
    }

    @RequestMapping(value = "/question")
    public List<Question> getQuestion(Long userId){
        return userService.getQuestion(userId);
    }

    @RequestMapping(value = "/answer")
    public BackState answerQuestion(Long userId,String questionId,String content){
        return userService.answerQuestion(userId,questionId,content);
    }

    @RequestMapping(value = "/login")
    public BackState Login(String account ,String password){
        return userService.login(account, password);
    }

    @RequestMapping(value = "/logout")
    public BackState Logout(Long userId){
        return userService.logout(userId);
    }

    //MARKER TYPE=0 提问 TYPE=1 回复
    @RequestMapping(value = "/report")
    public BackState report(Long userId,String reportId,int type,int reportType,String content){
            return userService.report(userId, reportId, type,reportType, content);
    }

    @RequestMapping(value = "/findPassword")
    public BackState findPassword(String account,String email){
        return userService.findPassword(account,email);
    }

    @RequestMapping(value = "/resetPassword")
    public BackState resetPassword(String account,String password,String emailCode){
        return userService.resetPassword(account, password, emailCode);
    }
//    //MARKER:统一的异常处理
//    @ResponseBody
//    @ExceptionHandler(BindException.class)
//    public BackState exceptionHandler(BindException e)
//    {
//        return new BackState(false,e.getBindingResult().getFieldError().getDefaultMessage());
//    }
}
