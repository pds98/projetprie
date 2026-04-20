package com.dembaandousmane.gest_priere.salle.controllers;


import com.dembaandousmane.gest_priere.salle.dto.SalleResponseDto;
import com.dembaandousmane.gest_priere.salle.model.Salle;
import com.dembaandousmane.gest_priere.salle.service.SalleService;
import jakarta.transaction.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("api/salles")
@RestController
public class SalleController {
    private final  SalleService salleService;

    public SalleController(SalleService salleService) {
        this.salleService = salleService;
    }


    @PostMapping("salle")
    public SalleResponseDto creerSalle(@RequestBody Salle salle){

       return salleService.creerSalle(salle);
    }

     @PutMapping
    public void modifierSalleParCapacite(@RequestParam Long idSalle, @RequestParam int nouvelleCapacite){
        salleService.modifierSalleParCapacite(idSalle, nouvelleCapacite);
    }


    @DeleteMapping
    @Transactional
    public void supprimerSalle(@RequestParam Long idsalle){
      salleService.supprimerSalle(idsalle);
    }

    @GetMapping
    public List<Salle> avoirSalleLibre(){

        return salleService.avoirSalleLibre();
    }
}


