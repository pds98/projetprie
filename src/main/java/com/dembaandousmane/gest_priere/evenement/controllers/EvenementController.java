package com.dembaandousmane.gest_priere.evenement.controllers;

import com.dembaandousmane.gest_priere.etudiant.model.Etudiant;
import com.dembaandousmane.gest_priere.etudiant.repository.EtudiantRepository;
import com.dembaandousmane.gest_priere.evenement.model.Evenement;
import com.dembaandousmane.gest_priere.evenement.repository.EvenementRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/evenements")
public class EvenementController {

    private final EvenementRepository evenementRepository;
    private final EtudiantRepository etudiantRepository;

    public EvenementController(EvenementRepository evenementRepository, EtudiantRepository etudiantRepository) {
        this.evenementRepository = evenementRepository;
        this.etudiantRepository  = etudiantRepository;
    }

    @GetMapping
    public List<Evenement> getTous() {
        return evenementRepository.findAll();
    }

    @PostMapping
    public Evenement creer(@RequestBody Evenement evenement) {
        if (evenement.getDateCreation() == null) evenement.setDateCreation(LocalDateTime.now());
        evenement.setEstActif(true);
        if (evenement.getStatut() == null) evenement.setStatut("actif");
        return evenementRepository.save(evenement);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Evenement> modifier(@PathVariable Long id, @RequestBody Evenement body) {
        Optional<Evenement> opt = evenementRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        Evenement e = opt.get();
        if (body.getNom()         != null) e.setNom(body.getNom());
        if (body.getDescription() != null) e.setDescription(body.getDescription());
        if (body.getLieu()        != null) e.setLieu(body.getLieu());
        if (body.getStatut()      != null) e.setStatut(body.getStatut());
        return ResponseEntity.ok(evenementRepository.save(e));
    }

    @DeleteMapping("/{id}")
    public void supprimer(@PathVariable Long id) {
        evenementRepository.deleteById(id);
    }

    @PostMapping("/{id}/participer")
    public ResponseEntity<Void> participer(@PathVariable Long id, @RequestBody Map<String, Long> body) {
        Long idEtudiant = body.get("idEtudiant");
        Evenement evenement = evenementRepository.findById(id).orElse(null);
        Etudiant etudiant   = (idEtudiant != null) ? etudiantRepository.findById(idEtudiant).orElse(null) : null;
        if (evenement == null || etudiant == null) return ResponseEntity.notFound().build();

        if (etudiant.getEvenementsParticipe() == null) etudiant.setEvenementsParticipe(new ArrayList<>());

        boolean dejaInscrit = etudiant.getEvenementsParticipe().stream().anyMatch(e -> e.getId().equals(id));
        if (!dejaInscrit) {
            etudiant.getEvenementsParticipe().add(evenement);
            etudiantRepository.save(etudiant);
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/quitter/{idEtudiant}")
    public ResponseEntity<Void> quitter(@PathVariable Long id, @PathVariable Long idEtudiant) {
        Evenement evenement = evenementRepository.findById(id).orElse(null);
        Etudiant etudiant   = etudiantRepository.findById(idEtudiant).orElse(null);
        if (evenement == null || etudiant == null) return ResponseEntity.notFound().build();

        if (etudiant.getEvenementsParticipe() != null) {
            etudiant.getEvenementsParticipe().removeIf(e -> e.getId().equals(id));
            etudiantRepository.save(etudiant);
        }
        return ResponseEntity.ok().build();
    }
}
