package com.dembaandousmane.gest_priere.evenement.dto;

import com.dembaandousmane.gest_priere.evenement.model.Evenement;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EvenementRequestDto {

    private String nom ;
    private String description;
    private String lieu ;
    private LocalDateTime dateCreation ;
    private  boolean estActif;
    private Long etudiantId;

    public EvenementRequestDto(Evenement evenement, Long etudiantId){
        this.nom = evenement.getNom();
        this.description = evenement.getDescription();
        this.lieu = evenement.getLieu();
        this.dateCreation = evenement.getDateCreation();
        this.estActif = evenement.isEstActif();
        this.etudiantId = etudiantId;
    }
}
