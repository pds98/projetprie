package com.dembaandousmane.gest_priere.forum.controllers;

import com.dembaandousmane.gest_priere.forum.model.Forum;
import com.dembaandousmane.gest_priere.forum.repository.ForumRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/forums")
public class ForumController {

    private final ForumRepository forumRepository;

    public ForumController(ForumRepository forumRepository) {
        this.forumRepository = forumRepository;
    }

    @GetMapping
    public List<Forum> getTous() {
        return forumRepository.findAll();
    }

    @PostMapping
    public Forum creer(@RequestBody Forum forum) {
        if (forum.getDateCreation() == null) forum.setDateCreation(LocalDateTime.now());
        return forumRepository.save(forum);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Forum> modifier(@PathVariable Long id, @RequestBody Forum body) {
        Optional<Forum> opt = forumRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        Forum f = opt.get();
        if (body.getSujet() != null) f.setSujet(body.getSujet());
        return ResponseEntity.ok(forumRepository.save(f));
    }

    @DeleteMapping("/{id}")
    public void supprimer(@PathVariable Long id) {
        forumRepository.deleteById(id);
    }
}
