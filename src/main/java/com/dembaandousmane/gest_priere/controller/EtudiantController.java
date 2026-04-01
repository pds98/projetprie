package com.dembaandousmane.gest_priere.controller;

import com.dembaandousmane.gest_priere.entity.Etudiant;
import com.dembaandousmane.gest_priere.service.EtudiantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/etudiants")
public class EtudiantController {

    @Autowired
    private EtudiantService etudiantService;

    @GetMapping
    public List<Etudiant> getAll() {
        return etudiantService.obtenirTousLesEtudiants();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Etudiant> getById(@PathVariable int id) {
        return etudiantService.obtenirEtudiant(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Etudiant create(@RequestBody Etudiant etudiant) {
        return etudiantService.inscrireEtudiant(etudiant);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Etudiant> update(@PathVariable int id, @RequestBody Etudiant etudiant) {
        try {
            return ResponseEntity.ok(etudiantService.modifierEtudiant(id, etudiant));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        try {
            etudiantService.supprimerEtudiant(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
