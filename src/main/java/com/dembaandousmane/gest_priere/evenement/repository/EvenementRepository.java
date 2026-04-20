package com.dembaandousmane.gest_priere.evenement.repository;

import com.dembaandousmane.gest_priere.evenement.model.Evenement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EvenementRepository extends JpaRepository<Evenement, Long> {

}
