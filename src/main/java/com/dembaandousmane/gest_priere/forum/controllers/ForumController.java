package com.dembaandousmane.gest_priere.forum.controllers;

import com.dembaandousmane.gest_priere.etudiant.model.Etudiant;
import com.dembaandousmane.gest_priere.etudiant.repository.EtudiantRepository;
import com.dembaandousmane.gest_priere.forum.model.Forum;
import com.dembaandousmane.gest_priere.forum.repository.ForumRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/forums")
public class ForumController {

    private final ForumRepository forumRepository;
    private final EtudiantRepository etudiantRepository;

    public ForumController(ForumRepository forumRepository, EtudiantRepository etudiantRepository) {
        this.forumRepository = forumRepository;
        this.etudiantRepository = etudiantRepository;
    }

    @GetMapping
    public List<Forum> getTous() {
        return forumRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> creer(@RequestBody Map<String, Object> body) {
        String sujet = (String) body.get("sujet");
        if (sujet == null || sujet.isBlank()) {
            return ResponseEntity.badRequest().body(java.util.Map.of("message", "Le sujet est obligatoire."));
        }

        Long idEtudiant = null;
        Object raw = body.get("idEtudiant");
        if (raw instanceof Number) idEtudiant = ((Number) raw).longValue();

        Etudiant etudiant = (idEtudiant != null)
                ? etudiantRepository.findById(idEtudiant).orElse(null)
                : null;

        Forum forum = Forum.builder()
                .sujet(sujet)
                .dateCreation(LocalDateTime.now())
                .etudiant(etudiant)
                .build();

        return ResponseEntity.ok(forumRepository.save(forum));
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
