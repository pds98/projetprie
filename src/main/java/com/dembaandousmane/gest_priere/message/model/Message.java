package com.dembaandousmane.gest_priere.message.model;

import com.dembaandousmane.gest_priere.etudiant.model.Etudiant;
import com.dembaandousmane.gest_priere.forum.model.Forum;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Long id;

    @Column(nullable = false)
    private String contenu;

    @Column(name = "DateCreation")
    private LocalDateTime dateCreation;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name ="id_forum")
    private Forum forum;


    @ManyToOne
    @JoinColumn(name = "id_etudiant")
    private Etudiant etudiant;


}
