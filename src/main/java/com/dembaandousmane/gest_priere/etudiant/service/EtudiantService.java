package com.dembaandousmane.gest_priere.etudiant.service;

import com.dembaandousmane.gest_priere.auth.dto.InscriptionResponseDto;
import com.dembaandousmane.gest_priere.auth.dto.LoginResponseDto;
import com.dembaandousmane.gest_priere.etudiant.model.Etudiant;
import com.dembaandousmane.gest_priere.etudiant.repository.EtudiantRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EtudiantService {

    private final EtudiantRepository etudiantRepository;

    public EtudiantService(EtudiantRepository etudiantRepository){
        this.etudiantRepository = etudiantRepository;
    }


    public InscriptionResponseDto creerEtudiant(Etudiant etudiant){

         if(etudiantRepository.existsByEmail(etudiant.getEmail())){

             throw new RuntimeException("Email deja utilisé");

         }

         if(etudiant.getPrenom() == null || etudiant.getPrenom().isEmpty() ){
             throw new RuntimeException("champ obligatoire Prenom");
         }

        Etudiant e = Etudiant.builder()
                .prenom(etudiant.getPrenom())
                .nom(etudiant.getNom())
                .telephone(etudiant.getTelephone())
                .email(etudiant.getEmail())
                .build();

        Etudiant saved =  etudiantRepository.save(e);

        return new InscriptionResponseDto(
                saved.getId(),
                saved.getEmail(),
                saved.getPrenom())
        ;


    }

    public LoginResponseDto trouverEtudiantParEmail(Etudiant etudiant){

        Optional<Etudiant> etudiant1 =  etudiantRepository.findByEmail(etudiant.getEmail());
        Etudiant e = etudiant1.orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return new LoginResponseDto(e.getId(), e.getEmail());


    }

    public Etudiant trouverEtudiantParId(Long id){
        Optional<Etudiant> etudiant = etudiantRepository.findById(id);
        Etudiant e = etudiant.orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return e ;
    }

}
