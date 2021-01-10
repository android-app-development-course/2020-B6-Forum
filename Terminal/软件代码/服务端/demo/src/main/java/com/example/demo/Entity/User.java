package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Query;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
//MARKER: 防止使用ID查询且List为null时报错
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "nickName is empty")
    @Column(length = 10)
    private String nickName;
    @NotNull(message = "password is empty")
    @Column(length = 15)
    private String password;
    @Column(length = 15)
    private String name;
    @Column(length = 11)
    private String studentId;
    @Column(length = 12)
    private String college;
    @Column(length = 10)
    private String grade;
    @Column(length = 11)
    private String phoneNumber;
    @Column(length = 25)
    @NotNull(message = "email is empty")
    @Email(message = "email is illegal")
    private String email;
    @Value("100")
    private int credit;
    //MARKER 是否为管理员
    private boolean isAdmin;
    //生成的User_Anaswer表只会在多对多时创建对象
    @OneToMany
    private List<Answer> Answers;
    @OneToMany
    private List<Question> Questions;
    @OneToMany
    private List<Report> Reports;

}
