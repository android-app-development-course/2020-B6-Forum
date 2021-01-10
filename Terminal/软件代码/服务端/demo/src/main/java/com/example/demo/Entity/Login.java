package com.example.demo.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Login {
    @Id
    @NotNull(message = "userId is null")
    private Long userId;
    @NotNull(message = "Identifier is null")
    private String identifier;
}
