package com.dembaandousmane.gest_priere.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "forum")
public class Forum {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdForum")
    private int idForum;

    private String sujet;

    @Column(name = "DateCreation")
    private LocalDateTime dateCreation;

    public Forum() {}

    public Forum(String sujet, LocalDateTime dateCreation) {
        this.sujet = sujet;
        this.dateCreation = dateCreation;
    }

    public int getIdForum() { return idForum; }
    public void setIdForum(int idForum) { this.idForum = idForum; }

    public String getSujet() { return sujet; }
    public void setSujet(String sujet) { this.sujet = sujet; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
}
