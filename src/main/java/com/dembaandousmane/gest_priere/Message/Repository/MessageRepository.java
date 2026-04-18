package com.dembaandousmane.gest_priere.Message.Repository;

import com.dembaandousmane.gest_priere.Message.Model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
}
