package com.dembaandousmane.gest_priere.evenement.dto;

import com.dembaandousmane.gest_priere.evenement.model.Evenement;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter

public class EvenementResponseDto {

    private String nom ;
    private String description;
    private String lieu ;
    private LocalDateTime dateCreation ;
    private Boolean estActif;
    private Long etudiantId;

    public EvenementResponseDto(Evenement evenement, Long etudiantId){
        this.nom = evenement.getNom();
        this.description = evenement.getDescription();
        this.lieu = evenement.getLieu();
        this.dateCreation = evenement.getDateCreation();
        this.estActif = evenement.getEstActif();
        this.etudiantId = etudiantId;
    }


}
