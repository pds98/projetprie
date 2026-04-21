package com.dembaandousmane.gest_priere.reservation.service;


import com.dembaandousmane.gest_priere.etudiant.model.Etudiant;
import com.dembaandousmane.gest_priere.etudiant.service.EtudiantService;
import com.dembaandousmane.gest_priere.evenement.model.Evenement;
import com.dembaandousmane.gest_priere.priere.model.Priere;
import com.dembaandousmane.gest_priere.priere.service.PriereService;
import com.dembaandousmane.gest_priere.reservation.controllers.ReservationController;
import com.dembaandousmane.gest_priere.reservation.dto.ReservationRequestDto;
import com.dembaandousmane.gest_priere.reservation.dto.ReservationResponseDto;
import com.dembaandousmane.gest_priere.reservation.model.Reservation;
import com.dembaandousmane.gest_priere.reservation.repository.ReservationRepository;
import com.dembaandousmane.gest_priere.salle.model.Salle;
import com.dembaandousmane.gest_priere.salle.service.SalleService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
public class ReservationService {
    private final EtudiantService etudiantService;
    private final SalleService salleService;
    private final PriereService priereService;
    private final ReservationRepository reservationRepository;

    public ReservationService(EtudiantService etudiantService, SalleService salleService, PriereService priereService, ReservationRepository reservationRepository) {
        this.etudiantService = etudiantService;
        this.salleService = salleService;
        this.priereService = priereService;
        this.reservationRepository = reservationRepository;
    }

    public ReservationResponseDto creerReservation(ReservationRequestDto reservationRequestDto){


        if(reservationRequestDto.getMotif() == null){
            throw new RuntimeException("le champ motif est obligatoire");
        }

        if(reservationRequestDto.getIdEtudiant() == null || reservationRequestDto.getIdPriere() == null || reservationRequestDto.getIdSalle() == null){
            throw new RuntimeException("les champ id doivent etre obligatoire");
        }

        if(reservationRequestDto.getDateDebut() == null || reservationRequestDto.getDateFin() ==  null){
            throw new RuntimeException("les champs debut et fin doivent etre obligatoire");
        }

        Etudiant e = etudiantService.trouverEtudiantParId(reservationRequestDto.getIdEtudiant());
        Salle s = salleService.trouverSalleParId(reservationRequestDto.getIdSalle());
        Priere p = priereService.trouverPriereParId(reservationRequestDto.getIdSalle());


        if(!s.estLibre()){
            throw new RuntimeException("la classe est deja reservé.........");
        }

        Reservation r = Reservation.builder().debut(reservationRequestDto.getDateDebut())
                .fin(reservationRequestDto.getDateFin())
                .motif(reservationRequestDto.getMotif())
                .estActif(true)
                .build();


        e.ajouterReservation(r);
        s.ajouterReservation(r);
        p.ajouterReservation(r);


        Reservation saved  = reservationRepository.save(r);

       return  ReservationResponseDto.builder().id(saved.getId())
               .idEtudiant(saved.getId()).nomEtudiant(saved.getEtudiant().getNom())
               .idSalle(saved.getSalle().getIdSalle())
               .numeroSalle(saved.getSalle().getNumeroSalle())
               .nomPriere(saved.getPriere().getNom())
               .idPriere(saved.getPriere().getId())
               .motif(saved.getMotif())
               .debut(saved.getDebut())
               .fin(saved.getFin())
               .build();

    }

    @Transactional
    public void annulerReservation(Long idReservation){

        Reservation r = reservationRepository.findById(idReservation).orElseThrow(() -> new RuntimeException("la reservation n'existe pas"));
        r.setEstActif(false);

        Salle s = r.getSalle();
        s.libererSalle();



    }

    public List<Reservation> avoirToutLesReservation(){
        return reservationRepository.findByEstActif(true);
    }

}
