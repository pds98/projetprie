package com.dembaandousmane.gest_priere.reservation.service;


import com.dembaandousmane.gest_priere.etudiant.model.Etudiant;
import com.dembaandousmane.gest_priere.etudiant.service.EtudiantService;
import com.dembaandousmane.gest_priere.priere.model.Priere;
import com.dembaandousmane.gest_priere.priere.service.PriereService;
import com.dembaandousmane.gest_priere.reservation.dto.ReservationResponseDto;
import com.dembaandousmane.gest_priere.reservation.model.Reservation;
import com.dembaandousmane.gest_priere.reservation.repository.ReservationRepository;
import com.dembaandousmane.gest_priere.salle.model.Salle;
import com.dembaandousmane.gest_priere.salle.service.SalleService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

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

    public ReservationResponseDto creerReservation(Reservation reservation, Long etudiantId, Long salleId, Long priereId){


        if(reservation == null){
            throw new RuntimeException("reservation null");
        }

        if(etudiantId == null || salleId == null || priereId == null){
            throw new RuntimeException("les champ id doivent etre obligatoire");
        }

        if(reservation.getDebut() == null || reservation.getFin() ==  null){
            throw new RuntimeException("les champs debut et fin doivent etre obligatoire");
        }

        Etudiant e = etudiantService.trouverEtudiantParId(etudiantId);
        Salle s = salleService.trouverSalleParId(salleId);
        Priere p = priereService.trouverPriereParId(priereId);

        if(!s.estLibre()){
            throw new RuntimeException("la classe est deja reservé.........");
        }

        Reservation r = Reservation.builder().debut(reservation.getDebut())
                .fin(reservation.getFin())
                .motif(reservation.getMotif())
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


    public void annulerReservation(Long idReservation){

        Reservation r = reservationRepository.findById(idReservation).orElseThrow(() -> new RuntimeException("la reservation n'existe pas"));
        r.setEstActif(false);

        Salle s = r.getSalle();
        s.libererSalle();



    }

}
