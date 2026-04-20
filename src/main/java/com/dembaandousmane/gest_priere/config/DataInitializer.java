package com.dembaandousmane.gest_priere.config;

import com.dembaandousmane.gest_priere.priere.model.Priere;
import com.dembaandousmane.gest_priere.priere.repository.PriereRepository;
import com.dembaandousmane.gest_priere.salle.model.Salle;
import com.dembaandousmane.gest_priere.salle.repository.SalleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final PriereRepository priereRepository;
    private final SalleRepository  salleRepository;

    public DataInitializer(PriereRepository priereRepository, SalleRepository salleRepository) {
        this.priereRepository = priereRepository;
        this.salleRepository  = salleRepository;
    }

    @Override
    public void run(String... args) {
        initialiserPrieres();
        initialiserSalles();
    }

    // ----------------------------------------------------------------
    //  PRIERES
    // ----------------------------------------------------------------
    private void initialiserPrieres() {
        if (priereRepository.count() > 0) return; // deja charge

        List<Priere> prieres = List.of(
            Priere.builder()
                .nom("Fajr")
                .description("Priere de l'aube")
                .heureDebut("05:00")
                .heureFin("06:00")
                .statut("actif")
                .build(),
            Priere.builder()
                .nom("Dhuhr")
                .description("Priere de midi")
                .heureDebut("13:00")
                .heureFin("14:00")
                .statut("actif")
                .build(),
            Priere.builder()
                .nom("Asr")
                .description("Priere de l'apres-midi")
                .heureDebut("16:30")
                .heureFin("17:30")
                .statut("actif")
                .build(),
            Priere.builder()
                .nom("Maghrib")
                .description("Priere du coucher du soleil")
                .heureDebut("19:30")
                .heureFin("20:00")
                .statut("actif")
                .build(),
            Priere.builder()
                .nom("Isha")
                .description("Priere du soir")
                .heureDebut("21:00")
                .heureFin("22:00")
                .statut("actif")
                .build()
        );

        priereRepository.saveAll(prieres);
        System.out.println(">>> [DataInitializer] " + prieres.size() + " prieres inserees.");
    }

    // ----------------------------------------------------------------
    //  SALLES
    // ----------------------------------------------------------------
    private void initialiserSalles() {
        if (salleRepository.count() > 0) return; // deja charge

        List<Salle> salles = List.of(
            Salle.builder().NumeroSalle(1).capacite(30).statut("libre").build(),
            Salle.builder().NumeroSalle(2).capacite(50).statut("libre").build(),
            Salle.builder().NumeroSalle(3).capacite(20).statut("libre").build(),
            Salle.builder().NumeroSalle(4).capacite(100).statut("libre").build(),
            Salle.builder().NumeroSalle(5).capacite(15).statut("libre").build()
        );

        salleRepository.saveAll(salles);
        System.out.println(">>> [DataInitializer] " + salles.size() + " salles inserees.");
    }
}
