package com.dembaandousmane.gest_priere.evenement.controllers;


import com.dembaandousmane.gest_priere.evenement.dto.EvenementResponseDto;
import com.dembaandousmane.gest_priere.evenement.model.Evenement;
import com.dembaandousmane.gest_priere.evenement.service.EvenementService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/evenement/")
public class EvenementController {

    private final EvenementService evenementService;

    public EvenementController(EvenementService evenementService) {
        this.evenementService = evenementService;
    }


    @PostMapping
    public EvenementResponseDto creerEvenement(@RequestBody Evenement evenement, @RequestParam Long etudiantId){

        return evenementService.creeEvenement(evenement, etudiantId);
    }



}
