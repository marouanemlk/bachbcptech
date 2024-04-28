package com.bezkoder.springjwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.bezkoder.springjwt.models.Projet;
import java.util.Optional;

@Repository
public interface ProjetRepository extends JpaRepository<Projet, Long> {
    // Recherche d'un projet par son code
    Optional<Projet> findByCodeProjet(String codeProjet);

    // Recherche d'un projet par son nom
    Optional<Projet> findByNomProjet(String nomProjet);

    // Vérification de l'existence d'un projet par son code
    boolean existsByCodeProjet(String codeProjet);

    // Vérification de l'existence d'un projet par son nom
    boolean existsByNomProjet(String nomProjet);
}
