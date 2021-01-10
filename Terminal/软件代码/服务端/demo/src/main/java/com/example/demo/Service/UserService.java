package com.example.demo.Service;


import com.example.demo.Entity.User;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

//MARKER User的数据访问层接口
public interface UserService {
    public List<User> getUserList();
    public User findUserById(Long id);
}
