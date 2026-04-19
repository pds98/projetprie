package com.dembaandousmane.gest_priere.groupe.repository;

import com.dembaandousmane.gest_priere.groupe.model.Groupe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupeRepository extends JpaRepository<Groupe, Integer> {
}
