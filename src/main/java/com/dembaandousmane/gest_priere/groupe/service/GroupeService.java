package com.dembaandousmane.gest_priere.groupe.service;


import com.dembaandousmane.gest_priere.etudiant.model.Etudiant;
import com.dembaandousmane.gest_priere.etudiant.repository.EtudiantRepository;
import com.dembaandousmane.gest_priere.etudiant.service.EtudiantService;
import com.dembaandousmane.gest_priere.groupe.controllers.GroupeController;
import com.dembaandousmane.gest_priere.groupe.dto.GroupeDtoResponse;
import com.dembaandousmane.gest_priere.groupe.model.Groupe;
import com.dembaandousmane.gest_priere.groupe.repository.GroupeRepository;
import com.dembaandousmane.gest_priere.reservation.model.Reservation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupeService {

    private final EtudiantService etudiantService;
    private final GroupeRepository groupeRepository;

    public GroupeService(EtudiantService etudiantService, GroupeRepository groupeRepository) {
        this.etudiantService = etudiantService;
        this.groupeRepository = groupeRepository;
    }

    public GroupeDtoResponse creerGroupe(Groupe groupe, Long id ){

        if(groupe == null){
            throw new RuntimeException("le groupe est null");
        }
        if(groupe.getNom() == null){
            throw new RuntimeException("le nom est obligatoire");
        }



        Etudiant e = etudiantService.trouverEtudiantParId(id);

        Groupe g = Groupe.builder().nom(groupe.getNom())
                .description(groupe.getDescription())
                .createurGroupe(e).build();


        g.ajouterMembre(e);

        Groupe saved = groupeRepository.save(g);

        GroupeDtoResponse groupeDtoResponse = GroupeDtoResponse.builder().id(saved.getId()).nom(saved.getNom())
                .description(saved.getDescription())
                .createurNom(saved.getCreateurGroupe().getNom())
                .build();

        return groupeDtoResponse;


    }

    public void supprimerGroupe(Long groupeId, Long etudiantId){

        if(groupeId == 0 || etudiantId == 0){
            throw new RuntimeException("l'id du groupe ou le etudiantId est null");
        }

        Groupe groupe = groupeRepository.findById(groupeId).orElseThrow(() ->  new RuntimeException("groupe n'existe pas"));
        Etudiant etudiant = etudiantService.trouverEtudiantParId(etudiantId);


        groupeRepository.delete(groupe);
        etudiant.retirerGroupe(groupe);


    }

    public void rejoindreGroupe(Long groupeId, Long etudiantId){

        if(groupeId == 0 || etudiantId == 0){
            throw new RuntimeException("le groupe id , etudiant id");
        }

        Groupe groupe = groupeRepository.findById(groupeId).orElseThrow(() -> new RuntimeException("groupe existe pas"));
        Etudiant etudiant = etudiantService.trouverEtudiantParId(etudiantId);

        etudiant.rejoindreGroupe(groupe);


    }

    public void quitterGroupe(Long groupeId, Long etudiantId){


        if(groupeId == 0 || etudiantId == 0){
            throw new RuntimeException("le groupe id , etudiant id");
        }

        Groupe groupe = groupeRepository.findById(groupeId).orElseThrow(() -> new RuntimeException("groupe existe pas"));
        Etudiant etudiant = etudiantService.trouverEtudiantParId(etudiantId);

        etudiant.quitterGroupe(groupe);
    }

    public List<Groupe> avoirToutlesGroupe(){
       return  groupeRepository.findAll();
    }
}
