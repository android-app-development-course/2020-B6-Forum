package com.example.demo.infra;

import com.example.demo.Entity.Question;
import com.example.demo.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;


//MARKER Question 实体的Repository
public interface QuestionRepository extends JpaRepository<Question,String> {
    Optional<Question> findById(String id);

//    @Query("select u from Question u where u.content like '%?1%' or u.title = '%?1%'")
    //MARKER：用于关键字搜索
    @Query("select u from Question u where u.content like %?1% or u.title like %?1%")
    List<Question> findByKeyWord(@Param("KeyWord") String KeyWord);

}
