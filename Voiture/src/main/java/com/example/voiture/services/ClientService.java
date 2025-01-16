package com.example.voiture.services;

import com.example.voiture.Entities.Client;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ClientService {

    private final WebClient webClient;

    public ClientService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Client clientById(Long id) {
        return webClient.get()
                .uri("/clients/{id}", id)
                .retrieve()
                .bodyToMono(Client.class)
                .block(); // Blocking call for simplicity; consider reactive programming for scalability
    }
}
