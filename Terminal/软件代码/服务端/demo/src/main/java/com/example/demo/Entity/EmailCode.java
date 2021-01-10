package com.example.demo.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailCode {
    @Id
    @NotNull(message = "userId is null")
    private Long userId;
    @NotNull(message = "resetCode is null")
    private String resetCode;
}