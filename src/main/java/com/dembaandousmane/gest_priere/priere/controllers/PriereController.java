package com.dembaandousmane.gest_priere.priere.controllers;

import com.dembaandousmane.gest_priere.priere.model.Priere;
import com.dembaandousmane.gest_priere.priere.repository.PriereRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prieres")
public class PriereController {

    private final PriereRepository priereRepository;

    public PriereController(PriereRepository priereRepository) {
        this.priereRepository = priereRepository;
    }

    @GetMapping
    public List<Priere> getTous() {
        return priereRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Priere> creer(@RequestBody Priere priere) {
        if (priere.getNom() == null || priere.getNom().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        Priere saved = priereRepository.save(priere);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Priere> modifier(@PathVariable Long id, @RequestBody Priere donnees) {
        return priereRepository.findById(id).map(priere -> {
            if (donnees.getNom() != null) priere.setNom(donnees.getNom());
            if (donnees.getDescription() != null) priere.setDescription(donnees.getDescription());
            if (donnees.getHeureDebut() != null) priere.setHeureDebut(donnees.getHeureDebut());
            if (donnees.getHeureFin() != null) priere.setHeureFin(donnees.getHeureFin());
            if (donnees.getStatut() != null) priere.setStatut(donnees.getStatut());
            return ResponseEntity.ok(priereRepository.save(priere));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        if (!priereRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        priereRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
