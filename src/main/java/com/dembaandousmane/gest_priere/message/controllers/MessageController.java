package com.dembaandousmane.gest_priere.message.controllers;


import com.dembaandousmane.gest_priere.message.dto.MessageResponseDto;
import com.dembaandousmane.gest_priere.message.model.Message;
import com.dembaandousmane.gest_priere.message.service.MessageService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/messages/")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public MessageResponseDto creerMessage(@RequestBody Message message, @RequestParam Long idEtudiant, @RequestParam Long idForum){

       return  messageService.creerMessage(message,idEtudiant, idForum);

    }

    @DeleteMapping
    public void supprimerMessage(){}


   @PutMapping
    public void modifierMessage(){}
}
