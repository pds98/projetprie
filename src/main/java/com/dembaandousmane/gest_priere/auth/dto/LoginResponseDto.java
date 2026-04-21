package com.dembaandousmane.gest_priere.auth.dto;


import lombok.*;

@Getter
@Setter


public class LoginResponseDto {

    private Long id ;
    private String email ;
    private String prenom ;
    private String nom ;

    public LoginResponseDto(Long id, String email, String prenom, String nom) {
        this.id = id;
        this.email = email;
        this.prenom = prenom;
        this.nom = nom;
    }




}
