package com.dembaandousmane.gest_priere.message.repository;

import com.dembaandousmane.gest_priere.message.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
}
