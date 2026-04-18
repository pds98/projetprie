package com.dembaandousmane.gest_priere.Priere.Repository;

import com.dembaandousmane.gest_priere.Priere.Model.Priere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PriereRepository extends JpaRepository<Priere, Integer> {
    List<Priere> findByNomContaining(String nom);
}
