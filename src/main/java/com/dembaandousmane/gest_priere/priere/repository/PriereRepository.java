package com.dembaandousmane.gest_priere.priere.repository;

import com.dembaandousmane.gest_priere.priere.model.Priere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PriereRepository extends JpaRepository<Priere, Long> {
    List<Priere> findByNomContaining(String nom);
}
