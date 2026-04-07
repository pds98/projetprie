package com.dembaandousmane.gest_priere.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

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


    @OneToMany(mappedBy = "forum")
    private ArrayList<Message> messages;



    public void addMessage(Message message){
        messages.add(message);
    }




}
