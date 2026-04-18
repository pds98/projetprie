package com.dembaandousmane.gest_priere.Etudiant.Model;

import com.dembaandousmane.gest_priere.Evenement.Model.Evenement;
import com.dembaandousmane.gest_priere.Forum.Model.Forum;
import com.dembaandousmane.gest_priere.Groupe.Model.Groupe;
import com.dembaandousmane.gest_priere.Message.Model.Message;
import com.dembaandousmane.gest_priere.Reservation.Model.Reservation;
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
            name = "groupe_membre",
            joinColumns = @JoinColumn(name = "id_etudiant"),
            inverseJoinColumns = @JoinColumn(name = "id_groupe" )
    )
    private List <Groupe> groupesRejoins;


    @OneToMany(mappedBy = "createurGroupe")
    private List <Groupe> groupesCrees;



    @ManyToMany
    @JoinTable(name = "participation_evenement",
            joinColumns = @JoinColumn(name = "id_etudiant"),
            inverseJoinColumns = @JoinColumn(name = "id_evenement"))
    private List<Evenement> evenementsParticipe;


    @OneToMany(mappedBy = "createurEvenement")
    private List <Evenement> evenementsCrees;



    @OneToMany(mappedBy = "etudiant")
    private List <Message> messages;






    @OneToMany(mappedBy = "etudiant")
    private List<Forum> forums;




    public void rejoindreEvenement(Evenement evenement){
        if (this.evenementsParticipe == null){
            this.evenementsParticipe = new ArrayList<>();
        }

        evenementsParticipe.add(evenement);


        evenement.getParticipants().add(this);
    }

    public void quitterEvenement(Evenement evenement){

        if(this.evenementsParticipe != null){
            evenementsParticipe.remove(evenement);
        }
        if(evenement.getParticipants()!= null){
        evenement.getParticipants().remove(this);
        }
    }

    public void RejoindreGroupe(Groupe groupe){
        if(this.groupesRejoins == null){
            groupesRejoins = new ArrayList<>();
        }

        groupesRejoins.add(groupe);

        groupe.getMembres().add(this);
    }

    public void quitterGroupe(Groupe groupe){

        if(this.groupesRejoins != null){
            groupesRejoins.remove(groupe);
        }
        if(groupe.getMembres() != null){
            groupe.getMembres().remove(this);
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

public void ajouterMessage(Message message){

        if(this.messages == null){
            this.messages = new ArrayList<>();
        }

        messages.add(message);

        message.setEtudiant(this);
}


public void supprimerMessage(Message message){

        if(message.getEtudiant() == this){
            message.setEtudiant(null);
        }

        messages.remove(message);
}


}
