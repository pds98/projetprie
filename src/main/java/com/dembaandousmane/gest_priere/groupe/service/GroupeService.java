package com.dembaandousmane.gest_priere.groupe.service;


import com.dembaandousmane.gest_priere.etudiant.model.Etudiant;
import com.dembaandousmane.gest_priere.etudiant.repository.EtudiantRepository;
import com.dembaandousmane.gest_priere.etudiant.service.EtudiantService;
import com.dembaandousmane.gest_priere.groupe.dto.GroupeDtoResponse;
import com.dembaandousmane.gest_priere.groupe.dto.GroupeRequestDto;
import com.dembaandousmane.gest_priere.groupe.model.Groupe;
import com.dembaandousmane.gest_priere.groupe.repository.GroupeRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupeService {

    private final EtudiantService etudiantService;
    private final EtudiantRepository etudiantRepository;
    private final GroupeRepository groupeRepository;

    public GroupeService(EtudiantService etudiantService, EtudiantRepository etudiantRepository, GroupeRepository groupeRepository) {
        this.etudiantService = etudiantService;
        this.etudiantRepository = etudiantRepository;
        this.groupeRepository = groupeRepository;
    }

    private GroupeDtoResponse toDto(Groupe g) {
        List<Long> membreIds = g.getMembres().stream()
                .map(Etudiant::getId)
                .collect(Collectors.toList());
        return GroupeDtoResponse.builder()
                .id(g.getId())
                .nom(g.getNom())
                .description(g.getDescription())
                .createurNom(g.getCreateurGroupe() != null ? g.getCreateurGroupe().getNom() : "")
                .createurId(g.getCreateurGroupe() != null ? g.getCreateurGroupe().getId() : null)
                .membre(membreIds.size())
                .membreIds(membreIds)
                .build();
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
        return toDto(saved);


    }

    public void supprimerGroupe(Long groupeId){


        Groupe groupe = groupeRepository.findById(groupeId).orElseThrow(() ->  new RuntimeException("groupe n'existe pas"));
        Etudiant etudiant = groupe.getCreateurGroupe();

        etudiant.retirerGroupe(groupe);
        groupeRepository.delete(groupe);



    }

    @Transactional
    public void rejoindreGroupe(Long groupeId, Long etudiantId){
        Groupe groupe = groupeRepository.findById(groupeId).orElseThrow(() -> new RuntimeException("groupe existe pas"));
        Etudiant etudiant = etudiantService.trouverEtudiantParId(etudiantId);

        if (!etudiant.getGroupesRejoins().contains(groupe)) {
            etudiant.rejoindreGroupe(groupe);
            etudiantRepository.save(etudiant);
        }
    }

    @Transactional
    public void quitterGroupe(Long groupeId, Long etudiantId){
        Groupe groupe = groupeRepository.findById(groupeId).orElseThrow(() -> new RuntimeException("groupe existe pas"));
        Etudiant etudiant = etudiantService.trouverEtudiantParId(etudiantId);

        etudiant.quitterGroupe(groupe);
        etudiantRepository.save(etudiant);
    }

    public List<GroupeDtoResponse> avoirToutlesGroupe(){
        return groupeRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
