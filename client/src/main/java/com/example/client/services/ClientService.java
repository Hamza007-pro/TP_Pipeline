package com.example.client.services;

import com.example.client.entities.Client;

import java.util.List;

public interface ClientService {
    List<Client> findAllClients();
    Client findClientById(Long id) throws Exception;
    Client addClient(Client client);
    Client updateClient(Long id, Client client) throws Exception;
    void deleteClient(Long id) throws Exception;
}

