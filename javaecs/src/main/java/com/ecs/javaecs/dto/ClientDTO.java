package com.ecs.javaecs.dto;

import java.util.Set;

import com.ecs.javaecs.model.BankAccount;

import jakarta.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class ClientDto {
    @NotBlank
    private Long id;
    
    @NotBlank
    private String name;

    @NotBlank
    private String cpf;

    public Set<BankAccount> bankAccount;
}
