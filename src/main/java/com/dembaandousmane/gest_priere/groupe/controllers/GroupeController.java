package com.dembaandousmane.gest_priere.groupe.controllers;


import com.dembaandousmane.gest_priere.groupe.dto.GroupeDtoResponse;
import com.dembaandousmane.gest_priere.groupe.model.Groupe;
import com.dembaandousmane.gest_priere.groupe.service.GroupeService;
import org.springframework.web.bind.annotation.*;

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

    public void supprimerGroupe(){

    }

    public void rejoindreGroupe(){}



}
