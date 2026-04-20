package com.dembaandousmane.gest_priere.evenement.controllers;

import com.dembaandousmane.gest_priere.etudiant.model.Etudiant;
import com.dembaandousmane.gest_priere.etudiant.repository.EtudiantRepository;
import com.dembaandousmane.gest_priere.evenement.model.Evenement;
import com.dembaandousmane.gest_priere.evenement.repository.EvenementRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/evenements")
public class EvenementController {

    private final EvenementRepository evenementRepository;
    private final EtudiantRepository etudiantRepository;

    public EvenementController(EvenementRepository evenementRepository, EtudiantRepository etudiantRepository) {
        this.evenementRepository = evenementRepository;
        this.etudiantRepository  = etudiantRepository;
    }

    @GetMapping
    public List<Evenement> getTous() {
        return evenementRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> creer(@RequestBody Map<String, Object> body) {
        String nom         = (String) body.get("nom");
        String description = body.get("description") != null ? (String) body.get("description") : "";
        String lieu        = (String) body.get("lieu");
        String statut      = body.get("statut") != null ? (String) body.get("statut") : "actif";

        if (nom == null || lieu == null) {
            return ResponseEntity.badRequest().body(java.util.Map.of("message", "nom et lieu sont obligatoires."));
        }

        Long idCreateur = null;
        Object raw = body.get("idCreateurEvenement");
        if (raw instanceof Number) idCreateur = ((Number) raw).longValue();

        Etudiant createur = (idCreateur != null)
                ? etudiantRepository.findById(idCreateur).orElse(null)
                : null;

        Evenement evenement = Evenement.builder()
                .nom(nom)
                .description(description)
                .lieu(lieu)
                .dateCreation(LocalDateTime.now())
                .estActif(true)
                .statut(statut)
                .createurEvenement(createur)
                .build();

        return ResponseEntity.ok(evenementRepository.save(evenement));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Evenement> modifier(@PathVariable Long id, @RequestBody Evenement body) {
        Optional<Evenement> opt = evenementRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        Evenement e = opt.get();
        if (body.getNom()         != null) e.setNom(body.getNom());
        if (body.getDescription() != null) e.setDescription(body.getDescription());
        if (body.getLieu()        != null) e.setLieu(body.getLieu());
        if (body.getStatut()      != null) e.setStatut(body.getStatut());
        return ResponseEntity.ok(evenementRepository.save(e));
    }

    @DeleteMapping("/{id}")
    public void supprimer(@PathVariable Long id) {
        evenementRepository.deleteById(id);
    }

    // POST /api/evenements/{id}/participer  — corps : { "idEtudiant": 1 }
    @Transactional
    @PostMapping("/{id}/participer")
    public ResponseEntity<Void> participer(@PathVariable Long id, @RequestBody Map<String, Long> body) {
        Long idEtudiant = body.get("idEtudiant");
        Evenement evenement = evenementRepository.findById(id).orElse(null);
        Etudiant  etudiant  = (idEtudiant != null) ? etudiantRepository.findById(idEtudiant).orElse(null) : null;
        if (evenement == null || etudiant == null) return ResponseEntity.notFound().build();

        boolean dejaInscrit = etudiant.getEvenementsParticipe().stream()
                .anyMatch(e -> e.getId().equals(id));
        if (!dejaInscrit) {
            etudiant.getEvenementsParticipe().add(evenement);
            etudiantRepository.save(etudiant);
        }
        return ResponseEntity.ok().build();
    }

    // DELETE /api/evenements/{id}/quitter/{idEtudiant}
    @Transactional
    @DeleteMapping("/{id}/quitter/{idEtudiant}")
    public ResponseEntity<Void> quitter(@PathVariable Long id, @PathVariable Long idEtudiant) {
        Evenement evenement = evenementRepository.findById(id).orElse(null);
        Etudiant  etudiant  = etudiantRepository.findById(idEtudiant).orElse(null);
        if (evenement == null || etudiant == null) return ResponseEntity.notFound().build();

        etudiant.getEvenementsParticipe().removeIf(e -> e.getId().equals(id));
        etudiantRepository.save(etudiant);
        return ResponseEntity.ok().build();
    }
}
