package com.example.voiture;

import com.example.voiture.Entities.Client;
import com.example.voiture.Entities.Voiture;
import com.example.voiture.repositories.VoitureRepository;
import com.example.voiture.services.ClientService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class VoitureApplication {

    public static void main(String[] args) {
        SpringApplication.run(VoitureApplication.class, args);
    }

    @Bean
    CommandLineRunner initialiserBaseH2(VoitureRepository voitureRepository, ClientService clientService) {
        return args -> {
            try {
                // Fetch clients from the client service
                Client c1 = clientService.clientById(2L);
                Client c2 = clientService.clientById(1L);

                // Display client information for debugging
                System.out.println("Client 1: " + c1);
                System.out.println("Client 2: " + c2);

                // Save voitures with correct constructor and client object
                voitureRepository.save(new Voiture(null, "Toyota", "A 25 333", "Corolla", c2.getId(), c2));
                voitureRepository.save(new Voiture(null, "Renault", "B 6 3456", "Megane", c2.getId(), c2));
                voitureRepository.save(new Voiture(null, "Peugeot", "A 55 4444", "301", c1.getId(), c1));
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }
}
