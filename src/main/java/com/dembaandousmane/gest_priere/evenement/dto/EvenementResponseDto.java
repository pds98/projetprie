package com.dembaandousmane.gest_priere.evenement.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EvenementResponseDto {

    private Long id;
    private String nom;
    private String description;
    private String lieu;
    private LocalDateTime dateCreation;
    private Long idCreateurEvenement;
    private String statut;
}
