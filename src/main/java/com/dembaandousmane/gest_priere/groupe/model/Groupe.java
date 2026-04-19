package com.dembaandousmane.gest_priere.groupe.model;

import com.dembaandousmane.gest_priere.etudiant.model.Etudiant;
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
public class Groupe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String description;

    @Builder.Default
    @ManyToMany(mappedBy = "groupesRejoins")
    private List<Etudiant> membres = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "id_etudiant")
    private Etudiant createurGroupe;


    public void ajouterMember(Etudiant etudiant){
        if(this.membres == null){
            this.membres = new ArrayList<>();
        }

        membres.add(etudiant);
        etudiant.getGroupesCrees().add(this);
    }

  public void supprimerMembre(Etudiant etudiant){
        if(etudiant.getGroupesCrees() == this){
            etudiant.getGroupesCrees().remove(this);
        }

        membres.remove(etudiant);
  }


}
