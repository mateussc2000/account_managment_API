package com.ecs.javaecs.service;

import java.util.List;

import com.ecs.javaecs.model.Client;
import com.ecs.javaecs.model.BankAccount;

public interface BankAccountService {
    
    List <BankAccount> findAccountsByClient(Client client);

    BankAccount findAccountById(long accountId);

    BankAccount createBankAccount(long clientId, BankAccount bankAccount);

    BankAccount withdraw(long accountId, double valor);

    BankAccount deposit(long accountId, double valor);

    void deleteAccount(BankAccount bankAccount);
}
