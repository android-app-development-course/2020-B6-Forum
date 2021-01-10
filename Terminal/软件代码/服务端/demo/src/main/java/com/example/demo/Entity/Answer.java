package com.example.demo.Entity;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.*;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(value={"question"})
public class Answer implements Serializable {

    @Id
    @NotNull(message= "answerID is Empty")
    @GeneratedValue(generator = "system-uuid")//默认由程序控制
    @GenericGenerator(name="system-uuid", strategy = "uuid")//主键生成策略生成器
    private String id;
    private String content;
    @Min(0)
    @Value("0")
    private long thumpsUpNumber;
    @Min(0)
    @Value("0")
    private long treadNumber;
    @Value("false")
    private boolean isReported;//考虑下举报分一个表
    private Date publishTime;
    @ManyToOne
    private Question question;
    @OneToMany
    private List<Report> reports;
    private Long userId;
}
