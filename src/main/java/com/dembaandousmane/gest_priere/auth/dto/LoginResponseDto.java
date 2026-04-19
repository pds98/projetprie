package com.dembaandousmane.gest_priere.auth.dto;


import lombok.*;

@Getter
@Setter


public class LoginResponseDto {

    private Long id ;
    private String email ;

    public LoginResponseDto(Long id, String email) {
        this.id = id;
        this.email = email;
    }




}
