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
    private LocalDateTime debut;
    private LocalDateTime fin;
    private Long idEtudiant;
    private Long idSalle;
    private Long idPriere;
    private Integer nombrePersonnes;
}
