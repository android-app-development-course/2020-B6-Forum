package com.example.demo.infra;

import com.example.demo.Entity.Login;
import org.springframework.data.jpa.repository.JpaRepository;



public interface LoginRepository extends JpaRepository<Login,Long> {

}
