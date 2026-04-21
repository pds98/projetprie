package com.dembaandousmane.gest_priere.reservation.dto;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationRequestDto {

    private String motif;
    private LocalDateTime dateDebut ;
    private LocalDateTime dateFin;
    private Long idEtudiant;
    private Long idSalle;
    private Long idPriere;
}
