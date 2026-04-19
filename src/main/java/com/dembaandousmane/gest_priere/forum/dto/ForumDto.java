package com.dembaandousmane.gest_priere.forum.dto;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForumDto {

    private Long id ;
    private String sujet;
    private LocalDateTime dateCreation ;
    private int message;
}
