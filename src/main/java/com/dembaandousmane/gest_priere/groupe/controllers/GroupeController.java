package com.dembaandousmane.gest_priere.groupe.controllers;

import com.dembaandousmane.gest_priere.etudiant.model.Etudiant;
import com.dembaandousmane.gest_priere.etudiant.repository.EtudiantRepository;
import com.dembaandousmane.gest_priere.groupe.model.Groupe;
import com.dembaandousmane.gest_priere.groupe.repository.GroupeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

        if (createur != null) {
            if (g.getMembres() == null) g.setMembres(new ArrayList<>());
            g.getMembres().add(createur);
        }

        return groupeRepository.save(g);
    }

    @DeleteMapping("/{id}")
    public void supprimer(@PathVariable Long id) {
        groupeRepository.deleteById(id);
    }

    @PostMapping("/{id}/rejoindre")
    public ResponseEntity<Void> rejoindre(@PathVariable Long id, @RequestBody Map<String, Long> body) {
        Long idEtudiant = body.get("idEtudiant");
        Groupe groupe   = groupeRepository.findById(id).orElse(null);
        Etudiant etudiant = (idEtudiant != null) ? etudiantRepository.findById(idEtudiant).orElse(null) : null;
        if (groupe == null || etudiant == null) return ResponseEntity.notFound().build();

        if (groupe.getMembres() == null) groupe.setMembres(new ArrayList<>());

        boolean dejaInscrit = groupe.getMembres().stream().anyMatch(e -> e.getId().equals(idEtudiant));
        if (!dejaInscrit) {
            groupe.getMembres().add(etudiant);
            groupeRepository.save(groupe);
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/quitter/{idEtudiant}")
    public ResponseEntity<Void> quitter(@PathVariable Long id, @PathVariable Long idEtudiant) {
        Groupe groupe     = groupeRepository.findById(id).orElse(null);
        Etudiant etudiant = etudiantRepository.findById(idEtudiant).orElse(null);
        if (groupe == null || etudiant == null) return ResponseEntity.notFound().build();

        if (groupe.getMembres() != null) {
            groupe.getMembres().removeIf(e -> e.getId().equals(idEtudiant));
            groupeRepository.save(groupe);
        }
        return ResponseEntity.ok().build();
    }
}
