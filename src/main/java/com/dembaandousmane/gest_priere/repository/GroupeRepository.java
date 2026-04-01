package com.dembaandousmane.gest_priere.repository;

import com.dembaandousmane.gest_priere.entity.Groupe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupeRepository extends JpaRepository<Groupe, Integer> {
}
