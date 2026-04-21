package com.dembaandousmane.gest_priere.evenement.service;


import com.dembaandousmane.gest_priere.etudiant.model.Etudiant;
import com.dembaandousmane.gest_priere.etudiant.service.EtudiantService;
import com.dembaandousmane.gest_priere.evenement.dto.EvenementRequestDto;
import com.dembaandousmane.gest_priere.evenement.dto.EvenementResponseDto;
import com.dembaandousmane.gest_priere.evenement.model.Evenement;
import com.dembaandousmane.gest_priere.evenement.repository.EvenementRepository;
import com.dembaandousmane.gest_priere.salle.model.Salle;
import org.springframework.stereotype.Service;

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

    public EvenementResponseDto creeEvenement(EvenementRequestDto requestDto){


        if(requestDto.getEtudiantId() == null && requestDto.getNom() == null){
            throw new RuntimeException("Nom obligatoire");
        }

        Etudiant etudiant = etudiantService.trouverEtudiantParId(requestDto.getEtudiantId());



        Evenement evenement = Evenement.builder().nom(requestDto.getNom())
                .dateCreation(LocalDateTime.now()).description(requestDto.getDescription())
                .lieu(requestDto.getLieu())
                .estActif(true)
                .build();

        etudiant.creerEvenement(evenement);

         Evenement saved = evenementRepository.save(evenement);

         return EvenementResponseDto.builder().nom(saved.getNom())
                 .lieu(saved.getLieu())
                 .description(saved.getDescription())
                 .dateCreation(saved.getDateCreation()).build();



    }

    public void modifierEvenement(){}


    public void annulerEvenement(Long evenementId){

        if(evenementId == 0){
            throw new RuntimeException("l'evenement id est 0");
        }

        Evenement evenement = evenementRepository.findById(evenementId).orElseThrow(() -> new RuntimeException("l'evenement n'a pas été trouvé"));

        evenement.setEstActif(false);

    }

    public List<Evenement> avoirToutLesEvenement(){

      return  evenementRepository.findByEstActif(true);
    }


    public void participerEvenement(Long evenementId, Long etudiantId){

        if(evenementId == 0 ||  etudiantId == 0){
            throw new RuntimeException("l'evenement id est 0 ou alors etudiantId 0 ");
        }

        Evenement evenement = evenementRepository.findById(evenementId).orElseThrow(() -> new RuntimeException("l'evenement n'existe pas"));
        Etudiant etudiant = etudiantService.trouverEtudiantParId(etudiantId);

        etudiant.participerEvenement(evenement);

    }

    public void quitterEvenement(Long evenementId, Long etudiantId){

        if(evenementId == 0 ||  etudiantId == 0){
            throw new RuntimeException("l'evenement id est 0 ou alors etudiantId 0 ");
        }

        Evenement evenement = evenementRepository.findById(evenementId).orElseThrow(() -> new RuntimeException("l'evenement n'existe pas"));
        Etudiant etudiant = etudiantService.trouverEtudiantParId(etudiantId);

        etudiant.quitterEvenement(evenement);
    }
}
