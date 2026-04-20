// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
//  LoginScreen.js — Page de connexion
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

// React = la bibliothèque de base pour créer des composants
// useState = un "tiroir" pour stocker une valeur qui peut changer
import React, { useState } from 'react';

// Composants visuels fournis par React Native
import {
  View,               // boîte invisible pour grouper d'autres éléments
  Text,               // afficher du texte
  TextInput,          // champ de saisie
  TouchableOpacity,   // bouton cliquable
  StyleSheet,         // pour écrire les styles CSS-like
  ActivityIndicator,  // le spinner de chargement (rond qui tourne)
  KeyboardAvoidingView, // fait remonter la page quand le clavier s'ouvre
  Platform,           // détecte si on est sur iPhone ou Android
  ScrollView,         // permet de scroller si la page est trop longue
  Alert,              // affiche une boîte de dialogue (popup)
} from 'react-native';

import { Ionicons } from '@expo/vector-icons'; // icônes (email, cadenas, etc.)
import { authAPI } from '../../api/apiService'; // notre pont vers le backend
import colors from '../../theme/colors'; // nos couleurs définies dans theme/colors.js


// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
//  Le composant LoginScreen
//  - navigation : objet pour aller vers d'autres pages
//  - onLogin    : fonction appelée quand la connexion réussit
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
export default function LoginScreen({ navigation, onLogin }) {

  // useState('') = tiroir avec valeur initiale vide ''
  // email = valeur actuelle, setEmail = fonction pour changer la valeur
  const [email, setEmail]           = useState('');
  const [motDePasse, setMotDePasse] = useState('');

  // true/false pour afficher ou cacher le mot de passe
  const [afficherMdp, setAfficherMdp] = useState(false);

  // true/false pour afficher le spinner de chargement
  const [chargement, setChargement]   = useState(false);


  // ─── Fonction appelée quand on clique sur "Se connecter" ───
  // "async" = cette fonction fait des appels réseau (elle doit "attendre")
  const seConnecter = async () => {

    // Vérification : les champs sont-ils remplis ?
    if (!email.trim()) {
      Alert.alert('Champ vide', 'Veuillez entrer votre email.');
      return;
    }
    if (!motDePasse.trim()) {
      Alert.alert('Champ vide', 'Veuillez entrer votre mot de passe.');
      return;
    }

    setChargement(true); // Active le spinner

    try {
      // authAPI.login() envoie POST /api/auth/login avec email + motDePasse
      // Le backend vérifie les deux et retourne l'étudiant si c'est correct
      const reponse = await authAPI.login(email.trim().toLowerCase(), motDePasse);

      // Connexion réussie ! On appelle onLogin avec les infos de l'étudiant
      // AppNavigator va alors basculer vers le menu principal
      onLogin(reponse.data);

    } catch (erreur) {
      // Le backend retourne 401 si email introuvable ou mot de passe incorrect
      const status  = erreur.response?.status;
      const message = erreur.response?.data?.message;

      if (status === 401 && message) {
        // Message précis venant du backend ("Aucun compte avec cet email." ou "Mot de passe incorrect.")
        Alert.alert('Connexion refusée', message);
      } else {
        // Autre erreur (pas de réseau, serveur éteint…)
        Alert.alert('Erreur réseau', 'Impossible de joindre le serveur. Vérifiez votre WiFi.');
      }

    } finally {
      // "finally" s'exécute TOUJOURS, qu'il y ait une erreur ou non
      setChargement(false); // Désactive le spinner
    }
  };


  // ─── Ce que l'écran affiche ───────────────────────────────
  return (
    // KeyboardAvoidingView : remonte le contenu quand le clavier iPhone apparaît
    <KeyboardAvoidingView
      behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
      style={styles.page}
    >

      {/* ScrollView : permet de scroller sur les petits écrans */}
      <ScrollView contentContainerStyle={styles.scroll} keyboardShouldPersistTaps="handled">

        {/* ── EN-TÊTE VERT ── */}
        <View style={styles.entete}>
          <View style={styles.cercleLune}>
            <Ionicons name="moon" size={40} color="#fff" />
          </View>
          <Text style={styles.titreApp}>GestPrière</Text>
          <Text style={styles.sousTitre}>Bienvenue sur votre espace</Text>
        </View>

        {/* ── CARTE BLANCHE (formulaire) ── */}
        <View style={styles.carte}>
          <Text style={styles.titreCarte}>Connexion</Text>

          {/* Champ Email */}
          <View style={styles.groupeChamp}>
            <Text style={styles.etiquette}>Email</Text>
            <View style={styles.contourChamp}>
              <Ionicons name="mail-outline" size={18} color={colors.textSecondary} style={styles.iconeChamp} />
              <TextInput
                style={styles.champ}
                placeholder="votre@email.com"
                placeholderTextColor={colors.textSecondary}
                keyboardType="email-address" // clavier avec @ directement accessible
                autoCapitalize="none"        // pas de majuscule automatique
                value={email}                // valeur liée au tiroir "email"
                onChangeText={setEmail}      // met à jour le tiroir à chaque frappe
              />
            </View>
          </View>

          {/* Champ Mot de passe */}
          <View style={styles.groupeChamp}>
            <Text style={styles.etiquette}>Mot de passe</Text>
            <View style={styles.contourChamp}>
              <Ionicons name="lock-closed-outline" size={18} color={colors.textSecondary} style={styles.iconeChamp} />
              <TextInput
                style={styles.champ}
                placeholder="••••••••"
                placeholderTextColor={colors.textSecondary}
                secureTextEntry={!afficherMdp} // true = texte caché, false = visible
                value={motDePasse}
                onChangeText={setMotDePasse}
              />
              {/* Bouton œil pour montrer/cacher le mot de passe */}
              <TouchableOpacity onPress={() => setAfficherMdp(!afficherMdp)}>
                <Ionicons
                  name={afficherMdp ? 'eye-off-outline' : 'eye-outline'}
                  size={18}
                  color={colors.textSecondary}
                />
              </TouchableOpacity>
            </View>
          </View>

          {/* Bouton Se connecter */}
          {/* [styles.bouton, chargement && styles.boutonDesactive] = si chargement=true, on ajoute le style grisé */}
          <TouchableOpacity
            style={[styles.bouton, chargement && styles.boutonDesactive]}
            onPress={seConnecter}
            disabled={chargement} // désactive le bouton pendant le chargement
          >
            {/* Condition : si chargement → spinner, sinon → texte */}
            {chargement
              ? <ActivityIndicator color="#fff" />
              : <Text style={styles.texteBouton}>Se connecter</Text>
            }
          </TouchableOpacity>

          {/* Lien vers la page d'inscription */}
          <TouchableOpacity
            style={styles.lienInscription}
            onPress={() => navigation.navigate('Register')} // navigue vers RegisterScreen
          >
            <Text style={styles.texteLien}>
              Pas encore de compte ?{' '}
              <Text style={styles.texteLienGras}>S'inscrire</Text>
            </Text>
          </TouchableOpacity>

        </View>
      </ScrollView>
    </KeyboardAvoidingView>
  );
}


// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
//  STYLES — équivalent du CSS pour React Native
//  Les noms comme "page", "carte" sont inventés par nous
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
const styles = StyleSheet.create({

  // La page entière
  page: {
    flex: 1,                          // prend tout l'espace disponible
    backgroundColor: colors.background,
  },

  // Le contenu scrollable
  scroll: {
    flexGrow: 1,
  },

  // Bloc vert en haut
  entete: {
    backgroundColor: colors.primary,
    paddingTop: 80,
    paddingBottom: 50,
    alignItems: 'center',             // centrer horizontalement
  },

  // Cercle autour de la lune
  cercleLune: {
    width: 80,
    height: 80,
    borderRadius: 40,                 // border-radius = 40 → cercle parfait
    backgroundColor: 'rgba(255,255,255,0.15)',
    justifyContent: 'center',
    alignItems: 'center',
    marginBottom: 12,
  },

  titreApp: {
    fontSize: 28,
    fontWeight: 'bold',
    color: '#fff',
    letterSpacing: 1,
  },

  sousTitre: {
    fontSize: 14,
    color: 'rgba(255,255,255,0.75)',
    marginTop: 4,
  },

  // La carte blanche avec le formulaire
  carte: {
    backgroundColor: colors.surface,
    borderRadius: 24,
    margin: 20,
    marginTop: -24,                   // remonte la carte par-dessus le vert
    padding: 24,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.1,
    shadowRadius: 12,
    elevation: 6,                     // ombre sur Android
  },

  titreCarte: {
    fontSize: 20,
    fontWeight: '700',
    color: colors.text,
    marginBottom: 24,
  },

  // Groupe label + champ
  groupeChamp: {
    marginBottom: 16,
  },

  etiquette: {
    fontSize: 13,
    fontWeight: '600',
    color: colors.textSecondary,
    marginBottom: 6,
    textTransform: 'uppercase',
  },

  // La boîte autour du champ (icône + input + œil)
  contourChamp: {
    flexDirection: 'row',             // icône et input côte à côte
    alignItems: 'center',
    backgroundColor: colors.background,
    borderWidth: 1,
    borderColor: colors.border,
    borderRadius: 12,
    paddingHorizontal: 12,
    height: 50,
  },

  iconeChamp: {
    marginRight: 8,
  },

  champ: {
    flex: 1,                          // prend tout l'espace restant
    fontSize: 15,
    color: colors.text,
  },

  // Bouton vert
  bouton: {
    backgroundColor: colors.primary,
    borderRadius: 12,
    height: 52,
    justifyContent: 'center',
    alignItems: 'center',
    marginTop: 8,
  },

  // Bouton grisé pendant le chargement
  boutonDesactive: {
    opacity: 0.6,
  },

  texteBouton: {
    color: '#fff',
    fontSize: 16,
    fontWeight: '700',
  },

  // Lien "S'inscrire"
  lienInscription: {
    marginTop: 20,
    alignItems: 'center',
  },

  texteLien: {
    color: colors.textSecondary,
    fontSize: 14,
  },

  texteLienGras: {
    color: colors.primary,
    fontWeight: '700',
  },
});
