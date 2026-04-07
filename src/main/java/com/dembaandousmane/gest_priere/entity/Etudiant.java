package com.dembaandousmane.gest_priere.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Entity
@Getter
@Setter
@Builder
public class Etudiant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column(nullable = false,unique = true)
    private String telephone;

    @Column(nullable = false,unique = true)
    private String email;

    @OneToMany(mappedBy = "etudiant")
    private ArrayList <Reservation> reservations;



    @ManyToMany
    @JoinTable(
            name = "detail_groupe",
            joinColumns = @JoinColumn(name = "id_etudiant"),
            inverseJoinColumns = @JoinColumn(name = "id_groupe" )
    )
    private ArrayList <Groupe> groupes;


    @ManyToMany
    @JoinTable(name = "detail_evenement",
            joinColumns = @JoinColumn(name = "id_etudiant"),
            inverseJoinColumns = @JoinColumn(name = "id_evenement"))
    private ArrayList<Evenement> evenements;


    @ManyToOne
    @JoinColumn(name = "id_salle")
    private Salle salle;


    public Etudiant() {}

    public Etudiant(String nom, String prenom, String telephone, String email) {
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.email = email;
    }







}
