package com.dembaandousmane.gest_priere.service;

import com.dembaandousmane.gest_priere.entity.Salle;
import com.dembaandousmane.gest_priere.repository.SalleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SalleService {

    @Autowired
    private SalleRepository salleRepository;

    public Salle ajouterSalle(Salle s) {
        if (s.getCapacite() <= 0) {
            throw new IllegalArgumentException("La capacité doit être supérieure à 0.");
        }
        return salleRepository.save(s);
    }

    public Optional<Salle> obtenirSalle(int id) {
        return salleRepository.findById(id);
    }

    public List<Salle> obtenirToutesLesSalles() {
        return salleRepository.findAll();
    }

    public List<Salle> obtenirSallesDisponibles() {
        return salleRepository.findByStatut("disponible");
    }

    public Salle modifierSalle(int id, Salle s) {
        if (!salleRepository.existsById(id)) {
            throw new IllegalArgumentException("Salle introuvable (ID: " + id + ").");
        }
        s.setIdSalle(id);
        return salleRepository.save(s);
    }

    public Salle libererSalle(int id) {
        Salle s = salleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Salle introuvable (ID: " + id + ")."));
        s.libererSalle();
        return salleRepository.save(s);
    }

    public void supprimerSalle(int id) {
        if (!salleRepository.existsById(id)) {
            throw new IllegalArgumentException("Salle introuvable (ID: " + id + ").");
        }
        salleRepository.deleteById(id);
    }
}
