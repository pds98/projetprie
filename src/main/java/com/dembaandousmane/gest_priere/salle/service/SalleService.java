package com.dembaandousmane.gest_priere.salle.service;


import com.dembaandousmane.gest_priere.salle.model.Salle;
import com.dembaandousmane.gest_priere.salle.repository.SalleRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SalleService {
    private final SalleRepository salleRepository;

    public SalleService(SalleRepository salleRepository) {
        this.salleRepository = salleRepository;
    }


    public Salle trouverSalleParId(Long id ){

        Optional<Salle> s = salleRepository.findById(id);

        return  s.orElseThrow(() -> new RuntimeException("la classe n'existe pas "));



    }
}
