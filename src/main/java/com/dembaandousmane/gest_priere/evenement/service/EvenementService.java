package com.dembaandousmane.gest_priere.evenement.service;


import com.dembaandousmane.gest_priere.etudiant.model.Etudiant;
import com.dembaandousmane.gest_priere.etudiant.service.EtudiantService;
import com.dembaandousmane.gest_priere.evenement.dto.EvenementResponseDto;
import com.dembaandousmane.gest_priere.evenement.model.Evenement;
import com.dembaandousmane.gest_priere.evenement.repository.EvenementRepository;
import org.springframework.stereotype.Service;

@Service
public class EvenementService {

  private final EvenementRepository evenementRepository;
  private final EtudiantService etudiantService;


    public EvenementService(EvenementRepository evenementRepository, EtudiantService etudiantService) {
        this.evenementRepository = evenementRepository;
        this.etudiantService = etudiantService;

    }

    public EvenementResponseDto creeEvenement(Evenement evenement, Long etudiantId){


        if(evenement.getCreateurEvenement() == null && evenement.getNom() == null){
            throw new RuntimeException("Nom obligatoire");
        }

        Etudiant etudiant = etudiantService.trouverEtudiantParId(etudiantId);



        Evenement evenement1 = Evenement.builder().nom(evenement.getNom())
                .dateCreation(evenement.getDateCreation()).description(evenement.getDescription())
                .lieu(evenement.getLieu())
                .estActif(evenement.isEstActif()).build();

        etudiant.rejoindreEvenement(evenement1);

         Evenement ev = evenementRepository.save(evenement1);

         EvenementResponseDto responseDto = new EvenementResponseDto(evenement1, etudiantId);

         return new EvenementResponseDto(ev, etudiantId);



    }

    public void modifierEvenement(){}


    public void supprimerEvenement(){

    }
}
