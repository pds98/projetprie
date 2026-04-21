package com.dembaandousmane.gest_priere.groupe.dto;


import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupeRequestDto {

    private String nom;
    private String description;
    private Long etudiantId;
}
