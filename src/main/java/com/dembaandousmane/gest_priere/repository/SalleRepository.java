package com.dembaandousmane.gest_priere.repository;

import com.dembaandousmane.gest_priere.entity.Salle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalleRepository extends JpaRepository<Salle, Integer> {
    List<Salle> findByStatut(String statut);
}
