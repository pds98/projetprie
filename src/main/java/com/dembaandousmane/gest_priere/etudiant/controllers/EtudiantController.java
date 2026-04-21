package com.dembaandousmane.gest_priere.etudiant.controllers;

import com.dembaandousmane.gest_priere.etudiant.model.Etudiant;
import com.dembaandousmane.gest_priere.etudiant.repository.EtudiantRepository;
import com.dembaandousmane.gest_priere.etudiant.service.EtudiantService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/etudiants")
public class EtudiantController {


    private final EtudiantService etudiantService;


    public EtudiantController( EtudiantService etudiantService){
        this.etudiantService = etudiantService;


    }

    @PostMapping("/etudiant")
    public void AjouterEtudiant(@RequestBody Etudiant etudiant){

    }

     public void  modifierEtudiant(){}


    public void supprimerEtudiant(){}


}
