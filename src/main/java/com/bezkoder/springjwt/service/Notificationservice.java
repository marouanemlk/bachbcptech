package com.bezkoder.springjwt.service;

import com.bezkoder.springjwt.models.Validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class Notificationservice {
    private final JavaMailSender javaMailSender;

    @Autowired
    public Notificationservice(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void envoyer(Validation validation) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("no-reply@chillo.tech");
        message.setTo(validation.getUser().getEmail());
        message.setSubject("Votre code d'activation");

        String texte = String.format(
                "Bonjour %s, <br /> Votre code d'action est %s; A bientôt",
                validation.getUser().getUsername(),
                validation.getCode()
        );
        message.setText(texte);

        javaMailSender.send(message);
    }


}

