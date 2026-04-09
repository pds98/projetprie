package com.dembaandousmane.gest_priere.entity;

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
    private List <Groupe> groupes;


    @ManyToMany
    @JoinTable(name = "detail_evenement",
            joinColumns = @JoinColumn(name = "id_etudiant"),
            inverseJoinColumns = @JoinColumn(name = "id_evenement"))
    private List<Evenement> evenements;


    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_salle")
    private Salle salle;



    public void ajouterEvenement(Evenement evenement){
        if (this.evenements == null){
            this.evenements = new ArrayList<>();
        }

        evenements.add(evenement);


        evenement.getEtudiants().add(this);
    }

    public void supprimerEvenement(){}

    public void AjouterGroupe(Groupe groupe){
        if(this.groupes == null){
            groupes = new ArrayList<>();
        }

        groupes.add(groupe);

        groupe.getEtudiants().add(this);
    }

    public void supprimerGroupe(){}







}
