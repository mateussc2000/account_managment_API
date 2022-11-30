package com.ecs.javaecs.dto;

import jakarta.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class ClientDto {
    
    @NotBlank
    private String name;

    @NotBlank
    private String cpf;
}
