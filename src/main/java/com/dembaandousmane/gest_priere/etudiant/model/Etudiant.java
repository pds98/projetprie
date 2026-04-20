package com.dembaandousmane.gest_priere.etudiant.model;

import com.dembaandousmane.gest_priere.evenement.model.Evenement;
import com.dembaandousmane.gest_priere.forum.model.Forum;
import com.dembaandousmane.gest_priere.groupe.model.Groupe;
import com.dembaandousmane.gest_priere.message.model.Message;
import com.dembaandousmane.gest_priere.reservation.model.Reservation;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Etudiant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column(nullable = false, unique = true)
    private String telephone;

    @Column(nullable = false, unique = true)
    private String email;

    // WRITE_ONLY : Jackson peut LIRE ce champ depuis le JSON entrant (inscription)
    // mais ne l'expose JAMAIS dans les reponses JSON
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column
    private String motDePasse;

    @JsonIgnore
    @OneToMany(mappedBy = "etudiant")
    private List<Reservation> reservations;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "groupe_membre",
            joinColumns = @JoinColumn(name = "id_etudiant"),
            inverseJoinColumns = @JoinColumn(name = "id_groupe")
    )
    private List<Groupe> groupesRejoins;

    @JsonIgnore
    @OneToMany(mappedBy = "createurGroupe")
    private List<Groupe> groupesCrees;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "participation_evenement",
            joinColumns = @JoinColumn(name = "id_etudiant"),
            inverseJoinColumns = @JoinColumn(name = "id_evenement"))
    private List<Evenement> evenementsParticipe = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "createurEvenement")
    private List<Evenement> evenementsCrees = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "etudiant")
    private List<Message> messages = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "etudiant")
    private List<Forum> forums;

    // Expose uniquement les IDs des groupes rejoints (evite la boucle infinie)
    public List<Long> getGroupeIds() {
        if (groupesRejoins == null) return Collections.emptyList();
        return groupesRejoins.stream()
                .map(Groupe::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    // Expose uniquement les IDs des evenements
    public List<Long> getEvenementIds() {
        if (evenementsParticipe == null) return Collections.emptyList();
        return evenementsParticipe.stream()
                .map(Evenement::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public void rejoindreEvenement(Evenement evenement) {
        if (this.evenementsParticipe == null) this.evenementsParticipe = new ArrayList<>();
        evenementsParticipe.add(evenement);
        evenement.getParticipants().add(this);
    }

    public void quitterEvenement(Evenement evenement) {
        if (this.evenementsParticipe != null) evenementsParticipe.remove(evenement);
        if (evenement.getParticipants() != null) evenement.getParticipants().remove(this);
    }

    public void RejoindreGroupe(Groupe groupe) {
        if (this.groupesRejoins == null) groupesRejoins = new ArrayList<>();
        groupesRejoins.add(groupe);
        groupe.getMembres().add(this);
    }

    public void quitterGroupe(Groupe groupe) {
        if (this.groupesRejoins != null) groupesRejoins.remove(groupe);
        if (groupe.getMembres() != null) groupe.getMembres().remove(this);
    }

    public void rejoindreForum(Forum forum) {
        if (this.forums == null) this.forums = new ArrayList<>();
        forums.add(forum);
        forum.setEtudiant(this);
    }

    public void quitterForum(Forum forum) {
        if (this.forums != null) forums.remove(forum);
        if (forum.getEtudiant() == this) forum.setEtudiant(null);
    }

    public void ajouterReservation(Reservation reservation) {
        if (this.reservations == null) this.reservations = new ArrayList<>();
        reservations.add(reservation);
        reservation.setEtudiant(this);
    }

    public void annulerReservation(Reservation reservation) {
        if (this.reservations != null) reservations.remove(reservation);
        if (reservation.getEtudiant() == this) reservation.setEtudiant(null);
    }

    public void ajouterMessage(Message message) {
        if (this.messages == null) this.messages = new ArrayList<>();
        messages.add(message);
        message.setEtudiant(this);
    }

    public void supprimerMessage(Message message) {
        if (message.getEtudiant() == this) message.setEtudiant(null);
        messages.remove(message);
    }
}
