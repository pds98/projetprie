package com.dembaandousmane.gest_priere.evenement.model;

import com.dembaandousmane.gest_priere.etudiant.model.Etudiant;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    // Boolean (boxed) pour accepter null depuis le frontend sans planter Jackson
    @Column(nullable = false)
    @Builder.Default
    private Boolean estActif = true;

    @Column
    private String statut;

    @JsonIgnore
    @Builder.Default
    @ManyToMany(mappedBy = "evenementsParticipe")
    private List<Etudiant> participants = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "id_etudiant")
    private Etudiant createurEvenement;

    // Expose uniquement les IDs des participants (evite boucle infinie)
    public List<Long> getParticipantIds() {
        if (participants == null) return Collections.emptyList();
        return participants.stream()
                .map(Etudiant::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public void ajouterParticipant(Etudiant etudiant) {
        if (this.participants == null) this.participants = new ArrayList<>();
        participants.add(etudiant);
        etudiant.getEvenementsParticipe().add(this);
    }

    public void supprimerParticipant(Etudiant etudiant) {
        if (this.participants != null) participants.remove(etudiant);
        if (etudiant.getEvenementsParticipe() != null) etudiant.getEvenementsParticipe().remove(this);
    }
}
