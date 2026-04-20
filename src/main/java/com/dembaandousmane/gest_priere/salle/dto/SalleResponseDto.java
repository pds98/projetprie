package com.dembaandousmane.gest_priere.salle.dto;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalleResponseDto {

    private Long id;
    private Long idSalle;
    private int numeroSalle;
    private int capacite;
    private String status;
}
