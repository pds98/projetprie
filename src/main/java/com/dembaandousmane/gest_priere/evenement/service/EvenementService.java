package com.dembaandousmane.gest_priere.evenement.service;

import com.dembaandousmane.gest_priere.etudiant.model.Etudiant;
import com.dembaandousmane.gest_priere.etudiant.service.EtudiantService;
import com.dembaandousmane.gest_priere.evenement.dto.EvenementRequestDto;
import com.dembaandousmane.gest_priere.evenement.dto.EvenementResponseDto;
import com.dembaandousmane.gest_priere.evenement.model.Evenement;
import com.dembaandousmane.gest_priere.evenement.repository.EvenementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EvenementService {

    private final EvenementRepository evenementRepository;
    private final EtudiantService etudiantService;

    public EvenementService(EvenementRepository evenementRepository, EtudiantService etudiantService) {
        this.evenementRepository = evenementRepository;
        this.etudiantService = etudiantService;
    }

    @Transactional
    public EvenementResponseDto creeEvenement(EvenementRequestDto requestDto, Long idEtudiant) {

        if (idEtudiant == null || requestDto.getNom() == null) {
            throw new RuntimeException("Nom et idEtudiant obligatoires");
        }

        Etudiant etudiant = etudiantService.trouverEtudiantParId(idEtudiant);

        Evenement evenement = Evenement.builder()
                .nom(requestDto.getNom())
                .dateCreation(LocalDateTime.now())
                .description(requestDto.getDescription() != null ? requestDto.getDescription() : "")
                .lieu(requestDto.getLieu())
                .estActif(true)
                .createurEvenement(etudiant)
                .build();

        Evenement saved = evenementRepository.save(evenement);

        return EvenementResponseDto.builder()
                .id(saved.getId())
                .nom(saved.getNom())
                .lieu(saved.getLieu())
                .description(saved.getDescription())
                .dateCreation(saved.getDateCreation())
                .idCreateurEvenement(idEtudiant)
                .statut(saved.isEstActif() ? "actif" : "annule")
                .build();
    }

    public void modifierEvenement() {}

    public void annulerEvenement(Long evenementId) {
        if (evenementId == null || evenementId == 0) {
            throw new RuntimeException("l'evenement id est invalide");
        }
        Evenement evenement = evenementRepository.findById(evenementId)
                .orElseThrow(() -> new RuntimeException("l'evenement n'a pas ete trouve"));
        evenement.setEstActif(false);
        evenementRepository.save(evenement);
    }

    public List<Evenement> avoirToutLesEvenement() {
        return evenementRepository.findByEstActif(true);
    }

    public List<EvenementResponseDto> avoirToutLesEvenementDto() {
        return evenementRepository.findByEstActif(true).stream()
                .map(e -> EvenementResponseDto.builder()
                        .id(e.getId())
                        .nom(e.getNom())
                        .description(e.getDescription())
                        .lieu(e.getLieu())
                        .dateCreation(e.getDateCreation())
                        .idCreateurEvenement(e.getCreateurEvenement() != null ? e.getCreateurEvenement().getId() : null)
                        .statut(e.isEstActif() ? "actif" : "annule")
                        .build())
                .toList();
    }

    @Transactional
    public void participerEvenement(Long evenementId, Long etudiantId) {
        if (evenementId == null || etudiantId == null) {
            throw new RuntimeException("evenementId ou etudiantId manquant");
        }
        Evenement evenement = evenementRepository.findById(evenementId)
                .orElseThrow(() -> new RuntimeException("l'evenement n'existe pas"));
        Etudiant etudiant = etudiantService.trouverEtudiantParId(etudiantId);

        etudiant.participerEvenement(evenement);
    }

    @Transactional
    public void quitterEvenement(Long evenementId, Long etudiantId) {
        if (evenementId == null || etudiantId == null) {
            throw new RuntimeException("evenementId ou etudiantId manquant");
        }
        Evenement evenement = evenementRepository.findById(evenementId)
                .orElseThrow(() -> new RuntimeException("l'evenement n'existe pas"));
        Etudiant etudiant = etudiantService.trouverEtudiantParId(etudiantId);

        etudiant.quitterEvenement(evenement);
    }
}
