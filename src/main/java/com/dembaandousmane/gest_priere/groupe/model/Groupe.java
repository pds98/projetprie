package com.dembaandousmane.gest_priere.groupe.model;

import com.dembaandousmane.gest_priere.etudiant.model.Etudiant;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

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
public class Groupe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String description;

    @JsonIgnore
    @Builder.Default
    @ManyToMany(mappedBy = "groupesRejoins")
    private List<Etudiant> membres = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "id_etudiant")
    private Etudiant createurGroupe;

    // Expose uniquement les IDs des membres (evite boucle infinie)
    public List<Long> getMembreIds() {
        if (membres == null) return Collections.emptyList();
        return membres.stream()
                .map(Etudiant::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public void ajouterMembre(Etudiant etudiant) {
        if (this.membres == null) this.membres = new ArrayList<>();
        membres.add(etudiant);
        etudiant.getGroupesRejoins().add(this);
    }

    public void supprimerMembre(Etudiant etudiant) {
        if (this.membres != null) membres.remove(etudiant);
        if (etudiant.getGroupesRejoins() != null) etudiant.getGroupesRejoins().remove(this);
    }
}
