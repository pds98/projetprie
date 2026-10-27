# Changements Backend — Explications détaillées
## Session du 20 avril 2026

---

## 1. Objet Etudiant partiel dans les controllers (EvenementController, ForumController)

### Le problème

Quand le frontend React Native crée un événement, il envoyait ce JSON :

```json
{
  "nom": "Conférence",
  "lieu": "Salle A",
  "createurEvenement": { "id": 1 }
}
```

Le controller recevait ça avec `@RequestBody Evenement evenement`. Jackson (la librairie qui transforme le JSON en objet Java) lit ce JSON et crée un objet `Evenement`. Pour le champ `createurEvenement`, il crée un objet `Etudiant` avec seulement `id = 1`. Tous les autres champs (`nom`, `prenom`, `email`, `telephone`) restent **null**.

### Ce que Hibernate fait avec ça

Quand on appelle `evenementRepository.save(evenement)`, Hibernate doit lier cet Evenement à un Etudiant. Il regarde l'objet `Etudiant` qu'on lui passe. Cet objet est **transient** — c'est-à-dire qu'il n'a jamais été chargé depuis la base de données, il a été créé directement par Jackson.

Hibernate ne sait pas quoi faire avec un Etudiant transient qui a `id = 1` mais tous les champs null. Deux choses pouvaient se passer :
- Hibernate essayait de faire un INSERT d'un nouvel Etudiant avec des champs null → erreur `@Column(nullable=false)` violée
- Hibernate sauvegardait l'événement sans créateur (null) et aucune erreur n'était visible

Résultat visible : les forums et événements créés n'avaient pas de créateur. Impossible d'afficher "créé par X" ou de savoir qui peut supprimer quoi.

### La solution

**Frontend** : envoyer seulement l'ID, pas un objet imbriqué :
```json
{
  "nom": "Conférence",
  "lieu": "Salle A",
  "idCreateurEvenement": 1
}
```

**Backend** : lire l'ID depuis le Map, charger l'Etudiant complet depuis la base, puis construire l'entité :
```java
// On change @RequestBody Evenement → @RequestBody Map<String, Object>
Long idCreateur = ((Number) body.get("idCreateurEvenement")).longValue();

// On charge l'Etudiant RÉEL depuis la base (objet géré par Hibernate)
Etudiant createur = etudiantRepository.findById(idCreateur).orElse(null);

// On construit l'Evenement avec l'Etudiant complet et géré
Evenement evenement = Evenement.builder()
    .createurEvenement(createur)  // ← Etudiant réel, pas un fantôme
    .build();
```

Avec `findById`, Hibernate charge l'Etudiant complet depuis la base et le place dans sa "session" (contexte de persistance). Quand on sauvegarde l'Evenement, Hibernate connaît cet Etudiant et écrit correctement la clé étrangère en base.

---

## 2. `isEstActif()` introuvable — Lombok et les types Boolean vs boolean

### Le problème

Dans `Evenement.java`, le champ était déclaré comme :
```java
private Boolean estActif = true;  // Boolean avec majuscule = type "boxed"
```

Dans `EvenementService.java` et les DTOs, on appelait :
```java
evenement.isEstActif()  // ← ERREUR : méthode introuvable
```

### Pourquoi Lombok ne génère pas `isEstActif()`

Lombok génère automatiquement des getters selon cette règle stricte :

| Type du champ | Méthode getter générée |
|---|---|
| `boolean` (primitif, minuscule) | `isEstActif()` |
| `Boolean` (boxed, majuscule) | `getEstActif()` |

