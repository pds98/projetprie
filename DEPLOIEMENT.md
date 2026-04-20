# Guide de déploiement — GestPrière Backend

## Pourquoi je ne peux pas déployer pour toi

Le déploiement nécessite **tes comptes personnels** (GitHub, Render, base de données distante). Je n'ai pas accès à ces services depuis cette session, donc tu dois faire ces étapes toi-même. Mais j'ai préparé **tout ce qu'il faut** pour que ça prenne 10 min max.

---

## 1. Ce que j'ai mis en place

### Réorganisation
```
Gest_priere_springboot/
├── backend/                  ← TOUT le code Spring Boot ici
│   ├── src/
│   ├── pom.xml
│   ├── mvnw, mvnw.cmd, .mvn/
│   ├── Dockerfile            ← nouveau, pour le déploiement
│   ├── .dockerignore         ← nouveau
│   └── system.properties     ← nouveau (version Java)
├── GestPriere_App/           ← l'app mobile React Native (inchangée)
└── DEPLOIEMENT.md            ← ce fichier
```

### Fichiers créés pour le déploiement

- **`backend/Dockerfile`** : recette pour fabriquer l'image Docker (Maven build → JAR → image légère)
- **`backend/.dockerignore`** : fichiers à ignorer dans l'image Docker
- **`backend/system.properties`** : précise Java 21
- **`backend/src/main/resources/application.properties`** : modifié pour lire les variables d'environnement (`PORT`, `DB_URL`, `DB_USER`, `DB_PASS`, `DDL_AUTO`)

---

## 2. Déploiement sur Render (recommandé, gratuit)

### Étape 1 — Pousser le code sur GitHub

```bash
cd /chemin/vers/Gest_priere_springboot
git add .
git commit -m "Réorganisation backend + préparation déploiement"
git push
```

Si tu n'as pas encore de repo GitHub :
1. Crée un repo sur https://github.com/new (nom : `gest-priere`)
2. Suis les instructions pour pousser ton code

### Étape 2 — Créer un compte Render

1. Va sur https://render.com
2. Clique "Get Started" → connecte avec ton compte GitHub
3. Autorise Render à voir tes repos

### Étape 3 — Créer le Web Service

1. Sur le dashboard Render → **New +** → **Web Service**
2. Sélectionne ton repo `gest-priere`
3. Configure :
   - **Name** : `gest-priere-api`
   - **Region** : `Frankfurt (EU Central)` (ou plus proche de toi)
   - **Branch** : `main` (ou `master`)
   - **Root Directory** : `backend`
   - **Runtime** : `Docker`
   - **Instance Type** : `Free`
4. Clique **Create Web Service**

Render va lire ton `Dockerfile` automatiquement et builder l'image. Le premier build prend ~5 min.

### Étape 4 — Configurer les variables d'environnement

Sur la page de ton service Render → onglet **Environment** → ajoute :

| Clé | Valeur |
|-----|--------|
| `DB_URL` | `jdbc:mariadb://76.13.121.48:3306/test?allowPublicKeyRetrieval=true&useSSL=false` |
| `DB_USER` | `ousmane` |
| `DB_PASS` | `ousmane` |
| `DDL_AUTO` | `update` |

> ⚠️ Ta base actuelle (`76.13.121.48`) est une IP perso. Si ton PC est éteint ou que cette IP change, ton API ne marchera plus. Voir section 4 pour héberger la bdd.

### Étape 5 — Récupérer ton URL

Une fois le déploiement terminé (status `Live`), tu auras une URL du type :
```
https://gest-priere-api.onrender.com
```

Teste : ouvre `https://gest-priere-api.onrender.com/api/etudiants` dans ton navigateur → tu dois voir une liste JSON (ou `[]` si vide).

### Étape 6 — Mettre à jour l'app mobile

Dans `GestPriere_App/src/api/apiService.js` ligne 3 :

```js
const BASE_URL = 'https://gest-priere-api.onrender.com/api';
```

Plus jamais besoin de ngrok 🎉

---

