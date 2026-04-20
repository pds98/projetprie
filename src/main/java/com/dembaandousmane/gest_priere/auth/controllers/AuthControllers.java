package com.dembaandousmane.gest_priere.auth.controllers;

import com.dembaandousmane.gest_priere.etudiant.model.Etudiant;
import com.dembaandousmane.gest_priere.etudiant.repository.EtudiantRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthControllers {

    private final EtudiantRepository etudiantRepository;

    public AuthControllers(EtudiantRepository etudiantRepository) {
        this.etudiantRepository = etudiantRepository;
    }

    // POST /api/auth/login
    // Le front envoie { "email": "...", "motDePasse": "..." }
    @PostMapping("/login")
    public ResponseEntity<?> connexion(@RequestBody Map<String, String> body) {
        String email      = body.get("email");
        String motDePasse = body.get("motDePasse");

        if (email == null || motDePasse == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email et mot de passe requis."));
        }

        Optional<Etudiant> opt = etudiantRepository.findByEmail(email.trim().toLowerCase());

        if (opt.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("message", "Aucun compte avec cet email."));
        }

        Etudiant etudiant = opt.get();

        if (etudiant.getMotDePasse() == null || etudiant.getMotDePasse().isBlank()) {
            return ResponseEntity.status(401).body(Map.of("message", "Ce compte n'a pas de mot de passe. Recree ton compte depuis l'inscription."));
        }

        if (!motDePasse.equals(etudiant.getMotDePasse())) {
            return ResponseEntity.status(401).body(Map.of("message", "Mot de passe incorrect."));
        }

        // Connexion reussie — retourne l'objet Etudiant complet (motDePasse est WRITE_ONLY donc jamais expose)
        return ResponseEntity.ok(etudiant);
    }
}
