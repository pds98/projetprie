package com.dembaandousmane.gest_priere.etudiant.controllers;

import com.dembaandousmane.gest_priere.auth.dto.InscriptionResponseDto;
import com.dembaandousmane.gest_priere.etudiant.model.Etudiant;
import com.dembaandousmane.gest_priere.etudiant.repository.EtudiantRepository;
import com.dembaandousmane.gest_priere.etudiant.service.EtudiantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/etudiants")
public class EtudiantController {

    private final EtudiantService etudiantService;
    private final EtudiantRepository etudiantRepository;

    public EtudiantController(EtudiantService etudiantService, EtudiantRepository etudiantRepository) {
        this.etudiantService = etudiantService;
        this.etudiantRepository = etudiantRepository;
    }

    @GetMapping
    public List<Etudiant> getTous() {
        return etudiantRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<InscriptionResponseDto> creer(@RequestBody Etudiant etudiant) {
        return ResponseEntity.ok(etudiantService.creerEtudiant(etudiant));
    }
}
