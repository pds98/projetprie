package com.dembaandousmane.gest_priere.reservation.controllers;

import com.dembaandousmane.gest_priere.reservation.dto.ReservationRequestDto;
import com.dembaandousmane.gest_priere.reservation.dto.ReservationResponseDto;
import com.dembaandousmane.gest_priere.reservation.model.Reservation;
import com.dembaandousmane.gest_priere.reservation.service.ReservationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ReservationResponseDto creerReservation(@RequestBody ReservationRequestDto reservationRequestDto){
        return reservationService.creerReservation(reservationRequestDto);
    }

    @DeleteMapping("/{id}")
    public void annulerReservation(@PathVariable Long id){
        reservationService.annulerReservation(id);
    }

    @PutMapping("/{id}")
    public void modifierReservation(@PathVariable Long id, @RequestBody ReservationRequestDto dto){
        reservationService.modifierReservation(id, dto);
    }

    @GetMapping
    public List<Reservation> avoirToutLesReservation(){
        return reservationService.avoirToutLesReservation();
    }

}
