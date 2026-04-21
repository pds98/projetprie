package com.dembaandousmane.gest_priere.message.dto;


import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequestDto {

    private String contenu;
    private Long idEtudiant;
    private Long idForum ;
}
