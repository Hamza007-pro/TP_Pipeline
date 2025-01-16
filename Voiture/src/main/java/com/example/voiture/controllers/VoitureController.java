package com.example.voiture.controllers;

import com.example.voiture.Entities.Client;
import com.example.voiture.Entities.Voiture;
import com.example.voiture.repositories.VoitureRepository;
import com.example.voiture.services.ClientService;
import com.example.voiture.services.VoitureService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class VoitureController {

    private final VoitureRepository voitureRepository;
    private final VoitureService voitureService;
    private final ClientService clientService;

    public VoitureController(VoitureRepository voitureRepository, VoitureService voitureService, ClientService clientService) {
        this.voitureRepository = voitureRepository;
        this.voitureService = voitureService;
        this.clientService = clientService;
    }

    @GetMapping("/voitures")
    public List<Voiture> getAllVoitures() {
        List<Voiture> voitures = voitureRepository.findAll();
        voitures.forEach(voiture -> {
            Client client = clientService.clientById(voiture.getIdClient());
            voiture.setClient(client);
        });
        return voitures;
    }

    @PostMapping("/voitures")
    public ResponseEntity<Object> createVoiture(@RequestBody Voiture voiture) {
        try {
            // Verify if client exists
            Client client = clientService.clientById(voiture.getIdClient());
            if (client == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(Map.of(
                                "error", "Client not found",
                                "message", "Cannot create vehicle for non-existent client with ID: " + voiture.getIdClient()
                        ));
            }

            // Basic validation
            if (voiture.getMarque() == null || voiture.getMarque().trim().isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Brand (marque) is required"));
            }

            if (voiture.getMatricule() == null || voiture.getMatricule().trim().isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "License plate (matricule) is required"));
            }

            if (voiture.getModel() == null || voiture.getModel().trim().isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Model is required"));
            }

            // Save the voiture
            Voiture savedVoiture = voitureRepository.save(voiture);

            // Set the client details in the response
            savedVoiture.setClient(client);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(savedVoiture);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "Failed to create vehicle",
                            "message", e.getMessage()
                    ));
        }
    }

    @GetMapping("/voitures/{id}")
    public ResponseEntity<Object> findById(@PathVariable Long id) {
        try {
            Voiture voiture = voitureRepository.findById(id)
                    .orElseThrow(() -> new Exception("Voiture Introuvable"));
            voiture.setClient(clientService.clientById(voiture.getIdClient()));
            return ResponseEntity.ok(voiture);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Voiture not found with ID: " + id);
        }
    }

    @GetMapping("/voitures/client/{id}")
    public ResponseEntity<List<Voiture>> findByClient(@PathVariable Long id) {
        try {
            Client client = clientService.clientById(id);
            if (client == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            List<Voiture> voitures = voitureRepository.findByIdClient(id);
            return ResponseEntity.ok(voitures);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}