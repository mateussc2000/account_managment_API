package com.ecs.javaecs.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecs.javaecs.model.BankAccount;
import com.ecs.javaecs.model.Client;
import com.ecs.javaecs.repository.BankAccountRepository;
import com.ecs.javaecs.service.BankAccountService;

@Service("bankAccountService")
public class BankAccountServiceImpl implements BankAccountService{
    
    @Autowired
    private BankAccountRepository repository;

   
    @Override
    public List<BankAccount> findAccountsByClient(Client clientRequest) {
      
        try {
           
            List<BankAccount> accounts = repository.findAllByClient(clientRequest); 
            
            if (accounts.isEmpty()) 
            throw new InternalError("Cliente não possui contas cadastradas");
            
            return accounts;

        } catch (NullPointerException e) {
            throw new NullPointerException("Insira o ID do cliente");
      
        } catch (DataAccessException e) {
            throw new ResourceNotFoundException("Não foi acessar o banco de dados.");
        }
    }

    @Override
    public BankAccount findAccountById(long id) {
        Optional<BankAccount> result = repository.findById(id);
        
        if (result.isPresent()) {
            return result.get();
        } else {
            throw new ResourceNotFoundException("Conta não existente!");
        }
    }

    @Override
    public BankAccount createBankAccount(Client client ,BankAccount bankAccountRequest) {
      
        BankAccount bankAccount = new BankAccount(client, bankAccountRequest.getAccountType(), 0);
       
        Set<BankAccount> accounts = new HashSet<>();
        accounts.add(bankAccount);

        try {
            return repository.save(bankAccount);
        } catch (DataAccessException e) {
            throw new InternalError("Não foi possível acessar o banco de dados");
        }
    }
    
    @Transactional
    @Override
    public BankAccount withdraw(long id, double amount) {
        BankAccount bankAccount = findAccountById(id);

        if (bankAccount.getBalance() - amount < 0) {
            throw new InternalError("Cliente não possui saldo suficiente");
        }  
        
        bankAccount.setBalance(bankAccount.getBalance() - amount);
        
        try {
            return repository.save(bankAccount);
        } catch (DataAccessException e) {
            throw new InternalError("Não foi possível realizar o saque");
        }
    }

    @Transactional
    @Override
    public BankAccount deposit(long id, double amount) {
        BankAccount bankAccount = findAccountById(id);

        bankAccount.setBalance(bankAccount.getBalance() + amount);

        try {
            return repository.save(bankAccount);
        } catch (DataAccessException e) {
            throw new InternalError("Não foi possível realizar o depósito");
        }
    }

    @Override
    public void deleteAccount(BankAccount bankAccountRequest) {
        BankAccount bankAccount = findAccountById(bankAccountRequest.getAccountId());

        try {
            repository.delete(bankAccount);
        } catch (DataAccessException e) {
            throw new InternalError("Não foi possível apagar a conta");
        }
        
    }
}
