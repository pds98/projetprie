package com.dembaandousmane.gest_priere.controller;


import com.dembaandousmane.gest_priere.entity.Salle;
import com.dembaandousmane.gest_priere.repository.SalleRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("api/salles")
@RestController
public class SalleController {
    private SalleRepository salleRepository;



    public SalleController(SalleRepository salleRepository){
        this.salleRepository = salleRepository;
    }




    @PostMapping("salle")
    public void ajouterSalle(@RequestBody Salle salle){

        Salle s = Salle.builder().capacite(salle.getCapacite())
                .statut(salle.getStatut())
                .build();



        salleRepository.save(s);

    }


    public void modifierSalle(){}



    public void supprimerSalle(){}


    public List<Salle> avoirSalle(){return null;}
}


