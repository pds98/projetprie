package com.dembaandousmane.gest_priere.groupe.service;


import com.dembaandousmane.gest_priere.etudiant.model.Etudiant;
import com.dembaandousmane.gest_priere.etudiant.repository.EtudiantRepository;
import com.dembaandousmane.gest_priere.etudiant.service.EtudiantService;
import com.dembaandousmane.gest_priere.groupe.controllers.GroupeController;
import com.dembaandousmane.gest_priere.groupe.dto.GroupeDtoResponse;
import com.dembaandousmane.gest_priere.groupe.dto.GroupeRequestDto;
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

    public GroupeDtoResponse creerGroupe(GroupeRequestDto requestDto){

        if(requestDto.getDescription() == null){
            throw new RuntimeException("le champ de description est obligatoire");
        }
        if(requestDto.getEtudiantId() == null){
            throw new RuntimeException("l'id etudiant doit etre obligatoire ");
        }



        Etudiant e = etudiantService.trouverEtudiantParId(requestDto.getEtudiantId());

        Groupe g = Groupe.builder().nom(requestDto.getNom())
                .description(requestDto.getDescription()).build();



        e.creerGroupe(g);

        Groupe saved = groupeRepository.save(g);

        GroupeDtoResponse groupeDtoResponse = GroupeDtoResponse.builder().id(saved.getId()).nom(saved.getNom())
                .description(saved.getDescription())
                .createurNom(saved.getCreateurGroupe().getNom())
                .build();

        return groupeDtoResponse;


    }

    public void supprimerGroupe(Long groupeId){


        Groupe groupe = groupeRepository.findById(groupeId).orElseThrow(() ->  new RuntimeException("groupe n'existe pas"));
        Etudiant etudiant = groupe.getCreateurGroupe();

        etudiant.retirerGroupe(groupe);
        groupeRepository.delete(groupe);



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
