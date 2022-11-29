package com.ecs.javaecs.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BankAccountDTO {
    
    @NotBlank
    private String accountType;

    @NotBlank
    private double balance;
}
