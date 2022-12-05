package com.ecs.javaecs.repository;

import com.ecs.javaecs.model.BankAccount;
import com.ecs.javaecs.model.Client;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    
    public List<BankAccount> findAllByClient(Client client);
}
