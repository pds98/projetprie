package com.dembaandousmane.gest_priere.forum.controllers;


import com.dembaandousmane.gest_priere.etudiant.model.Etudiant;
import com.dembaandousmane.gest_priere.etudiant.repository.EtudiantRepository;
import com.dembaandousmane.gest_priere.etudiant.service.EtudiantService;
import com.dembaandousmane.gest_priere.forum.dto.ForumDto;
import com.dembaandousmane.gest_priere.forum.dto.ForumRequestDto;
import com.dembaandousmane.gest_priere.forum.model.Forum;
import com.dembaandousmane.gest_priere.forum.service.ForumService;
import jakarta.transaction.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/forum/")
public class ForumController {
   private final ForumService forumService;


    public ForumController(ForumService forumService) {
        this.forumService = forumService;

    }

    @PostMapping
    public ForumDto creerForum(@RequestBody ForumRequestDto forumRequestDto){
        return forumService.creerForum(forumRequestDto);
    }

    @DeleteMapping
    @Transactional
    public void supprimerForumParId(@RequestParam Long idForum){

       forumService.supprimerForumParId(idForum);
    }

    @PutMapping
    public void modifierForum(){}



}
