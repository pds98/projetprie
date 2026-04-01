package com.dembaandousmane.gest_priere.service;

import com.dembaandousmane.gest_priere.entity.Forum;
import com.dembaandousmane.gest_priere.repository.ForumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ForumService {

    @Autowired
    private ForumRepository forumRepository;

    public Forum ajouterForum(Forum f) {
        if (f.getSujet() == null || f.getSujet().isEmpty()) {
            throw new IllegalArgumentException("Le sujet du forum est obligatoire.");
        }
        return forumRepository.save(f);
    }

    public Optional<Forum> obtenirForum(int id) {
        return forumRepository.findById(id);
    }

    public List<Forum> obtenirTousLesForums() {
        return forumRepository.findAll();
    }

    public Forum modifierForum(int id, Forum f) {
        if (!forumRepository.existsById(id)) {
            throw new IllegalArgumentException("Forum introuvable (ID: " + id + ").");
        }
        f.setIdForum(id);
        return forumRepository.save(f);
    }

    public void supprimerForum(int id) {
        if (!forumRepository.existsById(id)) {
            throw new IllegalArgumentException("Forum introuvable (ID: " + id + ").");
        }
        forumRepository.deleteById(id);
    }
}
