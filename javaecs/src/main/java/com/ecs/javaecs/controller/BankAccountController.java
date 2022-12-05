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
import com.ecs.javaecs.model.BankAccount;
import com.ecs.javaecs.dto.BankAccountDto;
import com.ecs.javaecs.service.BankAccountService;
import com.ecs.javaecs.service.ClientService;

@RestController
@CrossOrigin("*")
@RequestMapping("/conta")
public class BankAccountController {

    @Autowired
    private BankAccountService bankAccountService;
    
    @Autowired
    private ClientService clientService;

    @Autowired
    private ModelMapper modelMapper;

//Get Methods
@GetMapping("/cliente/{id}")
public List<BankAccountDto> findAccountsByClient(@PathVariable long id) {
    Client client = clientService.findClientById((long) id);

    return bankAccountService.findAccountsByClient(client).stream().map(bankAccounts -> modelMapper.map(bankAccounts, BankAccountDto.class))
    .collect(Collectors.toList());
}

@GetMapping("/{id}")
public BankAccount findAccountById(@PathVariable(name = "id") long id) {
    return bankAccountService.findAccountById(id);
}


//Post Methods
@PostMapping("/cadastrar/cliente/{id}")
public ResponseEntity<BankAccountDto> createBankAccount(@PathVariable long id, @RequestBody BankAccountDto bankAccountDto) {
    
    Client client = clientService.findClientById((long) id);

    BankAccount bankAccountRequest = modelMapper.map(bankAccountDto, BankAccount.class);
    
    BankAccount bankAccount = bankAccountService.createBankAccount(client, bankAccountRequest);

    BankAccountDto bankAccountResponse = modelMapper.map(bankAccount, BankAccountDto.class);

    return new ResponseEntity<BankAccountDto>(bankAccountResponse, HttpStatus.CREATED);
}

//Put Methods
@PutMapping("/{id}/saque")
public ResponseEntity<BankAccountDto> withdraw(@PathVariable long id, @RequestBody double amount) {
    BankAccount bankAccount = bankAccountService.withdraw(id, amount);
    
    BankAccountDto bankAccountResponse = modelMapper.map(bankAccount, BankAccountDto.class);

    return new ResponseEntity<BankAccountDto>(bankAccountResponse, HttpStatus.OK);
}

@PutMapping("/{id}/depósito")
public ResponseEntity<BankAccountDto> deposit(@PathVariable long id, @RequestBody double amount) {
    
    BankAccount bankAccount = bankAccountService.deposit(id, amount);
    
    BankAccountDto bankAccountResponse = modelMapper.map(bankAccount, BankAccountDto.class);

    return new ResponseEntity<BankAccountDto>(bankAccountResponse, HttpStatus.OK);
}


//Delete Methods
@DeleteMapping("/{id}")
public ResponseEntity<String> deleteBankAccount(@PathVariable(name = "id") long id) {
    BankAccount bankAccount = bankAccountService.findAccountById(id);

    bankAccountService.deleteAccount(bankAccount);

    return ResponseEntity.ok().body("Conta deletado com sucesso");
}
}
