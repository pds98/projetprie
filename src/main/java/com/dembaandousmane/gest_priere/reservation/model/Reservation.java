package com.dembaandousmane.gest_priere.reservation.model;

import com.dembaandousmane.gest_priere.etudiant.model.Etudiant;
import com.dembaandousmane.gest_priere.priere.model.Priere;
import com.dembaandousmane.gest_priere.salle.model.Salle;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @ManyToOne
    @JoinColumn(name = "id_etudiant")
    private Etudiant etudiant ;

    @ManyToOne
    @JoinColumn(name="id_priere")
    private Priere priere;

    @Column(nullable = false)
    private LocalDateTime debut;

    @Column(nullable = false)
    private LocalDateTime fin;


    @Column(nullable = false)
    private int nombrePersonnes;

    @Column(nullable = false)
    private String motif;

    @Column(nullable = false)
    private boolean estActif;

    @ManyToOne
    @JoinColumn(name = "id_salle")
    private Salle salle ;


}
