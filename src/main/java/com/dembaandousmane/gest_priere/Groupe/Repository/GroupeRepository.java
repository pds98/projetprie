package com.dembaandousmane.gest_priere.Groupe.Repository;

import com.dembaandousmane.gest_priere.Groupe.Model.Groupe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupeRepository extends JpaRepository<Groupe, Integer> {
}
