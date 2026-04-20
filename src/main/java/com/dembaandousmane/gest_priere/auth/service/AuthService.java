package com.dembaandousmane.gest_priere.auth.service;

import com.dembaandousmane.gest_priere.auth.dto.InscriptionResponseDto;
import com.dembaandousmane.gest_priere.etudiant.model.Etudiant;
import com.dembaandousmane.gest_priere.etudiant.service.EtudiantService;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final EtudiantService etudiantService;

    public AuthService(EtudiantService etudiantService) {
        this.etudiantService = etudiantService;
    }

    public InscriptionResponseDto inscription(Etudiant etudiant) {
        return etudiantService.creerEtudiant(etudiant);
    }

    // La connexion est geree directement dans AuthControllers via EtudiantRepository
}
