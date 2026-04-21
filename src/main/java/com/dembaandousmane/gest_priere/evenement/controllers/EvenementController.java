package com.dembaandousmane.gest_priere.evenement.controllers;


import com.dembaandousmane.gest_priere.evenement.dto.EvenementRequestDto;
import com.dembaandousmane.gest_priere.evenement.dto.EvenementResponseDto;
import com.dembaandousmane.gest_priere.evenement.model.Evenement;
import com.dembaandousmane.gest_priere.evenement.service.EvenementService;
import com.dembaandousmane.gest_priere.salle.model.Salle;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/evenement/")
public class EvenementController {

    private final EvenementService evenementService;

    public EvenementController(EvenementService evenementService) {
        this.evenementService = evenementService;
    }


    @PostMapping
    public EvenementResponseDto creerEvenement(@RequestBody EvenementRequestDto requestDto){

        return evenementService.creeEvenement(requestDto);
    }

    @DeleteMapping
    public void annulerEvenement(@RequestParam Long evenementId){

        evenementService.annulerEvenement(evenementId);
    }
    @GetMapping
    public List<Evenement> avoirToutLesEvenenement(){

       return  evenementService.avoirToutLesEvenement();
    }

    @PostMapping("participer/")
    public void participerEvenement(@RequestParam Long etudiantId, @RequestParam Long evenementId){

      evenementService.participerEvenement(evenementId, etudiantId);


}

@PostMapping("quitter/")
public void quitterEvenement(@RequestParam Long etudiantId, @RequestParam Long evenementId){

    evenementService.quitterEvenement(evenementId, etudiantId);
    }
}
