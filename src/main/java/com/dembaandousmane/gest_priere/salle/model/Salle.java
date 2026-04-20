package com.dembaandousmane.gest_priere.salle.model;

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
public class Salle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idSalle")
    private Long idSalle;

    @Column(nullable = false)
    private int capacite;

    @Column(nullable = false)
    private String statut;

    @Column(nullable = false)
    private int NumeroSalle;

    // @Builder.Default : valeur par defaut = false pour eviter erreur NOT NULL en base
    @Column(name = "est_reserver", nullable = false)
    @Builder.Default
    private Boolean estReserver = false;

    @JsonIgnore
    @OneToMany(mappedBy = "salle")
    private List<Reservation> reservations = new ArrayList<>();

    public boolean estLibre() {
        return !this.estReserver;
    }

    public void reserverSalle() {
        this.estReserver = true;
        this.statut = "reservee";
    }

    public void libererSalle() {
        this.estReserver = false;
        this.statut = "libre";
    }

    public void ajouterReservation(Reservation r) {
        if (this.reservations == null) this.reservations = new ArrayList<>();
        reservations.add(r);
        r.setSalle(this);
    }

    public void supprimerReservation(Reservation r) {
        if (r.getSalle() == this) r.setSalle(null);
        reservations.remove(r);
    }
}
