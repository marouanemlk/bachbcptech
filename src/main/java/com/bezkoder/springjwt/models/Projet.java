package com.bezkoder.springjwt.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "projets")
public class Projet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    private String nomProjet;

    @NotBlank
    @Size(max = 20)
    private String codeProjet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /*@ManyToMany(mappedBy = "projetsParticipes", fetch = FetchType.LAZY)
    private Set<User> membres = new HashSet<>();*/

    public Projet() {
    }

    public Projet(String nomProjet, String codeProjet) {
        this.nomProjet = nomProjet;
        this.codeProjet = codeProjet;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomProjet() {
        return nomProjet;
    }

    public void setNomProjet(String nomProjet) {
        this.nomProjet = nomProjet;
    }

    public String getCodeProjet() {
        return codeProjet;
    }

    public void setCodeProjet(String codeProjet) {
        this.codeProjet = codeProjet;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
