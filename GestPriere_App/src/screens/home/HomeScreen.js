// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
//  HomeScreen.js — Page d'accueil
//  Affiche : salutation, horaires des prières, raccourcis
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

import React from 'react';
import {
  View,
  Text,
  StyleSheet,
  ScrollView,
  TouchableOpacity,
  Alert,
  Dimensions,
} from 'react-native';

const CARD_WIDTH = (Dimensions.get('window').width - 24 * 2 - 12 * 3) / 2.3;
import { Ionicons } from '@expo/vector-icons';
import colors from '../../theme/colors';
import { useUser } from '../../context/UserContext';


// ─── Données fixes (pas dans la BD, juste pour affichage) ───
// On définit les 5 prières avec leurs horaires
const PRIERES = [
  { nom: 'Fajr',    heure: '05:30' },
  { nom: 'Dhuhr',   heure: '13:15' },
  { nom: 'Asr',     heure: '16:40' },
  { nom: 'Maghrib', heure: '19:50' },
  { nom: 'Isha',    heure: '21:15' },
];

// Les cartes de raccourcis vers les autres onglets
// "tab" = le nom de l'onglet dans AppNavigator
const RACCOURCIS = [
  { label: 'Réserver\nune salle',    icon: 'calendar',       tab: 'Réservations', couleur: colors.primary },
  { label: 'Voir les\névénements',   icon: 'star',           tab: 'Événements',   couleur: '#457b9d' },
  { label: 'Mes\ngroupes',           icon: 'people',         tab: 'Groupes',      couleur: '#2d6a4f' },
  { label: 'Ouvrir\nle forum',       icon: 'chatbubbles',    tab: 'Forum',        couleur: '#6d4c41' },
];


// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
//  Le composant HomeScreen
//  - navigation    : permet d'aller vers d'autres pages
//  - currentUser   : les infos de l'étudiant connecté
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
export default function HomeScreen({ navigation, currentUser }) {

  // On récupère le prénom de l'étudiant connecté
  // Le "?" = si currentUser est null, on évite une erreur
  // || 'Étudiant' = valeur par défaut si pas de prénom
  const prenom = currentUser?.prenom || 'Étudiant';
  const { onLogout } = useUser() || {};


  return (
    // ScrollView = la page peut défiler vers le bas
    <ScrollView style={styles.page} showsVerticalScrollIndicator={false}>

      {/* ── EN-TÊTE : salutation + initiale de l'utilisateur ── */}
      <View style={styles.entete}>
        <View>
          <Text style={styles.salutation}>As-salāmu ʿalaykum 👋</Text>
          <Text style={styles.prenom}>{prenom}</Text>
        </View>

        {/* Cercle avec l'initiale du prénom */}
        <TouchableOpacity
          style={styles.cercleAvatar}
          onPress={() => {
            Alert.alert(
              'Déconnexion',
              'Voulez-vous vous déconnecter ?',
              [
                { text: 'Annuler', style: 'cancel' },
                { text: 'Déconnexion', style: 'destructive', onPress: onLogout },
              ]
            );
          }}
        >
          <Text style={styles.initialeAvatar}>{prenom[0]?.toUpperCase()}</Text>
        </TouchableOpacity>
      </View>


      {/* ── SECTION : horaires des prières ── */}
      <View style={styles.section}>
        <Text style={styles.titreSectionion}>Prières du jour</Text>

        {/* Carte blanche avec la liste des prières */}
        <View style={styles.cartesPrieres}>

          {/* .map() = on répète ce bloc pour chaque prière du tableau */}
          {PRIERES.map((priere, index) => (

            // Chaque ligne de prière
            // "key" est obligatoire quand on utilise .map() en React
            <View
              key={index}
              style={[
                styles.lignePriere,
                // On ajoute une ligne de séparation entre les prières (pas sur la dernière)
                index < PRIERES.length - 1 && styles.ligneSeparation,
              ]}
            >
              {/* Petit point vert à gauche */}
              <View style={styles.point} />

              {/* Nom de la prière */}
              <Text style={styles.nomPriere}>{priere.nom}</Text>

              {/* Heure alignée à droite */}
              <Text style={styles.heurePriere}>{priere.heure}</Text>
            </View>
          ))}
        </View>
      </View>


      {/* ── SECTION : raccourcis vers les autres onglets ── */}
      <View style={styles.section}>
        <Text style={styles.titreSectionion}>Accès rapide</Text>

        {/* ScrollView horizontal = glissable gauche/droite */}
        <ScrollView
          horizontal
          showsHorizontalScrollIndicator={false}
          contentContainerStyle={styles.rangeeRaccourcis}
          decelerationRate="fast"
          snapToInterval={CARD_WIDTH + 12}
          snapToAlignment="start"
        >
          {RACCOURCIS.map((raccourci, index) => (
            <TouchableOpacity
              key={index}
              style={[styles.carteRaccourci, { backgroundColor: raccourci.couleur }]}
              onPress={() => navigation.navigate(raccourci.tab)}
            >
              <Ionicons name={raccourci.icon} size={28} color="#fff" />
              <Text style={styles.texteRaccourci}>{raccourci.label}</Text>
            </TouchableOpacity>
          ))}
        </ScrollView>
      </View>


      {/* ── PIED DE PAGE ── */}
      <View style={styles.piedDePage}>
        <Ionicons name="moon-outline" size={16} color={colors.textSecondary} />
        <Text style={styles.textePied}>  GestPrière — Université</Text>
      </View>

    </ScrollView>
  );
}


// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
//  STYLES
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
const styles = StyleSheet.create({

  page: {
    flex: 1,
    backgroundColor: colors.background,
  },

  // Bande verte en haut
  entete: {
    backgroundColor: colors.primary,
    paddingTop: 60,
    paddingBottom: 30,
    paddingHorizontal: 24,
    flexDirection: 'row',             // côte à côte : texte gauche, avatar droite
    justifyContent: 'space-between',  // espace maximum entre les deux
    alignItems: 'center',
  },

  salutation: {
    fontSize: 14,
    color: 'rgba(255,255,255,0.8)',
  },

  prenom: {
    fontSize: 24,
    fontWeight: 'bold',
    color: '#fff',
    marginTop: 2,
  },

  cercleAvatar: {
    width: 48,
    height: 48,
    borderRadius: 24,
    backgroundColor: 'rgba(255,255,255,0.2)',
    justifyContent: 'center',
    alignItems: 'center',
  },

  initialeAvatar: {
    color: '#fff',
    fontSize: 20,
    fontWeight: 'bold',
  },

  // Bloc de section (prières, raccourcis)
  section: {
    padding: 20,
  },

  titreSectionion: {
    fontSize: 16,
    fontWeight: '700',
    color: colors.text,
    marginBottom: 12,
  },

  // Carte contenant les lignes de prières
  cartesPrieres: {
    backgroundColor: colors.surface,
    borderRadius: 16,
    overflow: 'hidden',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.08,
    shadowRadius: 6,
    elevation: 3,
  },

  // Une ligne de prière (point + nom + heure)
  lignePriere: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingVertical: 14,
    paddingHorizontal: 16,
  },

  // Séparateur horizontal entre les lignes
  ligneSeparation: {
    borderBottomWidth: 1,
    borderBottomColor: colors.border,
  },

  // Petit rond vert
  point: {
    width: 8,
    height: 8,
    borderRadius: 4,
    backgroundColor: colors.primaryLight || colors.primary,
    marginRight: 12,
  },

  nomPriere: {
    flex: 1,                          // prend tout l'espace disponible (pousse l'heure à droite)
    fontSize: 15,
    color: colors.text,
    fontWeight: '500',
  },

  heurePriere: {
    fontSize: 15,
    color: colors.primary,
    fontWeight: '700',
  },

  // Rangée horizontale glissable des cartes raccourcis
  rangeeRaccourcis: {
    flexDirection: 'row',
    gap: 12,
    paddingRight: 8,                  // petit espace à droite pour voir qu'il y a encore des cartes
  },

  // Une carte raccourci (colorée)
  carteRaccourci: {
    width: CARD_WIDTH,                // largeur fixe pour le scroll horizontal
    borderRadius: 16,
    padding: 16,
    alignItems: 'center',
    justifyContent: 'center',
    minHeight: 110,
    gap: 8,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 3 },
    shadowOpacity: 0.15,
    shadowRadius: 6,
    elevation: 4,
  },

  texteRaccourci: {
    color: '#fff',
    fontSize: 12,
    fontWeight: '600',
    textAlign: 'center',
  },

  // Ligne du bas
  piedDePage: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    paddingVertical: 20,
  },

  textePied: {
    color: colors.textSecondary,
    fontSize: 13,
  },
});
