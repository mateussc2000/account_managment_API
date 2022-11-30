package com.ecs.javaecs.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.ecs.javaecs.model.BankAccount;
import com.ecs.javaecs.model.Client;
import com.ecs.javaecs.repository.ClientRepository;
import com.ecs.javaecs.service.BankAccountService;
import com.ecs.javaecs.service.ClientService;

@Service("clientService")
public class ClientServiceImpl implements ClientService {
    
    @Autowired
    private ClientRepository repository;
    
    @Autowired
    private BankAccountService bankAccountService;

    @Override
    public List<Client> getAllClients() {
        return repository.findAll();
    }

    @Override
    public Client createClient(Client client) {
        return repository.save(client);
    }

    @Override
    public Client updateClient(long id, Client clientRequest) {
        Client client = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não existe!"));

        client.setName(clientRequest.getName());
        //CPF can't be changed here

        return repository.save(client);
    }

    @Override
    public void deleteClient(long id) {
        Client client = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não existe!"));
        
        Iterator<BankAccount> iteratorAccount = client.getBankAccount().iterator();
        
        while(iteratorAccount.hasNext()) {
            BankAccount account = iteratorAccount.next();

            if (account.getBalance() != 0) {
                throw new InternalError("Conta "+ account.getAccountType() + " do cliente ainda com saldo! Favor sacar antes de continuar.");
            }

            iteratorAccount.remove();
            bankAccountService.deleteAccount(account);
        }
        
        try {
            repository.delete(client);
        } catch (DataAccessException e) {
            throw new InternalError("Falha ao deletar o cliente");
        }
    }

    @Override
    public Client findClientById(long id) {
        Optional<Client> result = repository.findById(id);

        if (result.isPresent()) {
            return result.get();
        } else {
            throw new ResourceNotFoundException("Cliente não existe");
        }
    }
}
