package Modele;

import java.time.LocalDateTime;

public class Message {

    private int IdMessage;
    private String contenu ;
    private LocalDateTime DateCreation;

    public Message(String contenu, LocalDateTime dateCreation) {
        this.contenu = contenu;
        DateCreation = dateCreation;
    }


    public int getIdMessage() {
        return IdMessage;
    }

    public void setIdMessage(int idMessage) {
        IdMessage = idMessage;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public LocalDateTime getDateCreation() {
        return DateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        DateCreation = dateCreation;
    }

    public void AfficherInfo(){

    }
}
