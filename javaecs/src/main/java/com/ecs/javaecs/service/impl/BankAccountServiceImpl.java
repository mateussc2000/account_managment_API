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
import com.ecs.javaecs.service.ClientService;

@Service("bankAccountService")
public class BankAccountServiceImpl implements BankAccountService{
    
    @Autowired
    private BankAccountRepository repository;

    @Autowired
    private ClientService clientService;

    @Override
    public List<BankAccount> findAccountsByClient(Client clientRequest) {
        Client client = clientService.findClientById(clientRequest.getClientId());

        try {
            return repository.findAllByClient(client);
        } catch (DataAccessException e) {
            throw new ResourceNotFoundException("Não foi possível encontrar contas para este cliente.");
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
    public BankAccount createBankAccount(long id, BankAccount bankAccountRequest) {
        Client client = clientService.findClientById(id);

        BankAccount bankAccount = new BankAccount(client, bankAccountRequest.getAccountType(), bankAccountRequest.getBalance());
       
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
    public BankAccount withdraw(long id, double valor) {
        BankAccount bankAccount = findAccountById(id);

        if (bankAccount.getBalance() - valor < 0) {
            throw new InternalError("Cliente não possui saldo suficiente");
        }  
        
        bankAccount.setBalance(valor);
        
        try {
            return repository.save(bankAccount);
        } catch (DataAccessException e) {
            throw new InternalError("Não foi possível realizar o saque");
        }
    }

    @Transactional
    @Override
    public BankAccount deposit(long id, double valor) {
        BankAccount bankAccount = findAccountById(id);

        bankAccount.setBalance(valor);

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
