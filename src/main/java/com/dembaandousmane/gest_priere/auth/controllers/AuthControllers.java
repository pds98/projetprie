package com.dembaandousmane.gest_priere.auth.controllers;


import com.dembaandousmane.gest_priere.auth.dto.InscriptionResponseDto;
import com.dembaandousmane.gest_priere.auth.dto.LoginResponseDto;
import com.dembaandousmane.gest_priere.auth.service.AuthService;
import com.dembaandousmane.gest_priere.etudiant.model.Etudiant;
import com.dembaandousmane.gest_priere.etudiant.service.EtudiantService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthControllers {

   private final  AuthService authService;

    public AuthControllers(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/register")
    public InscriptionResponseDto inscription(@RequestBody Etudiant etudiant){
        return authService.inscription(etudiant);


    }
    @PostMapping("/login")
    public LoginResponseDto connexion(@RequestBody Etudiant etudiant){
        return authService.connexion(etudiant);
    }

}
