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

        if(reservationRequestDto.getIdEtudiant() == null || reservationRequestDto.getIdSalle() == null){
            throw new RuntimeException("les champ id doivent etre obligatoire");
        }

        if(reservationRequestDto.getDebut() == null || reservationRequestDto.getFin() == null){
            throw new RuntimeException("les champs debut et fin doivent etre obligatoire");
        }

        Etudiant e = etudiantService.trouverEtudiantParId(reservationRequestDto.getIdEtudiant());
        Salle s = salleService.trouverSalleParId(reservationRequestDto.getIdSalle());

        if(!s.estLibre()){
            throw new RuntimeException("la salle est deja reservée");
        }

        int nbPersonnes = reservationRequestDto.getNombrePersonnes() != null
                ? reservationRequestDto.getNombrePersonnes() : 1;

        Reservation r = Reservation.builder()
                .debut(reservationRequestDto.getDebut())
                .fin(reservationRequestDto.getFin())
                .motif(reservationRequestDto.getMotif() != null ? reservationRequestDto.getMotif() : "")
                .nombrePersonnes(nbPersonnes)
                .estActif(true)
                .build();

        e.ajouterReservation(r);
        s.ajouterReservation(r);

        if(reservationRequestDto.getIdPriere() != null){
            Priere p = priereService.trouverPriereParId(reservationRequestDto.getIdPriere());
            p.ajouterReservation(r);
        }


        Reservation saved  = reservationRepository.save(r);

       return  ReservationResponseDto.builder().id(saved.getId())
               .idEtudiant(saved.getEtudiant().getId()).nomEtudiant(saved.getEtudiant().getNom())
               .idSalle(saved.getSalle().getIdSalle())
               .numeroSalle(saved.getSalle().getNumeroSalle())
               .nomPriere(saved.getPriere() != null ? saved.getPriere().getNom() : null)
               .idPriere(saved.getPriere() != null ? saved.getPriere().getId() : null)
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

    @Transactional
    public void modifierReservation(Long id, ReservationRequestDto dto){
        Reservation r = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));
        if(dto.getMotif() != null) r.setMotif(dto.getMotif());
        if(dto.getNombrePersonnes() != null) r.setNombrePersonnes(dto.getNombrePersonnes());
    }

    public List<Reservation> avoirToutLesReservation(){
        return reservationRepository.findByEstActif(true);
    }

}
