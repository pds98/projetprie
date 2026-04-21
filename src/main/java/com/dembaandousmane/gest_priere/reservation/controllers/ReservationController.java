package com.dembaandousmane.gest_priere.reservation.controllers;


import com.dembaandousmane.gest_priere.etudiant.model.Etudiant;
import com.dembaandousmane.gest_priere.evenement.model.Evenement;
import com.dembaandousmane.gest_priere.reservation.dto.ReservationResponseDto;
import com.dembaandousmane.gest_priere.reservation.model.Reservation;
import com.dembaandousmane.gest_priere.reservation.repository.ReservationRepository;
import com.dembaandousmane.gest_priere.reservation.service.ReservationService;
import com.dembaandousmane.gest_priere.salle.model.Salle;
import com.dembaandousmane.gest_priere.etudiant.repository.EtudiantRepository;
import com.dembaandousmane.gest_priere.salle.repository.SalleRepository;
import jakarta.transaction.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reservation/")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }


    @PostMapping()
    public ReservationResponseDto creerReservation(@RequestBody Reservation reservation, @RequestParam Long etudiantId, @RequestParam Long salleId, @RequestParam Long priereId ){

      return reservationService.creerReservation(reservation, etudiantId, salleId, priereId);

    }

    @DeleteMapping
    @Transactional
    public void annulerReservation(@RequestParam Long idReservation){

         reservationService.annulerReservation(idReservation);
    }

    public void modifierReservation(){}

    @GetMapping
    public List<Reservation> avoirToutLesReservation(){

        return reservationService.avoirToutLesReservation();
    }

}
