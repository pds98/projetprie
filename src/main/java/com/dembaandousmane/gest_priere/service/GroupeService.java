package com.dembaandousmane.gest_priere.service;

import com.dembaandousmane.gest_priere.entity.Groupe;
import com.dembaandousmane.gest_priere.repository.GroupeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupeService {

    @Autowired
    private GroupeRepository groupeRepository;

    public Groupe ajouterGroupe(Groupe g) {
        if (g.getNom() == null || g.getNom().isEmpty()) {
            throw new IllegalArgumentException("Le nom du groupe est obligatoire.");
        }
        return groupeRepository.save(g);
    }

    public Optional<Groupe> obtenirGroupe(int id) {
        return groupeRepository.findById(id);
    }

    public List<Groupe> obtenirTousLesGroupes() {
        return groupeRepository.findAll();
    }

    public Groupe modifierGroupe(int id, Groupe g) {
        if (!groupeRepository.existsById(id)) {
            throw new IllegalArgumentException("Groupe introuvable (ID: " + id + ").");
        }
        g.setIdGroupe(id);
        return groupeRepository.save(g);
    }

    public void supprimerGroupe(int id) {
        if (!groupeRepository.existsById(id)) {
            throw new IllegalArgumentException("Groupe introuvable (ID: " + id + ").");
        }
        groupeRepository.deleteById(id);
    }
}
