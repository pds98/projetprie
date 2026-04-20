package com.dembaandousmane.gest_priere.salle.service;


import com.dembaandousmane.gest_priere.salle.dto.SalleResponseDto;
import com.dembaandousmane.gest_priere.salle.model.Salle;
import com.dembaandousmane.gest_priere.salle.repository.SalleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SalleService {
    private final SalleRepository salleRepository;

    public SalleService(SalleRepository salleRepository) {
        this.salleRepository = salleRepository;
    }


    public Salle trouverSalleParId(Long id ){

        Optional<Salle> s = salleRepository.findById(id);

        return  s.orElseThrow(() -> new RuntimeException("la classe n'existe pas "));



    }

    public SalleResponseDto creerSalle(Salle salle){

        if(salle.getNumeroSalle() == 0 || salle.getCapacite() == 0 || salle.getStatut() == null){
            throw new RuntimeException("les champs numero salle, capacité , status, doivent etre remplie");
        }

        if(salle == null){
            throw new RuntimeException("salle est null");
        }

        Salle s = Salle.builder().NumeroSalle(salle.getNumeroSalle())
                .statut(salle.getStatut())
                .capacite(salle.getCapacite()).build();

        Salle saved = salleRepository.save(s);


        return SalleResponseDto.builder().numeroSalle(saved.getNumeroSalle())
                .status(saved.getStatut())
                .capacite(saved.getCapacite())
                .id(saved.getIdSalle()).build();

    }

    public void supprimerSalle(Long idSalle){
        Salle salle = trouverSalleParId(idSalle);

        if(salle != null){
            salleRepository.delete(salle);
        }



    }

    public void modifierSalleParCapacite(Long idsalle, int nouvelleCapacite){
        Salle salle = trouverSalleParId(idsalle);
        if (idsalle == 0){
            throw new RuntimeException("idsalle est egale a  0");
        }
        if(salle != null){
            salle.setCapacite(nouvelleCapacite);
        }
    }

    public List<Salle> avoirSalleLibre(){

        return salleRepository.findByStatut("libre");

    }
}
