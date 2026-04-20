package com.dembaandousmane.gest_priere.groupe.controllers;


import com.dembaandousmane.gest_priere.groupe.dto.GroupeDtoResponse;
import com.dembaandousmane.gest_priere.groupe.model.Groupe;
import com.dembaandousmane.gest_priere.groupe.service.GroupeService;
import com.dembaandousmane.gest_priere.reservation.model.Reservation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/groupe/")
public class GroupeController {
private final GroupeService groupeService;

    public GroupeController(GroupeService groupeService) {
        this.groupeService = groupeService;
    }

    @PostMapping
    public GroupeDtoResponse creerGroupe(@RequestBody Groupe groupe, @RequestParam Long etudiantId){
        return groupeService.creerGroupe(groupe, etudiantId);
    }

    @DeleteMapping
    public void supprimerGroupe(@RequestParam Long groupeId, @RequestParam Long etudiantId){

      groupeService.supprimerGroupe(groupeId, etudiantId);
    }

    @PostMapping("rejoindre/")
    public void rejoindreGroupe(@RequestParam Long etudiantId, @RequestParam Long groupeId){

        groupeService.rejoindreGroupe(groupeId, etudiantId);

    }

    @PostMapping("quitter/")
    public void quitterGroupe(@RequestParam Long etudiantId, @RequestParam Long groupeId){
        groupeService.quitterGroupe(groupeId, etudiantId);
    }

    @GetMapping
    public List<Groupe> avoirTousLesGroupe(){
        return groupeService.avoirToutlesGroupe();
    }

}
