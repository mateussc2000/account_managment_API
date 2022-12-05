package com.ecs.javaecs.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecs.javaecs.dto.ClientDto;
import com.ecs.javaecs.model.Client;
import com.ecs.javaecs.service.ClientService;

@RestController
@RequestMapping("/clientes")
public class ClientController {
    
    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private ClientService clientService;


//Get Methods
@GetMapping
public List<ClientDto> getAllClients() {

    return clientService.getAllClients().stream().map(client -> modelMapper.map(client, ClientDto.class))
        .collect(Collectors.toList());
}

@GetMapping("/{id}")
public ResponseEntity<ClientDto> findClientById(@PathVariable(name = "id") Long id) {
    Client client = clientService.findClientById(id);

    // convert entity to DTO
    ClientDto clientResponse = modelMapper.map(client, ClientDto.class);

    return ResponseEntity.ok().body(clientResponse);
}


//Post Methods
@PostMapping("cadastrar")
public ResponseEntity<ClientDto> createClient(@RequestBody ClientDto clientDto) {

    // convert DTO to entity
    Client clientRequest = modelMapper.map(clientDto, Client.class);

    Client client = clientService.createClient(clientRequest);

    // convert entity to DTO
    ClientDto clientResponse = modelMapper.map(client, ClientDto.class);

    return new ResponseEntity<ClientDto>(clientResponse, HttpStatus.CREATED);
}


//Put Methods
@PutMapping("/{id}")
public ResponseEntity<ClientDto> updateClient(@PathVariable long id, @RequestBody ClientDto clientDto) {

    // convert DTO to Entity
    Client clientRequest = modelMapper.map(clientDto, Client.class);

    Client client = clientService.updateClient(id, clientRequest);

    // convert Entity to DTO
    ClientDto clientResponse = modelMapper.map(client, ClientDto.class);

    return ResponseEntity.ok().body(clientResponse);
}


//Delete Methods
@DeleteMapping("/{id}")
public ResponseEntity<String> deleteClient(@PathVariable(name = "id") Long id) {
    
    clientService.deleteClient(id);

    return ResponseEntity.ok().body("cliente deletado com sucesso");
}

}
