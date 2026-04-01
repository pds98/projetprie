package com.dembaandousmane.gest_priere.service;

import com.dembaandousmane.gest_priere.entity.Message;
import com.dembaandousmane.gest_priere.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public Message ajouterMessage(Message m) {
        if (m.getContenu() == null || m.getContenu().isEmpty()) {
            throw new IllegalArgumentException("Le contenu du message est obligatoire.");
        }
        return messageRepository.save(m);
    }

    public Optional<Message> obtenirMessage(int id) {
        return messageRepository.findById(id);
    }

    public List<Message> obtenirTousLesMessages() {
        return messageRepository.findAll();
    }

    public Message modifierMessage(int id, Message m) {
        if (!messageRepository.existsById(id)) {
            throw new IllegalArgumentException("Message introuvable (ID: " + id + ").");
        }
        m.setIdMessage(id);
        return messageRepository.save(m);
    }

    public void supprimerMessage(int id) {
        if (!messageRepository.existsById(id)) {
            throw new IllegalArgumentException("Message introuvable (ID: " + id + ").");
        }
        messageRepository.deleteById(id);
    }
}
