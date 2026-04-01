package com.dembaandousmane.gest_priere.service;

import com.dembaandousmane.gest_priere.entity.Etudiant;
import com.dembaandousmane.gest_priere.repository.EtudiantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EtudiantService {

    @Autowired
    private EtudiantRepository etudiantRepository;

    public Etudiant inscrireEtudiant(Etudiant e) {
        if (e.getNom() == null || e.getNom().isEmpty()) {
            throw new IllegalArgumentException("Le nom de l'étudiant est obligatoire.");
        }
        if (e.getEmail() == null || e.getEmail().isEmpty()) {
            throw new IllegalArgumentException("L'email de l'étudiant est obligatoire.");
        }
        return etudiantRepository.save(e);
    }

    public Optional<Etudiant> obtenirEtudiant(int id) {
        return etudiantRepository.findById(id);
    }

    public List<Etudiant> obtenirTousLesEtudiants() {
        return etudiantRepository.findAll();
    }

    public Etudiant modifierEtudiant(int id, Etudiant e) {
        if (!etudiantRepository.existsById(id)) {
            throw new IllegalArgumentException("Étudiant introuvable (ID: " + id + ").");
        }
        e.setIdEtudiant(id);
        return etudiantRepository.save(e);
    }

    public void supprimerEtudiant(int id) {
        if (!etudiantRepository.existsById(id)) {
            throw new IllegalArgumentException("Étudiant introuvable (ID: " + id + ").");
        }
        etudiantRepository.deleteById(id);
    }
}
