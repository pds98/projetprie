package com.dembaandousmane.gest_priere.priere.controllers;

import com.dembaandousmane.gest_priere.priere.model.Priere;
import com.dembaandousmane.gest_priere.priere.repository.PriereRepository;
import com.dembaandousmane.gest_priere.priere.service.PriereService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prieres")
public class PriereController {

    private final PriereService priereService;

    public PriereController(PriereService priereService) {
        this.priereService = priereService;
    }

    @GetMapping
    public List<Priere> avoirTouteLesPriere(){
        return priereService.avoirTouteLesPriere();
    }
}
