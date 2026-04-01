package com.dembaandousmane.gest_priere.repository;

import com.dembaandousmane.gest_priere.entity.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EtudiantRepository extends JpaRepository<Etudiant, Integer> {
    Optional<Etudiant> findByEmail(String email);
}
