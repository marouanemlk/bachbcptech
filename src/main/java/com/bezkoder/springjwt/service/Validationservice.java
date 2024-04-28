package com.bezkoder.springjwt.service;

import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.models.Validation;
import com.bezkoder.springjwt.repository.ValidationRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Random;

import static java.time.temporal.ChronoUnit.MINUTES;
@Service
public class Validationservice {
    private ValidationRepository validationRepository;
    private Notificationservice notificationservice;

    public void enregistrer(User utilisateur) {
        Validation validation = new Validation();
        validation.setUser(utilisateur);
        Instant creation = Instant.now();
        validation.setCreation(creation);
        Instant expiration = creation.plus(10, MINUTES);
        validation.setExpiration(expiration);
        Random random = new Random();
        int randomInteger = random.nextInt(999999);
        String code = String.format("%06d", randomInteger);

        validation.setCode(code);
        this.validationRepository.save(validation);
        this.notificationservice.envoyer(validation);
    }

    public Validation lireEnFonctionDuCode(String code) {
        return this.validationRepository.findByCode(code).orElseThrow(() -> new RuntimeException("Votre code est invalide"));
    }

    public Validationservice(ValidationRepository validationRepository, Notificationservice notificationservice) {
        this.validationRepository = validationRepository;
        this.notificationservice = notificationservice;
    }
}
