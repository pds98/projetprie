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
@RequestMapping("/api/etudiants")
public class EtudiantController {


    private final EtudiantRepository etudiantRepository;


    public EtudiantController(EtudiantRepository etudiantRepository){
        this.etudiantRepository = etudiantRepository;

    }

    @PostMapping("/etudiant")
    public void AjouterEtudiant(@RequestBody Etudiant etudiant){

      Etudiant e = Etudiant.builder()
              .prenom(etudiant.getPrenom())
              .nom(etudiant.getNom())
              .telephone(etudiant.getTelephone())
              .email(etudiant.getEmail())
              .build();


      etudiantRepository.save(etudiant);


    }

     public void  modifierEtudiant(){}


    public void supprimerEtudiant(){}


}
