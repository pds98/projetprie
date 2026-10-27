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
@RequestMapping("/api/forums")
public class ForumController {
   private final ForumService forumService;


    public ForumController(ForumService forumService) {
        this.forumService = forumService;
    }

    @GetMapping
    public java.util.List<Forum> getTousLesForums(){
        return forumService.getTousLesForums();
    }

    @PostMapping
    public ForumDto creerForum(@RequestBody ForumRequestDto forumRequestDto){
        return forumService.creerForum(forumRequestDto);
    }

    @PutMapping("/{idForum}")
    public ForumDto modifierForum(@PathVariable Long idForum, @RequestBody ForumRequestDto forumRequestDto){
        return forumService.modifierForum(idForum, forumRequestDto);
    }

    @DeleteMapping("/{idForum}")
    @Transactional
    public void supprimerForumParId(@PathVariable Long idForum){
       forumService.supprimerForumParId(idForum);
    }

}
