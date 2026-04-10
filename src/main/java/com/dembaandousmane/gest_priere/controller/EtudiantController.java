package com.dembaandousmane.gest_priere.controller;

import com.dembaandousmane.gest_priere.entity.*;
import com.dembaandousmane.gest_priere.repository.*;
import com.dembaandousmane.gest_priere.service.EtudiantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
//@RequestMapping("/api/etudiants")
public class EtudiantController {

    // @Autowired
   // private EtudiantService etudiantService;

    private EtudiantRepository etudiantRepository;
    private EvenementRepository evenementRepository;
    private SalleRepository salleRepository;
    private ForumRepository forumRepository;
    private GroupeRepository groupeRepository;

    public EtudiantController(EtudiantRepository etudiantRepository, EvenementRepository evenementRepository, SalleRepository salleRepository, ForumRepository forumRepository, GroupeRepository groupeRepository){
        this.etudiantRepository = etudiantRepository;
        this.evenementRepository = evenementRepository;
        this.salleRepository = salleRepository;
        this.forumRepository = forumRepository;
        this.groupeRepository = groupeRepository;
    }

    @GetMapping
    public void AjouterEtudiant(@RequestParam String nom , @RequestParam String prenom){
        LocalDateTime date = LocalDateTime.of(2026, 4, 8, 10, 30);

        Etudiant etudiant = etudiantRepository.getReferenceById(12L);
        Groupe groupe = groupeRepository.getReferenceById(1);
        etudiant.supprimerGroupe(groupe);





    }

}
