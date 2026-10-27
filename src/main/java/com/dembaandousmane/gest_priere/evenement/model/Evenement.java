package com.dembaandousmane.gest_priere.evenement.model;

import com.dembaandousmane.gest_priere.etudiant.model.Etudiant;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @JsonIgnore
    @Builder.Default
    @ManyToMany(mappedBy = "evenementsParticipe")
    private List<Etudiant> participants = new ArrayList<>();

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_etudiant")
    private Etudiant createurEvenement;
}
