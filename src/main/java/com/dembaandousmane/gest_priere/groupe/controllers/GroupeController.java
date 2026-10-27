package com.dembaandousmane.gest_priere.groupe.controllers;


import com.dembaandousmane.gest_priere.groupe.dto.GroupeDtoResponse;
import com.dembaandousmane.gest_priere.groupe.dto.GroupeRequestDto;
import com.dembaandousmane.gest_priere.groupe.service.GroupeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groupes")
public class GroupeController {
private final GroupeService groupeService;

    public GroupeController(GroupeService groupeService) {
        this.groupeService = groupeService;
    }

    @PostMapping
    public GroupeDtoResponse creerGroupe(@RequestBody GroupeRequestDto requestDto){
        return groupeService.creerGroupe(requestDto);
    }

    @DeleteMapping("/{groupeId}")
    public void supprimerGroupe(@PathVariable Long groupeId){
        groupeService.supprimerGroupe(groupeId);
    }

    @PostMapping("/{id}/rejoindre")
    public void rejoindreGroupe(@PathVariable Long id, @RequestBody java.util.Map<String, Object> body){
        Long etudiantId = Long.valueOf(body.get("idEtudiant").toString());
        groupeService.rejoindreGroupe(id, etudiantId);
    }

    @DeleteMapping("/{id}/quitter/{idEtudiant}")
    public void quitterGroupe(@PathVariable Long id, @PathVariable Long idEtudiant){
        groupeService.quitterGroupe(id, idEtudiant);
    }

    @GetMapping
    public List<GroupeDtoResponse> avoirTousLesGroupe(){
        return groupeService.avoirToutlesGroupe();
    }

}
