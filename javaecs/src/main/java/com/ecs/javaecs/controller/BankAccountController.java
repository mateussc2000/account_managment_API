package com.ecs.javaecs.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecs.javaecs.model.Client;
import com.ecs.javaecs.dto.ClientDto;
import com.ecs.javaecs.model.BankAccount;
import com.ecs.javaecs.dto.BankAccountDto;
import com.ecs.javaecs.service.BankAccountService;

@RestController
@CrossOrigin("*")
@RequestMapping("/conta")
public class BankAccountController {

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private ModelMapper modelMapper;

//Get Methods
@GetMapping
public List<BankAccountDto> findAccountsByClient(@RequestBody ClientDto clientDto) {
    Client client = modelMapper.map(clientDto, Client.class);

    return bankAccountService.findAccountsByClient(client).stream().map(bankAccounts -> modelMapper.map(bankAccounts, BankAccountDto.class))
    .collect(Collectors.toList());
}

@GetMapping
public BankAccount findAccountById(long id) {
    return bankAccountService.findAccountById(id);
}


//Post Methods
@PostMapping
public ResponseEntity<BankAccountDto> createBankAccount(long id, @RequestBody BankAccountDto bankAccountDto) {
    
    BankAccount bankAccountRequest = modelMapper.map(bankAccountDto, BankAccount.class);
    
    BankAccount bankAccount = bankAccountService.createBankAccount(id, bankAccountRequest);

    BankAccountDto bankAccountResponse = modelMapper.map(bankAccount, BankAccountDto.class);

    return new ResponseEntity<BankAccountDto>(bankAccountResponse, HttpStatus.CREATED);
}

//Put Methods
@PutMapping("${id}")
public ResponseEntity<BankAccountDto> withdraw(@PathVariable(name = "id") long id, double valor) {
    BankAccount bankAccount = bankAccountService.withdraw(id, valor);
    
    BankAccountDto bankAccountResponse = modelMapper.map(bankAccount, BankAccountDto.class);

    return new ResponseEntity<BankAccountDto>(bankAccountResponse, HttpStatus.OK);
}

@PutMapping("${id}")
public ResponseEntity<BankAccountDto> deposit(@PathVariable(name = "id") long id, double valor) {
    BankAccount bankAccount = bankAccountService.deposit(id, valor);
    
    BankAccountDto bankAccountResponse = modelMapper.map(bankAccount, BankAccountDto.class);

    return new ResponseEntity<BankAccountDto>(bankAccountResponse, HttpStatus.OK);
}


//Delete Methods
@DeleteMapping("/{id}")
public ResponseEntity<String> deleteBankAccount(@PathVariable(name = "id") long id) {
    BankAccount bankAccount = bankAccountService.findAccountById(id);

    bankAccountService.deleteAccount(bankAccount);

    return ResponseEntity.ok().body("Cliente deletado com sucesso");
}
}
