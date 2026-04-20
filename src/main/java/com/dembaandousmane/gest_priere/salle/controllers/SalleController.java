package com.dembaandousmane.gest_priere.salle.controllers;

import com.dembaandousmane.gest_priere.salle.model.Salle;
import com.dembaandousmane.gest_priere.salle.repository.SalleRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/salles")
public class SalleController {

    private final SalleRepository salleRepository;

    public SalleController(SalleRepository salleRepository) {
        this.salleRepository = salleRepository;
    }

    @GetMapping
    public List<Salle> getTous() {
        return salleRepository.findAll();
    }

    @PostMapping
    public Salle ajouter(@RequestBody Salle salle) {
        Salle s = Salle.builder()
                .capacite(salle.getCapacite())
                .statut(salle.getStatut() != null ? salle.getStatut() : "libre")
                .NumeroSalle(salle.getNumeroSalle())
                .build();
        return salleRepository.save(s);
    }

    @DeleteMapping("/{id}")
    public void supprimer(@PathVariable Long id) {
        salleRepository.deleteById(id);
    }
}
