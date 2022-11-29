package com.ecs.javaecs.service;

import java.util.List;

import com.ecs.javaecs.model.Client;

public interface ClientService {

    List <Client> getAllClients();
    
    Client createClient(Client client);

    Client updateClient(long id, Client client);

    void deleteClient(long id, Client client);

    Client findClientById(long id);

}
