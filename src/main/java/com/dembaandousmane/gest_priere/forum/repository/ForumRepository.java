package com.dembaandousmane.gest_priere.forum.repository;

import com.dembaandousmane.gest_priere.forum.model.Forum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ForumRepository extends JpaRepository<Forum, Long> {


}
