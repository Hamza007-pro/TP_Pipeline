package com.example.client.services;

import com.example.client.entities.Client;
import com.example.client.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public List<Client> findAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public Client findClientById(Long id) throws Exception {
        return clientRepository.findById(id)
                .orElseThrow(() -> new Exception("Client non trouvé"));
    }

    @Override
    public Client addClient(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public Client updateClient(Long id, Client client) throws Exception {
        Client existingClient = findClientById(id);
        existingClient.setNom(client.getNom());
        existingClient.setAge(client.getAge());
        return clientRepository.save(existingClient);
    }

    @Override
    public void deleteClient(Long id) throws Exception {
        Client client = findClientById(id);
        clientRepository.delete(client);
    }
}
