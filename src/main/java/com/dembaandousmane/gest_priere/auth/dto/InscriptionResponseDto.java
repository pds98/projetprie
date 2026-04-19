package com.dembaandousmane.gest_priere.auth.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class InscriptionResponseDto {
    Long id ;
    String email ;
    String prenom;


    public InscriptionResponseDto(Long id , String email , String prenom){
        this.id = id;
        this.email = email;
        this.prenom = prenom;

    }


}
