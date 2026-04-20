package com.dembaandousmane.gest_priere.priere.model;

import jakarta.persistence.*;
import lombok.*;

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
    private String heureDebut; // ex: "05:30"

    @Column
    private String heureFin;   // ex: "06:00"

    @Column
    private String statut; // ex: "actif", "inactif"
}
