package com.dembaandousmane.gest_priere.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Groupe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String description;

    @ManyToMany(mappedBy = "etudiants")
    private ArrayList <Etudiant> etudiants;







}
