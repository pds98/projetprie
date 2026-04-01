package com.dembaandousmane.gest_priere.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdMessage")
    private int idMessage;

    private String contenu;

    @Column(name = "DateCreation")
    private LocalDateTime dateCreation;

    public Message() {}

    public Message(String contenu, LocalDateTime dateCreation) {
        this.contenu = contenu;
        this.dateCreation = dateCreation;
    }

    public int getIdMessage() { return idMessage; }
    public void setIdMessage(int idMessage) { this.idMessage = idMessage; }

    public String getContenu() { return contenu; }
    public void setContenu(String contenu) { this.contenu = contenu; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
}
