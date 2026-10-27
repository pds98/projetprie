package com.dembaandousmane.gest_priere.forum.service;


import com.dembaandousmane.gest_priere.etudiant.model.Etudiant;
import com.dembaandousmane.gest_priere.etudiant.service.EtudiantService;
import com.dembaandousmane.gest_priere.forum.dto.ForumDto;
import com.dembaandousmane.gest_priere.forum.dto.ForumRequestDto;
import com.dembaandousmane.gest_priere.forum.model.Forum;
import com.dembaandousmane.gest_priere.forum.repository.ForumRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ForumService {
     private final EtudiantService etudiantService;
     private final ForumRepository forumRepository;

    public ForumService(EtudiantService etudiantService, ForumRepository forumRepository) {
        this.etudiantService = etudiantService;
        this.forumRepository = forumRepository;
    }

    public ForumDto creerForum(ForumRequestDto requestDto){


        if(requestDto.getIdEtudiant() == null){
            throw new RuntimeException("l'id etudiant est obligatoire...");
        }

        if(requestDto.getSujet() == null){

            throw new RuntimeException("le champ sujet est obligatoire");
        }


        Etudiant etudiant = etudiantService.trouverEtudiantParId(requestDto.getIdEtudiant());

        Forum forum = Forum.builder()
                .sujet(requestDto.getSujet())
                .dateCreation(requestDto.getDateCreation() != null ? requestDto.getDateCreation() : java.time.LocalDateTime.now())
                .etudiant(etudiant)
                .build();

        Forum saved = forumRepository.save(forum);

        ForumDto forumDto = ForumDto.builder().id(saved.getId())
                .dateCreation(saved.getDateCreation())
                .sujet(saved.getSujet()).build();

        return forumDto;
    }

   public List<Forum> getTousLesForums(){
       return forumRepository.findAll();
   }

   public ForumDto modifierForum(Long id, ForumRequestDto requestDto){
       Forum forum = trouverForumById(id);
       if(requestDto.getSujet() != null && !requestDto.getSujet().isBlank()){
           forum.setSujet(requestDto.getSujet());
       }
       Forum saved = forumRepository.save(forum);
       return ForumDto.builder().id(saved.getId())
               .dateCreation(saved.getDateCreation())
               .sujet(saved.getSujet()).build();
   }

   public void supprimerForumParId(Long id){
       Forum forum = trouverForumById(id);

        forum.setEtudiant(null);
        forumRepository.delete(forum);

   }

   public Forum trouverForumById(Long id){

       Optional<Forum> forum = forumRepository.findById(id);
       return forum.orElseThrow(() ->  new RuntimeException("le forum n'existe pas"));
   }
}
