package com.dembaandousmane.gest_priere.priere.model;

import com.dembaandousmane.gest_priere.reservation.model.Reservation;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Priere {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom; // ex: Fajr, Dhuhr, Asr, Maghrib, Isha

    @Column
    private String description;

    @Column
    private String heureDebut;

    @Column
    private String heureFin;

    @Column
    private String statut;

    @JsonIgnore
    @Builder.Default
    @OneToMany(mappedBy = "priere")
    private List<Reservation> reservations = new ArrayList<>();

    public void ajouterReservation(Reservation r) {
        if (this.reservations == null) this.reservations = new ArrayList<>();
        reservations.add(r);
        r.setPriere(this);
    }
}
