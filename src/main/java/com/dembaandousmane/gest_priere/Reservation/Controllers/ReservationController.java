package com.dembaandousmane.gest_priere.Reservation.Controllers;


import com.dembaandousmane.gest_priere.Etudiant.Model.Etudiant;
import com.dembaandousmane.gest_priere.Reservation.Model.Reservation;
import com.dembaandousmane.gest_priere.Reservation.Repository.ReservationRepository;
import com.dembaandousmane.gest_priere.Salle.Model.Salle;
import com.dembaandousmane.gest_priere.Etudiant.Repository.EtudiantRepository;
import com.dembaandousmane.gest_priere.Salle.Repository.SalleRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
public class ReservationController {
     EtudiantRepository etudiantRepository;
     SalleRepository salleRepository;
     ReservationRepository reservationRepository;


     public ReservationController(EtudiantRepository etudiantRepository, SalleRepository salleRepository, ReservationRepository reservationRepository){
         this.etudiantRepository = etudiantRepository;
         this.salleRepository = salleRepository;
         this.reservationRepository = reservationRepository;
     }


    @PostMapping
    public void creerReservation(@RequestBody Reservation reservation){

        LocalDateTime localDateTime = LocalDateTime.now();
        Optional<Etudiant> etudiant = etudiantRepository.findById(reservation.getEtudiant().getId());
        Optional<Salle> salle = salleRepository.findById(reservation.getSalle().getIdSalle());

        Etudiant e = etudiant.orElseThrow(() ->
                new RuntimeException("Etudiant introuvable"));

        Salle s = salle.orElseThrow(() ->
                new RuntimeException("Etudiant introuvable"));


        Reservation r = Reservation.builder().debut(localDateTime)
                .fin(localDateTime).motif(reservation.getMotif())
                .etudiant(reservation.getEtudiant())
                .salle(reservation.getSalle())
                .build();

        e.ajouterReservation(r);
        s.ajouterReservation(r);

        etudiantRepository.save(e);
        salleRepository.save(s);
        reservationRepository.save(r);


    }

    public void annulerReservation(){}

    public void modifierReservation(){}

}
