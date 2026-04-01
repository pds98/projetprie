package com.dembaandousmane.gest_priere.service;

import com.dembaandousmane.gest_priere.entity.Priere;
import com.dembaandousmane.gest_priere.repository.PriereRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PriereService {

    @Autowired
    private PriereRepository priereRepository;

    public Priere ajouterPriere(Priere p) {
        if (p.getNom() == null || p.getNom().isEmpty()) {
            throw new IllegalArgumentException("Le nom de la prière est obligatoire.");
        }
        return priereRepository.save(p);
    }

    public Optional<Priere> obtenirPriere(int id) {
        return priereRepository.findById(id);
    }

    public List<Priere> obtenirToutesLesPrieres() {
        return priereRepository.findAll();
    }

    public Priere modifierPriere(int id, Priere p) {
        if (!priereRepository.existsById(id)) {
            throw new IllegalArgumentException("Prière introuvable (ID: " + id + ").");
        }
        p.setId(id);
        return priereRepository.save(p);
    }

    public void supprimerPriere(int id) {
        if (!priereRepository.existsById(id)) {
            throw new IllegalArgumentException("Prière introuvable (ID: " + id + ").");
        }
        priereRepository.deleteById(id);
    }
}
