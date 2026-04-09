package com.dembaandousmane.gest_priere.controller;

import com.dembaandousmane.gest_priere.entity.Etudiant;
import com.dembaandousmane.gest_priere.entity.Evenement;
import com.dembaandousmane.gest_priere.entity.Salle;
import com.dembaandousmane.gest_priere.repository.EtudiantRepository;
import com.dembaandousmane.gest_priere.repository.EvenementRepository;
import com.dembaandousmane.gest_priere.repository.SalleRepository;
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

    public EtudiantController(EtudiantRepository etudiantRepository, EvenementRepository evenementRepository, SalleRepository salleRepository){
        this.etudiantRepository = etudiantRepository;
        this.evenementRepository = evenementRepository;
        this.salleRepository = salleRepository;
    }

    @GetMapping
    public void AjouterEtudiant(@RequestParam String nom , @RequestParam String prenom){
        LocalDateTime date = LocalDateTime.of(2026, 4, 8, 10, 30);

        Salle salle = Salle.builder()
                .capacite(22)
                .statut("occupé")
                .build();

        Evenement evenement = Evenement.builder()
                .nom("eid")
                .description("fete")
                .lieu("montreal")
                .dateCreation(date)
                .estActif(true)
                .build();

        Etudiant etudiant = Etudiant.builder().email("ousmanetelly30@gmail.com")
                .nom(nom)
                .prenom(prenom).telephone("500")
                .salle(salle)
                .build();

        etudiant.ajouterEvenement(evenement);





        evenementRepository.save(evenement);
        salleRepository.save(salle);
        etudiantRepository.save(etudiant);

    }

}
