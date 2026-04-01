package com.dembaandousmane.gest_priere.repository;

import com.dembaandousmane.gest_priere.entity.Evenement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvenementRepository extends JpaRepository<Evenement, Integer> {
    List<Evenement> findByStatut(String statut);
    List<Evenement> findByLieu(String lieu);
}
