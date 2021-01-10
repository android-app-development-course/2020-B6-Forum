package com.example.demo.Service.impl;


import com.example.demo.Entity.*;
import com.example.demo.Service.UserService;
import com.example.demo.State.BackState;
import com.example.demo.infra.*;
import org.apache.tomcat.jni.Time;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Query;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.sql.Struct;
import java.util.*;

@Service
//MARKER 编写User实体的Service实现
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private LoginRepository loginRepository;
    @Autowired
    private EmailCodeRepository emailCodeRepository;
    @Value("${config.mail}")
    private String mailFrom;
    //MARKER:邮箱注册接口
    @Autowired
    JavaMailSenderImpl mailSender;

    //MARKER 重写接口函数
    @Override
    public List<User> getUserList() {
        /*无需转换
        List<User> reUsers=new ArrayList<User>();
        Iterable<User> iterable=userRepository.findAll();
        iterable.forEach((User user)->{reUsers.add(user);});
        return reUsers;*/
        return userRepository.findAll();
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.getOne(id);
    }
    //MARKER: 单独处理Exception，提取message
    private String handleException(Exception ex){
        return ex.getMessage().substring(ex.getMessage().lastIndexOf("messageTemplate")).split("'")[1];
    }
    public BackState askQuestion(Long userId,String title,String content,String picUrl){
        try{
        User user=findUserById(userId);
        Question question=new Question();
        question.setUserId(userId);
        Date date=new Date();
        date.setHours(date.getHours()+8);
        question.setPublishTime(date);
        TimeZone china = TimeZone.getTimeZone("GMT+:08:00");
        question.setTitle(title);
        question.setReported(false);
        if(picUrl!=null){
            question.setPicUrl(picUrl);
        }
        question.setContent(content);
        user.getQuestions().add(question);
        questionRepository.save(question);
        return new BackState(true,"Successful");
        }catch (Exception e){
            return new BackState(false,e.toString());
        }
    }

    public List<Question> getQuestion(Long userId){
        return userRepository.getOne(userId).getQuestions();
    }

    public BackState answerQuestion(Long userId,String QuestionId,String content) {
        try{
        User user=userRepository.getOne(userId);
        Question question=questionRepository.getOne(QuestionId);
        Answer answer=new Answer();
        answer.setContent(content);
        Date date=new Date();
        date.setHours(date.getHours()+8);
        answer.setPublishTime(date);
        answer.setQuestion(question);
        answer.setUserId(userId);
        question.getAnswers().add(answer);
        user.getAnswers().add(answer);
        answerRepository.save(answer);
        return new BackState(true,"Successful");
        }catch (Exception e){
            return new BackState(false,e.toString());
        }
    }

        public BackState Update(Long id, String account,String name, String studentId, String college, String grade, String phoneNumber, String email){
        try {
            User user=userRepository.getOne(id);
            user.setCollege(college);
            user.setNickName(account);
            user.setName(name);
            user.setEmail(email);
            user.setStudentId(studentId);
            user.setCollege(college);
            user.setGrade(grade);
            user.setPhoneNumber(phoneNumber);
            userRepository.saveAndFlush(user);
            return new BackState(true,"Successful");
        } catch (Exception ex){
            return new BackState(false,handleException(ex));
        }
        }

    public BackState Register(String nickName,String password,String Email,int isAdmin){
        try{
            if(userRepository.findByNickName(nickName)!=null){
                return new BackState(false,"repeated");
            }
            User user=new User();
            if(isAdmin==0){
                user.setAdmin(false);
            }else user.setAdmin(true);
            user.setNickName(nickName);
            user.setPassword(password);
            user.setEmail(Email);
            userRepository.save(user);
            //MARKER:成功则返回uid
            return new BackState(true,user.getId().toString());
        }catch (Exception e){
            return new BackState(false,handleException(e));
        }
    }
    public BackState login(String account,String password){
        try {
            User user=userRepository.findByNickName(account);
            if(user.getPassword().equals(password)){
                String uuid=UUID.randomUUID().toString();
                loginRepository.save(new Login(user.getId(),uuid));
                //使用UUid标识登入
                return new BackState(true, user.getId()+"&"+uuid);
            }else{
                return new BackState(false,"Password Error");
            }
        }catch (NoSuchFieldError error){
            return new BackState(false,"No Such Account");
        }
    }

    public BackState logout(Long userId){
        try{
            Login login=loginRepository.getOne(userId);
            loginRepository.delete(login);
            return new BackState(true,"Successful");
        }catch (NoSuchFieldError error){
            return new BackState(false,"Account Error");
        }
    }
    //MARKER REPORTTYPE=0 提问 REPORTTYPE=1 回复
    public BackState report(Long userId,String reportId,int type,int reportType,String content){
        try{
            User user=userRepository.getOne(userId);
            Report report=new Report();
            report.setReportId(reportId);
            report.setReportTime(new Date());
            report.setReportType(reportType);
            if(content!=null){
                report.setContent(content);
            }
            report.setUserID(userId);
            report.setType(type);
            user.getReports().add(report);
            switch (reportType){
                case 1: Answer answer=answerRepository.getOne(reportId);
                        answer.getReports().add(report);
                        break;
                case 0:Question question=questionRepository.getOne(reportId);
                       question.getReports().add(report);
                       break;
            }
            reportRepository.save(report);
            return new BackState(true,"Successful");
        }catch (NoSuchFieldError error){
            return new BackState(false,"Can't find question or answer");
        }
    }
    //MARKER：找回密码
    public BackState findPassword(String account,String email)
    {
        //首先判断是否存在用户名
        User nowUser;
        if((nowUser=userRepository.findByNickName(account))==null){
                return new BackState(false,"account not find");
        }else{
            if(!nowUser.getEmail().equals(email)){
                return new BackState(false,"email not find");
            }
            try{
                Random random=new Random();
                Date date=new Date();
                random.setSeed(date.getTime()%10);//将当前时间设为种子
                String emailServiceCode="";
                for(int i=0;i<4;i++){
                    emailServiceCode+=String.valueOf(random.nextInt(9)+1);
                }
                SimpleMailMessage message=new SimpleMailMessage();
                message.setSubject("--华师帮帮平台注册验证码--");
                message.setText("找回密码，您的验证码是:"+emailServiceCode);
                message.setFrom(mailFrom);
                message.setTo(email);
                mailSender.send(message);
                //将Code存入EmailCode中
                EmailCode emailCode=new EmailCode(nowUser.getId(),emailServiceCode);
                //判断是否已存在，已存在则更新
                emailCodeRepository.saveAndFlush(emailCode);
                return new BackState(true,"successful");
            }catch (Exception e){
                return new BackState(false,"error to send email");
            }

        }
    }

    //MARKER:重设密码
    public BackState resetPassword(String account,String password,String emailCode){
        User nowUser=userRepository.findByNickName(account);
        if(nowUser!=null) {
            try {
                EmailCode code = emailCodeRepository.getOne(nowUser.getId());
                if (emailCode.equals(code.getResetCode())) {
                    nowUser.setPassword(password);
                    userRepository.saveAndFlush(nowUser);
                    return new BackState(true,"successful");
                }else{
                    return new BackState(false,"wrong code");
            }
        }catch (NoSuchFieldError e){
                return new BackState(false,"no such code");
            }
        }else{
            return new BackState(false,"no such user");
        }
    }
}
