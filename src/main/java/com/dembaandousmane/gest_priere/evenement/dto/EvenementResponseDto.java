package com.dembaandousmane.gest_priere.evenement.dto;

import com.dembaandousmane.gest_priere.evenement.model.Evenement;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class EvenementResponseDto {

    private String nom ;
    private String description;
    private String lieu ;
    private LocalDateTime dateCreation ;
    private Long etudiantId;




}
