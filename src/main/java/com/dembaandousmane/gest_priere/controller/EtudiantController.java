package com.dembaandousmane.gest_priere.controller;

import com.dembaandousmane.gest_priere.entity.*;
import com.dembaandousmane.gest_priere.repository.*;
import com.dembaandousmane.gest_priere.service.EtudiantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
//@RequestMapping("/api/etudiants")
public class EtudiantController {

    // @Autowired
   // private EtudiantService etudiantService;

    private EtudiantRepository etudiantRepository;
    private EvenementRepository evenementRepository;
    private SalleRepository salleRepository;
    private ForumRepository forumRepository;
    private GroupeRepository groupeRepository;
    private PriereRepository priereRepository;
    private ReservationRepository reservationRepository;

    public EtudiantController(EtudiantRepository etudiantRepository, EvenementRepository evenementRepository, SalleRepository salleRepository, ForumRepository forumRepository, GroupeRepository groupeRepository, PriereRepository priereRepository, ReservationRepository reservationRepository){
        this.etudiantRepository = etudiantRepository;
        this.evenementRepository = evenementRepository;
        this.salleRepository = salleRepository;
        this.forumRepository = forumRepository;
        this.groupeRepository = groupeRepository;
        this.priereRepository = priereRepository;
        this.reservationRepository = reservationRepository;
    }

    @PostMapping("/etudiant")
    public void AjouterEtudiant(@RequestBody Etudiant etudiant){
        LocalDateTime date = LocalDateTime.of(2026, 4, 8, 10, 30);


        Etudiant e = Etudiant.builder()
                .nom(etudiant.getNom())
                .prenom(etudiant.getPrenom())
                .telephone("000")
                .email("ousmanetelly30@gmail.com").build();



        Salle salle = Salle.builder()
                .capacite(12)
                .statut("libre")
                .build();

        Priere priere = Priere.builder()
                .nom("fajr")
                .description("prire du matin")
                .build();

        Reservation reservation = Reservation.builder()
                .debut(date)
                .estActif(true)
                .fin(date)
                .motif("prière de fajr")
                .nombrePersonnes(2)
                .build();

        reservation.setEtudiant(e);
        reservation.setSalle(salle);
        reservation.setPriere(priere);


        e.ajouterReservation(reservation);


        etudiantRepository.save(e);
        salleRepository.save(salle);
        priereRepository.save(priere);
        reservationRepository.save(reservation);



    }
    //@GetMapping
    public void AfficherReservation(){



        for(Reservation r : reservationRepository.findAll()){
            System.out.println(r.toString());
        }
    }

}
