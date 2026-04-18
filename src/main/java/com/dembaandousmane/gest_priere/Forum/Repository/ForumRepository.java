package com.dembaandousmane.gest_priere.Forum.Repository;

import com.dembaandousmane.gest_priere.Forum.Model.Forum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ForumRepository extends JpaRepository<Forum, Integer> {
}
