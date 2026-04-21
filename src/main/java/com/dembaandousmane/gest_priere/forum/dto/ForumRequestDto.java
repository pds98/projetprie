package com.dembaandousmane.gest_priere.forum.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ForumRequestDto {
    private String sujet;
    private Long idEtudiant;
    private LocalDateTime DateCreation;
}
