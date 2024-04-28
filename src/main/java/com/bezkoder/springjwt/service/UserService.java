package com.bezkoder.springjwt.service;

import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.models.Validation;
import com.bezkoder.springjwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

@Service
public class UserService {
    private UserRepository userRepository;
       private Validationservice validationservice;
    public void activation(Map<String, String> activation) {
        Validation validation = this.validationservice.lireEnFonctionDuCode(activation.get("code"));
        if(Instant.now().isAfter(validation.getExpiration())){
            throw  new RuntimeException("Votre code a expiré");
        }
        User utilisateurActiver = this.userRepository.findById(validation.getUser().getId()).orElseThrow(() -> new RuntimeException("Utilisateur inconnu"));
        utilisateurActiver.setActif(true);
        this.userRepository.save(utilisateurActiver);
    }

    public UserService(UserRepository userRepository, Validationservice validationservice) {
        this.userRepository = userRepository;
        this.validationservice = validationservice;
    }

}
