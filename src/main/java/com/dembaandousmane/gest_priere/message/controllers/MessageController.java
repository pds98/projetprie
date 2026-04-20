package com.dembaandousmane.gest_priere.message.controllers;

import com.dembaandousmane.gest_priere.message.dto.MessageResponseDto;
import com.dembaandousmane.gest_priere.message.model.Message;
import com.dembaandousmane.gest_priere.message.repository.MessageRepository;
import com.dembaandousmane.gest_priere.message.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;
    private final MessageRepository messageRepository;

    public MessageController(MessageService messageService, MessageRepository messageRepository) {
        this.messageService = messageService;
        this.messageRepository = messageRepository;
    }

    @GetMapping
    public List<Message> getTous() {
        return messageRepository.findAll();
    }

    // POST /api/messages
    // Corps JSON : { "contenu": "...", "idEtudiant": 1, "idForum": 2 }
    @PostMapping
    public ResponseEntity<MessageResponseDto> creerMessage(@RequestBody Map<String, Object> body) {
        String contenu = (String) body.get("contenu");
        Long idEtudiant = body.get("idEtudiant") != null
                ? Long.valueOf(body.get("idEtudiant").toString()) : null;
        Long idForum = body.get("idForum") != null
                ? Long.valueOf(body.get("idForum").toString()) : null;

        if (contenu == null || contenu.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        Message message = Message.builder().contenu(contenu).build();
        MessageResponseDto response = messageService.creerMessage(message, idEtudiant, idForum);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Message> modifierMessage(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        return messageRepository.findById(id).map(message -> {
            if (body.get("contenu") != null) message.setContenu(body.get("contenu"));
            return ResponseEntity.ok(messageRepository.save(message));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerMessage(@PathVariable Long id) {
        if (!messageRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        messageRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
