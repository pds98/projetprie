package com.dembaandousmane.gest_priere.Evenement.Repository;

import com.dembaandousmane.gest_priere.Evenement.Model.Evenement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EvenementRepository extends JpaRepository<Evenement, Integer> {

}
