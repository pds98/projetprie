package com.dembaandousmane.gest_priere.reservation.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationResponseDto {

    private Long id;

    private Long idEtudiant;
    private String nomEtudiant;

    private Long idSalle;
    private int numeroSalle;

    private String nomPriere;
    private Long idPriere;

    private String motif ;

    private LocalDateTime debut ;
    private LocalDateTime fin ;
}
