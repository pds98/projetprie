package com.dembaandousmane.gest_priere.reservation.controllers;

import com.dembaandousmane.gest_priere.reservation.model.Reservation;
import com.dembaandousmane.gest_priere.reservation.repository.ReservationRepository;
import com.dembaandousmane.gest_priere.etudiant.model.Etudiant;
import com.dembaandousmane.gest_priere.etudiant.repository.EtudiantRepository;
import com.dembaandousmane.gest_priere.salle.model.Salle;
import com.dembaandousmane.gest_priere.salle.repository.SalleRepository;
import com.dembaandousmane.gest_priere.priere.repository.PriereRepository;
import com.dembaandousmane.gest_priere.priere.model.Priere;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationRepository reservationRepository;
    private final EtudiantRepository etudiantRepository;
    private final SalleRepository salleRepository;
    private final PriereRepository priereRepository;

    public ReservationController(ReservationRepository reservationRepository,
                                 EtudiantRepository etudiantRepository,
                                 SalleRepository salleRepository,
                                 PriereRepository priereRepository) {
        this.reservationRepository = reservationRepository;
        this.etudiantRepository    = etudiantRepository;
        this.salleRepository       = salleRepository;
        this.priereRepository      = priereRepository;
    }

    @GetMapping
    public List<Reservation> getTous() {
        return reservationRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> creer(@RequestBody DonneesReservation donnees) {
        Etudiant etudiant = (donnees.idEtudiant != null) ? etudiantRepository.findById(donnees.idEtudiant).orElse(null) : null;
        Salle salle       = (donnees.idSalle    != null) ? salleRepository.findById(donnees.idSalle).orElse(null)       : null;
        Priere priere     = (donnees.idPriere   != null) ? priereRepository.findById(donnees.idPriere).orElse(null)     : null;

        if (salle != null && !salle.estLibre()) {
            return ResponseEntity.status(409).body(java.util.Map.of("message", "La salle est deja reservee."));
        }

        Reservation r = Reservation.builder()
                .debut(donnees.debut)
                .fin(donnees.fin)
                .motif(donnees.motif)
                .nombrePersonnes(donnees.nombrePersonnes)
                .estActif(true)
                .etudiant(etudiant)
                .salle(salle)
                .priere(priere)
                .build();

        if (salle != null) { salle.reserverSalle(); salleRepository.save(salle); }

        return ResponseEntity.ok(reservationRepository.save(r));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reservation> modifier(@PathVariable Long id, @RequestBody DonneesReservation body) {
        Optional<Reservation> opt = reservationRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        Reservation r = opt.get();
        if (body.motif           != null) r.setMotif(body.motif);
        if (body.nombrePersonnes != 0)    r.setNombrePersonnes(body.nombrePersonnes);
        return ResponseEntity.ok(reservationRepository.save(r));
    }

    @DeleteMapping("/{id}")
    public void supprimer(@PathVariable Long id) {
        Reservation r = reservationRepository.findById(id).orElse(null);
        if (r != null && r.getSalle() != null) {
            Salle salle = r.getSalle();
            salle.libererSalle();
            salleRepository.save(salle);
        }
        reservationRepository.deleteById(id);
    }

    public static class DonneesReservation {
        public Long idEtudiant;
        public Long idSalle;
        public Long idPriere;
        public LocalDateTime debut;
        public LocalDateTime fin;
        public int nombrePersonnes;
        public String motif;
    }
}
