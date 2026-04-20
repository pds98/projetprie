package com.dembaandousmane.gest_priere.salle.repository;

import com.dembaandousmane.gest_priere.salle.model.Salle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalleRepository extends JpaRepository<Salle, Long> {
    List<Salle> findByStatut(String statut);

    //public List<Salle> findByStatus(String status);
}
