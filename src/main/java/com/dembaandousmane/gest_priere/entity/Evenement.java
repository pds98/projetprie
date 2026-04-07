package com.dembaandousmane.gest_priere.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Entity

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Evenement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private LocalDateTime dateCreation;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String lieu;

    @Column(nullable = false)
    private boolean estActif;

    @ManyToMany(mappedBy = "etudiants")
    private ArrayList<Etudiant> etudiants;





}
