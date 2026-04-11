package com.dembaandousmane.gest_priere.entity;

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


    @Builder.Default
    @ManyToMany(mappedBy = "evenements")
    private List<Etudiant> participants = new ArrayList<>();


    @ManyToOne
    @JoinColumn(name = "id_etudiant")
    private Etudiant createurEvenement;



    public void ajouterParticipant(Etudiant etudiant){
        if(this.participants == null){
            this.participants = new ArrayList<>();
        }

        participants.add(etudiant);
        etudiant.getEvenementsParticipe().add(this);
    }


    public void supprimerParticipant(Etudiant etudiant){
        if(this.participants != null){
            participants.remove(etudiant);
        }

        if(etudiant.getEvenementsParticipe()!= null){
            etudiant.getEvenementsParticipe().remove(this);
        }
    }






}
