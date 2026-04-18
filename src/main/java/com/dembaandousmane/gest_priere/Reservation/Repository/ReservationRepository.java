package com.dembaandousmane.gest_priere.Reservation.Repository;

import com.dembaandousmane.gest_priere.Reservation.Model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

}
