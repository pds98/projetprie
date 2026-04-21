package com.dembaandousmane.gest_priere.forum.model;

import com.dembaandousmane.gest_priere.etudiant.model.Etudiant;
import com.dembaandousmane.gest_priere.message.model.Message;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Forum {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String sujet;

    @Column(nullable = false)
    private LocalDateTime dateCreation;

    @Builder.Default
    @OneToMany(mappedBy = "forum")
    private List<Message> messages = new ArrayList<>();


    @ManyToOne
    @JoinColumn(name = "id_etudiant")
    private Etudiant etudiant;


    public void ajouterMessage(Message message){

        if(this.messages == null){
            this.messages = new ArrayList<>();
        }
        messages.add(message);
        message.setForum(this);
    }

    public void supprimerMessage(Message message){
        if(message == null){
            throw new RuntimeException("le message est null");
        }

        if(message.getForum() == this){
            message.setForum(null);
        }

        messages.remove(message);
    }




}
