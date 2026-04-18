package com.dembaandousmane.gest_priere.Etudiant.Repository;

import com.dembaandousmane.gest_priere.Etudiant.Model.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {
    Optional<Etudiant> findByEmail(String email);
}
