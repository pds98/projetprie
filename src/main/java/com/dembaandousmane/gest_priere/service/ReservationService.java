package com.dembaandousmane.gest_priere.service;

import com.dembaandousmane.gest_priere.entity.Reservation;
import com.dembaandousmane.gest_priere.entity.Salle;
import com.dembaandousmane.gest_priere.repository.ReservationRepository;
import com.dembaandousmane.gest_priere.repository.SalleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private SalleRepository salleRepository;

    public Reservation creerReservation(Reservation r) {
        if (!r.getFin().isAfter(r.getDebut())) {
            throw new IllegalArgumentException("La date de fin doit être après la date de début.");
        }
        if (r.getNombrePersonnes() <= 0) {
            throw new IllegalArgumentException("Le nombre de personnes doit être supérieur à 0.");
        }

        Salle salle = salleRepository.findById(r.getIdSalle())
                .orElseThrow(() -> new IllegalArgumentException("Salle introuvable (ID: " + r.getIdSalle() + ")."));

        if (!"disponible".equalsIgnoreCase(salle.getStatut())) {
            throw new IllegalArgumentException("La salle n'est pas disponible.");
        }
        if (r.getNombrePersonnes() > salle.getCapacite()) {
            throw new IllegalArgumentException("Le nombre de personnes dépasse la capacité de la salle (" + salle.getCapacite() + ").");
        }

        // Marquer la salle comme occupée
        salle.setStatut("occupée");
        salleRepository.save(salle);

        r.setEstReserver(true);
        return reservationRepository.save(r);
    }

    public Optional<Reservation> obtenirReservation(int id) {
        return reservationRepository.findById(id);
    }

    public List<Reservation> obtenirToutesLesReservations() {
        return reservationRepository.findAll();
    }

    public List<Reservation> obtenirReservationsParEtudiant(int idEtudiant) {
        return reservationRepository.findByIdEtudiant(idEtudiant);
    }

    public Reservation modifierReservation(int id, Reservation r) {
        if (!reservationRepository.existsById(id)) {
            throw new IllegalArgumentException("Réservation introuvable (ID: " + id + ").");
        }
        if (!r.getFin().isAfter(r.getDebut())) {
            throw new IllegalArgumentException("La date de fin doit être après la date de début.");
        }
        r.setId(id);
        return reservationRepository.save(r);
    }

    public Reservation annulerReservation(int id) {
        Reservation r = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Réservation introuvable (ID: " + id + ")."));

        // Libérer la salle
        salleRepository.findById(r.getIdSalle()).ifPresent(salle -> {
            salle.libererSalle();
            salleRepository.save(salle);
        });

        r.setEstReserver(false);
        return reservationRepository.save(r);
    }

    public void supprimerReservation(int id) {
        if (!reservationRepository.existsById(id)) {
            throw new IllegalArgumentException("Réservation introuvable (ID: " + id + ").");
        }
        reservationRepository.deleteById(id);
    }
}
