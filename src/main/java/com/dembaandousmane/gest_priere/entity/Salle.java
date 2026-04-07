package com.dembaandousmane.gest_priere.entity;

import jakarta.persistence.*;
import lombok.*;

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
    private int idSalle;

    @Column(nullable = false)
    private int capacite;

    @Column(nullable = false)
    private String statut;

    @OneToMany(mappedBy = "etudiant")
    private List<Etudiant> etudiants;


    @OneToMany(mappedBy = "salle")
    private Reservation reservation;


}
