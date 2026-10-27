package com.dembaandousmane.gest_priere.message.service;


import com.dembaandousmane.gest_priere.etudiant.model.Etudiant;
import com.dembaandousmane.gest_priere.etudiant.service.EtudiantService;
import com.dembaandousmane.gest_priere.forum.model.Forum;
import com.dembaandousmane.gest_priere.forum.service.ForumService;
import com.dembaandousmane.gest_priere.message.dto.MessageRequestDto;
import com.dembaandousmane.gest_priere.message.dto.MessageResponseDto;
import com.dembaandousmane.gest_priere.message.model.Message;
import com.dembaandousmane.gest_priere.message.repository.MessageRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

    public MessageResponseDto creerMessage(MessageRequestDto request){

        if(request.getContenu() == null){
            throw new RuntimeException("le champ du contenu est obligatoire");
        }

        if(request.getIdEtudiant() == null){
            throw new RuntimeException("le champ de l'id etudiant est obligatoire");
        }

        if(request.getIdForum() == null){
            throw new RuntimeException("le champ id forum est obligatoire");
        }

        Forum forum = forumService.trouverForumById(request.getIdForum());
        Etudiant etudiant = etudiantService.trouverEtudiantParId(request.getIdEtudiant());

        Message message = Message.builder().contenu(request.getContenu())
                .dateCreation(LocalDateTime.now())
                .build();


        etudiant.ajouterMessage(message);
        forum.ajouterMessage(message);

        Message saved =   messageRepository.save(message);

        MessageResponseDto responseDto = MessageResponseDto.builder()
                .id(saved.getId())
                .contenu(saved.getContenu())
                .dateCreation(saved.getDateCreation())
                .forumId(request.getIdForum())
                .etudiantId(request.getIdEtudiant())
                .build();


        return responseDto;






    }

    public java.util.List<MessageResponseDto> getMessagesByForum(Long forumId) {
        java.util.List<Message> messages = forumId != null
                ? messageRepository.findByForumId(forumId)
                : messageRepository.findAll();
        return messages.stream().map(m -> MessageResponseDto.builder()
                .id(m.getId())
                .contenu(m.getContenu())
                .dateCreation(m.getDateCreation())
                .forumId(m.getForum() != null ? m.getForum().getId() : null)
                .etudiantId(m.getEtudiant() != null ? m.getEtudiant().getId() : null)
                .build()
        ).collect(java.util.stream.Collectors.toList());
    }

    @Transactional
    public void supprimerMessage(Long messageId){



        Message message = messageRepository.findById(messageId).orElseThrow(() -> new RuntimeException("le message n'existe pas"));

        Etudiant etudiant = message.getEtudiant();
        Forum forum = message.getForum();

         if(etudiant != null){
             etudiant.supprimerMessage(message);
         }

         if(forum != null){
             forum.supprimerMessage(message);
         }


        messageRepository.delete(message);

    }

}
