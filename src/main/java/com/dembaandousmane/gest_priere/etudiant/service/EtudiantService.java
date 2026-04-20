package com.dembaandousmane.gest_priere.etudiant.service;

import com.dembaandousmane.gest_priere.auth.dto.InscriptionResponseDto;
import com.dembaandousmane.gest_priere.etudiant.model.Etudiant;
import com.dembaandousmane.gest_priere.etudiant.repository.EtudiantRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EtudiantService {

    private final EtudiantRepository etudiantRepository;

    public EtudiantService(EtudiantRepository etudiantRepository) {
        this.etudiantRepository = etudiantRepository;
    }

    public InscriptionResponseDto creerEtudiant(Etudiant etudiant) {

        if (etudiantRepository.existsByEmail(etudiant.getEmail())) {
            throw new RuntimeException("Email deja utilise");
        }

        if (etudiant.getPrenom() == null || etudiant.getPrenom().isEmpty()) {
            throw new RuntimeException("Champ obligatoire : Prenom");
        }

        Etudiant e = Etudiant.builder()
                .prenom(etudiant.getPrenom())
                .nom(etudiant.getNom())
                .telephone(etudiant.getTelephone())
                .email(etudiant.getEmail())
                .motDePasse(etudiant.getMotDePasse())  // sauvegarde le mot de passe
                .build();

        Etudiant saved = etudiantRepository.save(e);

        return new InscriptionResponseDto(saved.getId(), saved.getEmail(), saved.getPrenom());
    }

    public Etudiant trouverEtudiantParId(Long id) {
        Optional<Etudiant> etudiant = etudiantRepository.findById(id);
        return etudiant.orElseThrow(() -> new RuntimeException("Utilisateur non trouve"));
    }
}
