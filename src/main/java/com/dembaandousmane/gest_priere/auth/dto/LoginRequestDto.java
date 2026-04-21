package com.dembaandousmane.gest_priere.auth.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {

    private String email ;
    private String motDePasse;
}
