package com.dembaandousmane.gest_priere.salle.model;

import com.dembaandousmane.gest_priere.reservation.model.Reservation;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private Long idSalle;

    @Column(nullable = false)
    private int capacite;

    @Column(nullable = false)
    private String statut;

    @Column(nullable = false)
    private int NumeroSalle;


    @OneToMany(mappedBy = "salle")
    private  List<Reservation> reservations = new ArrayList<>();

   public boolean estLibre(){
       return Objects.equals(this.statut, "libre");
   }

   public void libererSalle(){
       this.statut = "libre";
   }


   public void ajouterReservation(Reservation r){

       if(this.reservations == null){
           this.reservations = new ArrayList<>();
       }
       r.setSalle(this);
   }

   public void supprimerReservation(Reservation r){

       if(r.getSalle() == this){
           r.setSalle(null);
       }

       reservations.remove(r);
   }
}
