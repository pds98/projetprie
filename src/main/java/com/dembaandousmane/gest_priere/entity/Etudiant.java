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
    private List <Reservation> reservations;



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


    @OneToMany(mappedBy = "etudiant")
    private List<Forum> forums;




    public void rejoindreEvenement(Evenement evenement){
        if (this.evenements == null){
            this.evenements = new ArrayList<>();
        }

        evenements.add(evenement);


        evenement.getEtudiants().add(this);
    }

    public void quitterEvenement(Evenement evenement){

        if(this.evenements != null){
            evenements.remove(evenement);
        }
        if(evenement.getEtudiants().contains(this)){
            evenement.getEtudiants().remove(this);
        }
    }

    public void RejoindreGroupe(Groupe groupe){
        if(this.groupes == null){
            groupes = new ArrayList<>();
        }

        groupes.add(groupe);

        groupe.getEtudiants().add(this);
    }

    public void quitterGroupe(Groupe groupe){

        if(this.groupes != null){
            groupes.remove(groupe);
        }
        if(groupe.getEtudiants() == this){
            groupe.setEtudiants(null);
        }

    }


  public void rejoindreForum(Forum forum){
        if(this.forums == null ){
            this.forums = new ArrayList<>();
        }

        forums.add(forum);

        forum.setEtudiant(this);
  }

  public void quitterForum(Forum forum){
        if(this.forums != null){
            forums.remove(forum);
        }

        if(forum.getEtudiant() == this){
            forum.setEtudiant(null);
        }


  }

  public void ajouterReservation(Reservation reservation){
        if(this.reservations == null){
            this.reservations = new ArrayList<>();
        }

        reservations.add(reservation);
        reservation.setEtudiant(this);

  }
  public void annulerReservation(Reservation reservation){

        if(this.reservations != null){
            reservations.remove(reservation);
        }

        if(reservation.getEtudiant()==this ){
            reservation.setEtudiant(this);
        }
  }


}
