package com.dembaandousmane.gest_priere.Etudiant.Controllers;

import com.dembaandousmane.gest_priere.Etudiant.Model.Etudiant;
import com.dembaandousmane.gest_priere.Etudiant.Repository.EtudiantRepository;
import org.springframework.web.bind.annotation.*;

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