Cette règle vient de la convention JavaBeans. Un `Boolean` boxed peut être `null` (c'est un objet), donc la convention `is` ne s'applique pas — `isEstActif()` retourne un `boolean` primitif et ne peut pas retourner `null`. Lombok respecte cette convention.

Concrètement : `Evenement.java` avait `Boolean estActif` → Lombok générait `getEstActif()`. Mais `EvenementService` appelait `isEstActif()` → méthode inexistante → erreur de compilation `cannot find symbol`.

### Pourquoi on utilise `Boolean` (boxed) et pas `boolean` (primitif)

On a choisi `Boolean` exprès parce que le frontend React Native peut envoyer `null` pour ce champ. Avec un primitif `boolean`, Jackson lèverait une erreur `Cannot map null into type boolean`. Avec `Boolean` (objet), `null` est une valeur valide.

### La solution

Partout où on appelait `isEstActif()`, remplacer par `getEstActif()`. Et dans les DTOs qui stockaient le résultat, passer de `boolean` à `Boolean` pour rester cohérent.

---

## 3. Boucle infinie JSON — `Forum ↔ Message`

### Le problème

Quand on appelait `GET /api/forums`, le serveur plantait avec :
```
HttpMessageNotWritableException: Document nesting depth (501) exceeds the maximum allowed (500)
```

### Ce qui se passait exactement

Jackson serialise un objet Java en JSON en explorant tous ses champs. Voici ce qui se passait :

```
Jackson sérialise Forum
 → regarde le champ "messages" (List<Message>)
   → sérialise chaque Message
     → regarde le champ "forum" (Forum)
       → sérialise ce Forum
         → regarde le champ "messages" (List<Message>)
           → sérialise chaque Message
             → regarde le champ "forum" ...
               → (continue à l'infini)
```

Jackson s'arrête à 500 niveaux d'imbrication et lève une exception. L'appel API retourne une erreur 500 et le frontend ne peut rien afficher.

### Pourquoi cette relation bidirectionnelle existe

En JPA, quand on a `Forum` avec une liste de `Message` (`@OneToMany`), et `Message` avec un lien vers son `Forum` (`@ManyToOne`), c'est une relation **bidirectionnelle**. Chaque côté connaît l'autre. C'est utile en Java pour naviguer dans les deux sens, mais c'est un piège pour Jackson.

### La solution

Ajouter `@JsonIgnore` sur les champs qui créent le cycle. `@JsonIgnore` dit à Jackson "n'essaie pas de sérialiser ce champ, ignore-le".

**`Forum.java`** — on ignore la liste de messages (on ne veut pas les messages imbriqués dans un forum de toute façon) :
```java
@JsonIgnore   // ← Jackson ne touche pas à ce champ
@OneToMany(mappedBy = "forum")
private List<Message> messages = new ArrayList<>();
```

**`Message.java`** — on ignore le lien retour vers le forum (on ne veut pas le forum entier imbriqué dans chaque message) :
```java
@JsonIgnore   // ← Jackson ne touche pas à ce champ
@ManyToOne
@JoinColumn(name = "id_forum")
private Forum forum;
```

Maintenant la sérialisation s'arrête proprement :
```
Jackson sérialise Forum
 → champ "messages" → @JsonIgnore → STOP, passe au champ suivant
 → champ "etudiant" → sérialise l'Etudiant (pas de boucle)
 → FIN
```

### Règle générale

Dans toute relation bidirectionnelle JPA (`@OneToMany` + `@ManyToOne`, ou `@ManyToMany`), au moins un côté doit avoir `@JsonIgnore` sinon Jackson boucle à l'infini.

---

## 4. ManyToMany — côté propriétaire vs côté inverse (GroupeController)

### Le problème

Quand un étudiant cliquait "Rejoindre" un groupe, rien ne se passait en base. L'UI rechargeait le groupe et l'étudiant n'était toujours pas dans la liste des membres.

### Comment fonctionne `@ManyToMany` en JPA

Une relation ManyToMany entre `Etudiant` et `Groupe` nécessite une **table de jointure** en base (`groupe_membre` avec deux colonnes : `id_etudiant` et `id_groupe`).

En JPA, cette relation a deux côtés :

**Côté propriétaire** (`Etudiant.groupesRejoins`) — c'est celui qui a `@JoinTable` :
```java
@ManyToMany
@JoinTable(
    name = "groupe_membre",
    joinColumns = @JoinColumn(name = "id_etudiant"),
    inverseJoinColumns = @JoinColumn(name = "id_groupe")
)
private List<Groupe> groupesRejoins;
```

**Côté inverse** (`Groupe.membres`) — c'est celui qui a `mappedBy` :
```java
@ManyToMany(mappedBy = "groupesRejoins")
private List<Etudiant> membres = new ArrayList<>();
```

**Règle fondamentale JPA** : quand tu modifies une relation ManyToMany, **seul le côté propriétaire est lu par Hibernate pour écrire en base**. Le côté `mappedBy` est ignoré pour les écritures — il sert uniquement à lire.

### Ce que le code faisait (faux)

```java
// On modifiait le côté INVERSE → Hibernate ignore complètement ça
groupe.getMembres().add(etudiant);
groupeRepository.save(groupe);
// Résultat en base : table groupe_membre → rien de nouveau
```

Hibernate voit que `groupe.membres` est le côté inverse (`mappedBy`). Il ne regarde jamais ce côté pour savoir quoi écrire dans `groupe_membre`. Donc même si on sauvegarde le groupe, aucune ligne n'est insérée dans la table de jointure.

### La solution

Modifier le côté propriétaire (`etudiant.groupesRejoins`) et sauvegarder l'Etudiant :

```java
// On modifie le côté PROPRIÉTAIRE → Hibernate lit ça et écrit en base
etudiant.getGroupesRejoins().add(groupe);
etudiantRepository.save(etudiant);
// Résultat en base : table groupe_membre → INSERT (id_etudiant=X, id_groupe=Y)
```

Hibernate voit que `etudiant.groupesRejoins` est le côté propriétaire (il a `@JoinTable`). Quand on sauvegarde l'Etudiant, Hibernate compare sa liste de groupes avant/après et insère les nouvelles lignes dans `groupe_membre`.

### Pourquoi on a ajouté `@Transactional`

`etudiant.getGroupesRejoins()` est une collection **lazy-loaded**. Ça signifie que Hibernate ne charge pas cette liste depuis la base automatiquement — il attend qu'on y accède pour faire le SELECT. Mais pour faire ce SELECT, Hibernate a besoin d'une **session** (connexion à la base) active.

Sans `@Transactional`, la session Hibernate se ferme dès que `etudiantRepository.findById()` retourne. Quand on appelle ensuite `.getGroupesRejoins()`, la session est déjà fermée → `LazyInitializationException`.

Avec `@Transactional`, la session reste ouverte pendant toute la durée de la méthode du controller. On peut accéder à la collection lazy sans problème.

---

## 5. `application.properties` — `ddl-auto=create` → `update`

### Le problème avec `create`

`spring.jpa.hibernate.ddl-auto=create` dit à Hibernate : **à chaque démarrage du serveur, DROP toutes les tables et les recréer vides**.

C'est utile une fois pour forcer le bon schéma. Mais si on laisse `create` en permanence, toutes les données sont effacées à chaque redémarrage : utilisateurs, réservations, groupes, tout.

### Pourquoi on avait mis `create`

On avait un problème de schéma : les tables existantes (créées par une ancienne version du code) avaient des colonnes avec de mauvais noms. Par exemple, la clé primaire de la table `etudiant` avait un nom différent de ce qu'Hibernate attendait. `ddl-auto=update` ne peut pas renommer des colonnes existantes — il ajoute seulement ce qui manque. La seule solution était `create` pour tout refaire proprement.

### Pourquoi on repasse à `update`

Maintenant que le schéma est correct, `update` est le bon mode : Hibernate compare le schéma actuel en base avec les entités Java, et ajoute seulement les colonnes/tables manquantes sans jamais toucher aux données existantes.

---

## Tableau récapitulatif

| # | Fichier modifié | Problème | Cause technique | Fix |
|---|---|---|---|---|
| 1 | `EvenementController`, `ForumController` | Événements/forums créés sans créateur | Frontend envoyait un objet Etudiant partiel (juste `{id}`) → Hibernate ne savait pas quoi en faire | Envoyer juste `idEtudiant` (Long), charger l'Etudiant complet avec `findById` côté backend |
| 2 | `EvenementService`, `EvenementRequestDto`, `EvenementResponseDto` | Erreur de compilation `cannot find symbol: isEstActif()` | Lombok génère `isXxx()` pour `boolean` primitif seulement. `Boolean` boxed génère `getXxx()` | Remplacer `isEstActif()` par `getEstActif()`, changer `boolean` en `Boolean` dans les DTOs |
| 3 | `Forum.java`, `Message.java` | Erreur 500 "nesting depth 501" sur GET /forums | Relation bidirectionnelle `Forum ↔ Message` sans `@JsonIgnore` → Jackson boucle à l'infini | Ajouter `@JsonIgnore` sur `Forum.messages` et `Message.forum` |
| 4 | `GroupeController.java` | Rejoindre un groupe ne faisait rien en base | Le côté `mappedBy` (inverse) d'une relation ManyToMany est ignoré par Hibernate pour les écritures | Modifier `etudiant.groupesRejoins` (côté propriétaire avec `@JoinTable`) + `@Transactional` pour lazy loading |
| 5 | `application.properties` | Toutes les données effacées à chaque redémarrage | `ddl-auto=create` recrée toutes les tables vides à chaque démarrage | Passer en `ddl-auto=update` une fois le schéma stable |
