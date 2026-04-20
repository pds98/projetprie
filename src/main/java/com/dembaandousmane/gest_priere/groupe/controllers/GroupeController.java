package com.dembaandousmane.gest_priere.groupe.controllers;

import com.dembaandousmane.gest_priere.etudiant.model.Etudiant;
import com.dembaandousmane.gest_priere.etudiant.repository.EtudiantRepository;
import com.dembaandousmane.gest_priere.groupe.model.Groupe;
import com.dembaandousmane.gest_priere.groupe.repository.GroupeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/groupes")
public class GroupeController {

    private final GroupeRepository groupeRepository;
    private final EtudiantRepository etudiantRepository;

    public GroupeController(GroupeRepository groupeRepository, EtudiantRepository etudiantRepository) {
        this.groupeRepository = groupeRepository;
        this.etudiantRepository = etudiantRepository;
    }

    @GetMapping
    public List<Groupe> getTous() {
        return groupeRepository.findAll();
    }

    @PostMapping
    public Groupe creer(@RequestBody Map<String, Object> body) {
        String nom         = (String) body.get("nom");
        String description = (String) body.get("description");
        Long idEtudiant    = body.get("idEtudiant") != null ? Long.valueOf(body.get("idEtudiant").toString()) : null;

        Etudiant createur = (idEtudiant != null) ? etudiantRepository.findById(idEtudiant).orElse(null) : null;

        Groupe g = Groupe.builder()
                .nom(nom)
                .description(description != null ? description : "")
                .createurGroupe(createur)
                .build();

        Groupe saved = groupeRepository.save(g);

        // Ajouter le créateur comme membre via le côté propriétaire (Etudiant)
        if (createur != null) {
            if (createur.getGroupesRejoins() == null) createur.setGroupesRejoins(new java.util.ArrayList<>());
            createur.getGroupesRejoins().add(saved);
            etudiantRepository.save(createur);
        }

        return saved;
    }

    @DeleteMapping("/{id}")
    public void supprimer(@PathVariable Long id) {
        groupeRepository.deleteById(id);
    }

    // Côté propriétaire = Etudiant.groupesRejoins → on modifie l'étudiant, pas le groupe
    @Transactional
    @PostMapping("/{id}/rejoindre")
    public ResponseEntity<Void> rejoindre(@PathVariable Long id, @RequestBody Map<String, Long> body) {
        Long idEtudiant   = body.get("idEtudiant");
        Groupe   groupe   = groupeRepository.findById(id).orElse(null);
        Etudiant etudiant = (idEtudiant != null) ? etudiantRepository.findById(idEtudiant).orElse(null) : null;
        if (groupe == null || etudiant == null) return ResponseEntity.notFound().build();

        if (etudiant.getGroupesRejoins() == null) etudiant.setGroupesRejoins(new java.util.ArrayList<>());

        boolean dejaInscrit = etudiant.getGroupesRejoins().stream().anyMatch(g -> g.getId().equals(id));
        if (!dejaInscrit) {
            etudiant.getGroupesRejoins().add(groupe);
            etudiantRepository.save(etudiant);
        }
        return ResponseEntity.ok().build();
    }

    @Transactional
    @DeleteMapping("/{id}/quitter/{idEtudiant}")
    public ResponseEntity<Void> quitter(@PathVariable Long id, @PathVariable Long idEtudiant) {
        Groupe   groupe   = groupeRepository.findById(id).orElse(null);
        Etudiant etudiant = etudiantRepository.findById(idEtudiant).orElse(null);
        if (groupe == null || etudiant == null) return ResponseEntity.notFound().build();

        if (etudiant.getGroupesRejoins() != null) {
            etudiant.getGroupesRejoins().removeIf(g -> g.getId().equals(id));
            etudiantRepository.save(etudiant);
        }
        return ResponseEntity.ok().build();
    }
}
