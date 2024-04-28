package com.bezkoder.springjwt.controllers;

import com.bezkoder.springjwt.models.*;
import com.bezkoder.springjwt.payload.request.ProjetRequest;
import com.bezkoder.springjwt.payload.request.SignupRequest;
import com.bezkoder.springjwt.repository.*;
import com.bezkoder.springjwt.service.Notificationservice;
import com.bezkoder.springjwt.service.UserService;
import com.bezkoder.springjwt.service.Validationservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import com.bezkoder.springjwt.payload.response.MessageResponse;

import javax.transaction.Transactional;



@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private ValidationRepository validationRepository;
    @Autowired
    private Notificationservice notificationservice;
    @Autowired
    private Validationservice validationservice;
    @Autowired
    private UserService userService;
    @Autowired
    private ProjetRepository projetRepository;
    @Autowired
    private ProjectAssignmentRepository projectAssignmentRepository;



    // Get all users
    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    @GetMapping("/listProjects")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Projet> getAllProjects() {
        return projetRepository.findAll();
    }

   // @GetMapping("/listProjectsAssignment")
   // @PreAuthorize("hasRole('ADMIN')")
   /* public List<ProjectAssignment> getAllProjectsAssignment() {
        return projectAssignmentRepository.findAll();
    }*/




































    // Add a new user with roles
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addUser(@RequestBody SignupRequest signUpRequest) {
        // Existing validation code

        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<Role> roles = new HashSet<>();
        if (signUpRequest.getRole() != null && !signUpRequest.getRole().isEmpty()) {
            signUpRequest.getRole().forEach(role -> {
                Role userRole = roleRepository.findByName(ERole.valueOf(role))
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                roles.add(userRole);
            });

            // Set moderator if the role is 'ROLE_USER'
            if (signUpRequest.getRole().contains("ROLE_USER")) {
                User moderator = userRepository.findById(signUpRequest.getModeratorId())
                        .orElseThrow(() -> new RuntimeException("Error: Moderator not found."));
                user.setModerator(moderator);
            }
        }
        user.setRoles(roles);
        user=this.userRepository.save(user);
        this.validationservice.enregistrer(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/activation")
    public void activation(@RequestBody Map<String, String> activation) {
        this.userService.activation(activation);
    }


    @PostMapping("/addPoject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addProject(@RequestBody ProjetRequest projetRequest) {
        // Vérifier si un projet avec le même code ou nom existe déjà
        if (projetRepository.existsByCodeProjet(projetRequest.getCodeProjet())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Project code is already in use!"));
        }

        if (projetRepository.existsByNomProjet(projetRequest.getNomProjet())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Project name is already in use!"));
        }

        // Créer un nouveau projet à partir des données fournies dans ProjetRequest
        Projet projet = new Projet(projetRequest.getNomProjet(), projetRequest.getCodeProjet());

        // Enregistrer le projet dans la base de données
        projetRepository.save(projet);

        // Retourner une réponse de succès
        return ResponseEntity.ok(new MessageResponse("Project added successfully!"));
    }




















    // Update user roles


    @PutMapping("/{id}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUserRoles(@PathVariable Long id, @RequestBody Map<String, List<String>> roleData) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        Set<Role> roles = new HashSet<>();
        List<String> rolesList = roleData.get("roles");

        if (rolesList != null) {
            for (String roleName : rolesList) {
                Role role = roleRepository.findByName(ERole.valueOf(roleName))
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                roles.add(role);
            }
        }

        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User roles updated successfully!"));
    }





























    // Dans votre UserController.java
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User updateRequest) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: User not found."));

        // Mise à jour des champs de l'utilisateur
        existingUser.setUsername(updateRequest.getUsername());
        existingUser.setEmail(updateRequest.getEmail());
        existingUser.setPassword(encoder.encode(updateRequest.getPassword())); // Encoder le mot de passe

        // Mise à jour des rôles si nécessaire
        if (updateRequest.getRoles() != null && !updateRequest.getRoles().isEmpty()) {
            Set<Role> updatedRoles = updateRequest.getRoles().stream()
                    .map(role -> roleRepository.findByName(role.getName())
                            .orElseThrow(() -> new RuntimeException("Error: Role not found.")))
                    .collect(Collectors.toSet());
            existingUser.setRoles(updatedRoles);
        }

        userRepository.save(existingUser);
        return ResponseEntity.ok(new MessageResponse("User updated successfully!"));
    }





    @PutMapping("/updateProject/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateProject(@PathVariable Long id, @RequestBody ProjetRequest projetRequest) {
        // Rechercher le projet existant par ID
        Projet existingProject = projetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: Project not found."));

        // Vérifier si un autre projet avec le même code existe
        Optional<Projet> projectWithSameCode = projetRepository.findByCodeProjet(projetRequest.getCodeProjet());
        if (projectWithSameCode.isPresent() && !projectWithSameCode.get().getId().equals(id)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Project code is already in use by another project!"));
        }

        // Vérifier si un autre projet avec le même nom existe
        Optional<Projet> projectWithSameName = projetRepository.findByNomProjet(projetRequest.getNomProjet());
        if (projectWithSameName.isPresent() && !projectWithSameName.get().getId().equals(id)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Project name is already in use by another project!"));
        }

        // Mettre à jour les champs du projet
        existingProject.setNomProjet(projetRequest.getNomProjet());
        existingProject.setCodeProjet(projetRequest.getCodeProjet());

        // Enregistrer les modifications dans la base de données
        projetRepository.save(existingProject);

        return ResponseEntity.ok(new MessageResponse("Project updated successfully!"));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteProject(@PathVariable Long id) {
        if (!projetRepository.existsById(id)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: User not found!"));
        }
        projetRepository.deleteById(id);
        return ResponseEntity.ok(new MessageResponse("User deleted successfully!"));
    }





    @Transactional

    // Delete a user
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: User not found!"));

        // Suppression des assignations de projet liées à cet utilisateur
        projectAssignmentRepository.deleteByUser(user);

        // Suppression de la validation associée à cet utilisateur (si nécessaire)
        Validation validation = validationRepository.findByUser(user);
        if (validation != null) {
            validationRepository.delete(validation);
        }


        // Suppression de l'utilisateur
        userRepository.delete(user);

        return ResponseEntity.ok(new MessageResponse("User deleted successfully!"));
    }




    @GetMapping("/moderators")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getModerators() {
        List<User> moderators = userRepository.findAll().stream()
                .filter(user -> user.getRoles().stream()
                        .anyMatch(role -> role.getName().equals(ERole.ROLE_MODERATOR)))
                .collect(Collectors.toList());
        return ResponseEntity.ok(moderators);
    }













    @PostMapping("/{userId}/assign-projects")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> assignProjectsToUser(
            @PathVariable Long userId,
            @RequestBody List<Long> projectIds) {

        if (projectIds == null || projectIds.isEmpty()) {
            return ResponseEntity.badRequest().body("Error: No projects provided.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Error: User not found."));

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusMonths(3);

        for (Long projectId : projectIds) {
            Projet projet = projetRepository.findById(projectId)
                    .orElseThrow(() -> new RuntimeException("Error: Project not found."));
            ProjectAssignment assignment = new ProjectAssignment(user, projet, startDate, endDate);
            projectAssignmentRepository.save(assignment);
        }

        return ResponseEntity.ok("Projects assigned successfully.");
    }


    @GetMapping("/{userId}/assigned-projects")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAssignedProjects(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Error: User not found."));

        // Récupérer toutes les assignations de projets pour cet utilisateur
        List<ProjectAssignment> assignments = projectAssignmentRepository.findByUser(user);

        if (assignments.isEmpty()) {
            return ResponseEntity.ok("No projects assigned to this user.");
        }

        // Extraire les projets de ces assignations
        List<Projet> projects = assignments.stream()
                .map(ProjectAssignment::getProjet)
                .collect(Collectors.toList());

        return ResponseEntity.ok(projects);
    }

}
