package com.dembaandousmane.gest_priere.reservation.repository;

import com.dembaandousmane.gest_priere.reservation.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

}
