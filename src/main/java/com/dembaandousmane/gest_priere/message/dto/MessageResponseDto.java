package com.dembaandousmane.gest_priere.message.dto;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponseDto {

    private Long id ;
    private String contenu;
    private LocalDateTime dateCreation;
    private int forumId;
}
