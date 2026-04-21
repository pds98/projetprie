package com.dembaandousmane.gest_priere.message.controllers;


import com.dembaandousmane.gest_priere.message.dto.MessageRequestDto;
import com.dembaandousmane.gest_priere.message.dto.MessageResponseDto;
import com.dembaandousmane.gest_priere.message.model.Message;
import com.dembaandousmane.gest_priere.message.service.MessageService;
import jakarta.transaction.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/messages/")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public MessageResponseDto creerMessage(@RequestBody MessageRequestDto messageRequestDto){

       return  messageService.creerMessage(messageRequestDto);

    }

    @DeleteMapping("/{messageId}")

    public void supprimerMessage(@PathVariable Long messageId){

        messageService.supprimerMessage(messageId);
    }


   @PutMapping
    public void modifierMessage(){}
}
