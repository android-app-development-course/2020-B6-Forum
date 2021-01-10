package com.example.demo.infra;

import com.example.demo.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


//MARKER User 实体的Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findById(Long id);
    //MARKER: JPA中调用Query语句
//    @Query(value = "select t from User t where t.nickName=?1")
//    User findByNickName(String nickName);

    User findByNickName(String nickName);
    User findByEmail(String email);
}
