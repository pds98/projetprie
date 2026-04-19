package com.dembaandousmane.gest_priere.forum.service;


import com.dembaandousmane.gest_priere.etudiant.model.Etudiant;
import com.dembaandousmane.gest_priere.etudiant.service.EtudiantService;
import com.dembaandousmane.gest_priere.forum.dto.ForumDto;
import com.dembaandousmane.gest_priere.forum.model.Forum;
import com.dembaandousmane.gest_priere.forum.repository.ForumRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ForumService {
     private final EtudiantService etudiantService;
     private final ForumRepository forumRepository;

    public ForumService(EtudiantService etudiantService, ForumRepository forumRepository) {
        this.etudiantService = etudiantService;
        this.forumRepository = forumRepository;
    }

    public ForumDto creerForum(Forum forum, Long id ){


        if(forum == null){
            throw new RuntimeException("le forum est null");
        }

        if(forum.getSujet() == null){

            throw new RuntimeException("le champ sujet est obligatoire");
        }


        Etudiant etudiant = etudiantService.trouverEtudiantParId(id);

        Forum forum1 = Forum.builder()
                .sujet(forum.getSujet())
                .dateCreation(forum.getDateCreation())
                .etudiant(etudiant)
                .build();

        Forum saved = forumRepository.save(forum1);

        ForumDto forumDto = ForumDto.builder().id(saved.getId())
                .dateCreation(saved.getDateCreation())
                .sujet(saved.getSujet()).build();

        return forumDto;
    }

   public void supprimerForumParId(Long id){
        Optional<Forum> forum =  forumRepository.findById(id);


        Forum  f= forum.orElseThrow(() -> new RuntimeException("forum non trouvé "));

        if (f != null){
            forumRepository.deleteById(id);
        }
   }

   public Forum trouverForumById(Long id){

       Optional<Forum> forum = forumRepository.findById(id);
       return forum.orElseThrow(() ->  new RuntimeException("le forum n'existe pas"));
   }
}