## 3. Variables d'environnement disponibles

| Variable | Défaut | Utilité |
|---|---|---|
| `PORT` | `8080` | Port d'écoute (Render le force) |
| `DB_URL` | ton serveur perso | URL JDBC de la base |
| `DB_USER` | `ousmane` | Utilisateur bdd |
| `DB_PASS` | `ousmane` | Mot de passe bdd |
| `DDL_AUTO` | `update` | Comportement Hibernate (`none`, `update`, `create`, `create-drop`) |

---

## 4. Héberger aussi la base de données (recommandé)

### Option A — Aiven (MariaDB/MySQL gratuit, simple)
1. https://aiven.io → inscription gratuite
2. Create Service → MySQL → plan Free
3. Récupère l'URL JDBC, le user et le password
4. Mets ces valeurs dans les env vars Render

### Option B — Railway (PostgreSQL ou MySQL)
1. https://railway.app
2. New Project → Provision MySQL
3. Récupère les credentials dans l'onglet Variables

### Option C — Garder ton serveur perso
Tant que ton PC qui héberge MariaDB est allumé et accessible publiquement (`76.13.121.48`), ça marche. Mais c'est fragile.

---

## 5. Alternatives à Render

| Service | Avantage | Inconvénient |
|---|---|---|
| **Render** | Très simple, free tier généreux | Service s'endort après 15 min d'inactivité (premier appel lent ~30s) |
| **Railway** | Pas d'endormissement | 5$ de crédit/mois (suffisant pour un projet étudiant) |
| **Koyeb** | Free tier, pas d'endormissement | Interface un peu moins claire |
| **Fly.io** | Très rapide | Demande la CB pour vérifier (pas facturé) |

Pour tous ces services, le `Dockerfile` que j'ai créé fonctionne pareil.

---

## 6. Tester en local avant de déployer

```bash
cd backend
./mvnw clean package -DskipTests
java -jar target/*.jar
```

Puis ouvre http://localhost:8080/api/etudiants

Ou avec Docker :
```bash
cd backend
docker build -t gest-priere .
docker run -p 8080:8080 gest-priere
```

---

## 7. Endpoints disponibles

| Méthode | URL | Action |
|---|---|---|
| GET | `/api/etudiants` | Liste étudiants |
| POST | `/api/etudiants` | Créer étudiant |
| GET | `/api/reservations` | Liste réservations |
| POST | `/api/reservations` | Créer réservation |
| DELETE | `/api/reservations/{id}` | Supprimer réservation |
| GET | `/api/evenements` | Liste événements |
| POST | `/api/evenements` | Créer événement |
| DELETE | `/api/evenements/{id}` | Supprimer événement |
| GET | `/api/forums` | Liste forums |
| POST | `/api/forums` | Créer forum |
| DELETE | `/api/forums/{id}` | Supprimer forum |
| GET | `/api/salles` | Liste salles |
| GET | `/api/prieres` | Liste prières |

---

## 8. Si ça plante

### Build échoue sur Render
- Regarde les logs (onglet **Logs**)
- Vérifie que `Root Directory` est bien `backend`
- Vérifie que le `Dockerfile` est bien à la racine de `backend/`

### L'API démarre mais retourne 500 sur les requêtes
- Problème de connexion bdd
- Vérifie tes env vars `DB_URL`, `DB_USER`, `DB_PASS`
- Vérifie que ta base est accessible depuis internet (pas juste localhost)

### L'app mobile ne charge pas les données
- Vérifie que tu as bien mis à jour `BASE_URL` dans `apiService.js`
- Teste l'URL dans un navigateur d'abord
- Si Render dort, le premier appel prend 30s → mets un timeout plus long dans axios

---

## Récap rapide

1. `git push` ton code
2. Render → New Web Service → ton repo → Root: `backend`, Runtime: Docker
3. Ajouter les env vars (`DB_URL`, `DB_USER`, `DB_PASS`)
4. Récupérer l'URL `.onrender.com`
5. La coller dans `apiService.js`
6. ✅ Fini
