package com.dembaandousmane.gest_priere.service;

import com.dembaandousmane.gest_priere.entity.Evenement;
import com.dembaandousmane.gest_priere.repository.EvenementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EvenementService {

    @Autowired
    private EvenementRepository evenementRepository;

    public Evenement ajouterEvenement(Evenement e) {
        if (e.getNom() == null || e.getNom().isEmpty()) {
            throw new IllegalArgumentException("Le nom de l'événement est obligatoire.");
        }
        return evenementRepository.save(e);
    }

    public Optional<Evenement> obtenirEvenement(int id) {
        return evenementRepository.findById(id);
    }

    public List<Evenement> obtenirTousLesEvenements() {
        return evenementRepository.findAll();
    }

    public List<Evenement> obtenirEvenementsParStatut(String statut) {
        return evenementRepository.findByStatut(statut);
    }

    public Evenement modifierEvenement(int id, Evenement e) {
        if (!evenementRepository.existsById(id)) {
            throw new IllegalArgumentException("Événement introuvable (ID: " + id + ").");
        }
        e.setId(id);
        return evenementRepository.save(e);
    }

    public void supprimerEvenement(int id) {
        if (!evenementRepository.existsById(id)) {
            throw new IllegalArgumentException("Événement introuvable (ID: " + id + ").");
        }
        evenementRepository.deleteById(id);
    }
}
