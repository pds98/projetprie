package com.dembaandousmane.gest_priere.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String contenu;

    @Column(name = "DateCreation")
    private LocalDateTime dateCreation;

    @ManyToOne
    @JoinColumn(name ="id_forum")
    private Forum forum;


    @ManyToOne
    @JoinColumn(name = "id_etudiant")
    private Etudiant etudiant;


}
