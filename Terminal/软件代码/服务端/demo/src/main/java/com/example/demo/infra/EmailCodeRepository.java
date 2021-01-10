package com.example.demo.infra;

import com.example.demo.Entity.EmailCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailCodeRepository extends JpaRepository<EmailCode,Long> {

}
