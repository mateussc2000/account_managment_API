package com.ecs.javaecs.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BankAccountDto {
    
    @NotBlank
    private String accountType;

    @NotBlank
    private double balance;
}
