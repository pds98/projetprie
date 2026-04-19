package com.dembaandousmane.gest_priere.groupe.dto;

import com.dembaandousmane.gest_priere.groupe.model.Groupe;
import lombok.*;
import org.springframework.stereotype.Service;

import java.util.List;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupeDtoResponse {

    private Long id;
    private String nom;
    private String description;
    private String createurNom;
    private int membre;




}
