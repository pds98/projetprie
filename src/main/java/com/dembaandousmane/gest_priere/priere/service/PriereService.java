package com.dembaandousmane.gest_priere.priere.service;

import com.dembaandousmane.gest_priere.priere.model.Priere;
import com.dembaandousmane.gest_priere.priere.repository.PriereRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Service
public class PriereService {

    private final PriereRepository priereRepository;

    public PriereService(PriereRepository priereRepository) {
        this.priereRepository = priereRepository;
    }


    public Priere trouverPriereParId(Long id){

        Optional<Priere> p = priereRepository.findById(id);
         return p.orElseThrow(() -> new RuntimeException("la priere n'existe pas"));

    }
}
