package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
//MARKER 忽略user的json，防止查询时发生循环
//@JsonIgnoreProperties(value={"user"})
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
public class Question implements Serializable {
    @Id
    @NotNull(message="questionID is Empty")
    @GeneratedValue(generator = "system-uuid")//默认由程序控制
    @GenericGenerator(name="system-uuid", strategy = "uuid") //因为id定义为String所以这里使用UUid
    private String id;
    @Column(length = 30)
    private String title;
    private String content;
    @Value("false")
    private boolean isReported;//考虑下举报分一个表
    private String picUrl;
    @Min(0)
    @Value("0")
    private long viewNumber;
    @Min(0)
    @Value("0")
    private long thumpsUpNumber;
    private Date publishTime;
    @OneToMany
    private List<Answer> answers;
    @OneToMany
    private List<Report> reports;
    @NotNull(message = "userId is null")
    private Long userId;
}
