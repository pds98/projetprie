package com.dembaandousmane.gest_priere.message.service;


import com.dembaandousmane.gest_priere.etudiant.model.Etudiant;
import com.dembaandousmane.gest_priere.etudiant.service.EtudiantService;
import com.dembaandousmane.gest_priere.forum.model.Forum;
import com.dembaandousmane.gest_priere.forum.service.ForumService;
import com.dembaandousmane.gest_priere.message.dto.MessageResponseDto;
import com.dembaandousmane.gest_priere.message.model.Message;
import com.dembaandousmane.gest_priere.message.repository.MessageRepository;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    private final ForumService forumService;
    private final EtudiantService etudiantService;
    private final MessageRepository messageRepository;

    public MessageService(ForumService forumService, EtudiantService etudiantService, MessageRepository messageRepository) {
        this.forumService = forumService;

        this.etudiantService = etudiantService;
        this.messageRepository = messageRepository;
    }

    public MessageResponseDto creerMessage(Message message , Long idEtudiant,  Long idForum){


        if(message == null){
            throw new RuntimeException("message null");
        }

        if(message.getContenu() == null || message.getContenu().isBlank()){
            throw new RuntimeException("contenu obligatoire");
        }

        if(idEtudiant == null){
            throw new RuntimeException("idEtudiant obligatoire");
        }

        if(idForum == null){
            throw new RuntimeException("idForum obligatoire");
        }

        Forum forum = forumService.trouverForumById(idForum);
        Etudiant etudiant = etudiantService.trouverEtudiantParId(idEtudiant);

        Message message1 = Message.builder().contenu(message.getContenu())
                .dateCreation(message.getDateCreation()).build();


        etudiant.ajouterMessage(message1);
        forum.ajouterMessage(message1);

       Message saved =   messageRepository.save(message1);

        MessageResponseDto responseDto = MessageResponseDto.builder()
                .id(saved.getId()).contenu(saved.getContenu())
                .dateCreation(saved.getDateCreation())
                .build();


        return responseDto;






    }

}
