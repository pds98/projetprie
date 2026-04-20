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
    private Etudiant etudiant;

    @ManyToOne
    @JoinColumn(name = "id_priere")
    private Priere priere;

    @Column(nullable = false)
    private LocalDateTime debut;

    @Column(nullable = false)
    private LocalDateTime fin;

    // Integer (boxed) pour accepter null depuis le frontend sans planter Jackson
    @Column(nullable = false)
    @Builder.Default
    private Integer nombrePersonnes = 0;

    @Column(nullable = false)
    @Builder.Default
    private String motif = "";

    // Boolean (boxed) pour accepter null depuis le frontend sans planter Jackson
    @Column(nullable = false)
    @Builder.Default
    private Boolean estActif = true;

    @ManyToOne
    @JoinColumn(name = "id_salle")
    private Salle salle;
}
