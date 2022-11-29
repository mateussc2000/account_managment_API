package com.ecs.javaecs.service;

//import java.util.List;

import com.ecs.javaecs.model.BankAccount;

public interface BankAccountService {
    
    //List <BankAccount> findAccountsByClient(Client client);

    BankAccount createBankAccount(long id, BankAccount bankAccount);

    BankAccount withdraw(long id, String accountType, double valor);

    BankAccount deposit(long id, String accountType, double valor);

    void deleteAccount(BankAccount bankAccount);
}
