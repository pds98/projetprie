package com.dembaandousmane.gest_priere.evenement.controllers;


import com.dembaandousmane.gest_priere.evenement.dto.EvenementRequestDto;
import com.dembaandousmane.gest_priere.evenement.dto.EvenementResponseDto;
import com.dembaandousmane.gest_priere.evenement.service.EvenementService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/evenements")
public class EvenementController {

    private final EvenementService evenementService;

    public EvenementController(EvenementService evenementService) {
        this.evenementService = evenementService;
    }

    @PostMapping
    public EvenementResponseDto creerEvenement(@RequestBody Map<String, Object> body) {
        EvenementRequestDto requestDto = new EvenementRequestDto();
        requestDto.setNom((String) body.get("nom"));
        requestDto.setDescription((String) body.get("description"));
        requestDto.setLieu((String) body.get("lieu"));

        Long idEtudiant = body.get("idCreateurEvenement") != null
                ? Long.valueOf(body.get("idCreateurEvenement").toString())
                : null;

        return evenementService.creeEvenement(requestDto, idEtudiant);
    }

    @DeleteMapping("/{evenementId}")
    public void annulerEvenement(@PathVariable Long evenementId) {
        evenementService.annulerEvenement(evenementId);
    }

    @GetMapping
    public List<EvenementResponseDto> avoirToutLesEvenements() {
        return evenementService.avoirToutLesEvenementDto();
    }

    @PostMapping("/{id}/participer")
    public void participerEvenement(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        Long etudiantId = body.get("idEtudiant") != null
                ? Long.valueOf(body.get("idEtudiant").toString())
                : null;
        evenementService.participerEvenement(id, etudiantId);
    }

    @DeleteMapping("/{id}/quitter/{idEtudiant}")
    public void quitterEvenement(@PathVariable Long id, @PathVariable Long idEtudiant) {
        evenementService.quitterEvenement(id, idEtudiant);
    }
}
