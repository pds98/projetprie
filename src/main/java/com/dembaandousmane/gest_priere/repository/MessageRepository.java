package com.dembaandousmane.gest_priere.repository;

import com.dembaandousmane.gest_priere.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
}
