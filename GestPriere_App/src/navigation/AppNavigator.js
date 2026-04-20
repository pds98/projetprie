// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
//  AppNavigator.js — Le GPS de l'application
//  Ce fichier décide QUELLE PAGE afficher selon la situation
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

import React, { useState } from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import { Ionicons } from '@expo/vector-icons';
import colors from '../theme/colors';
import { UserContext } from '../context/UserContext';

// On importe all pages de l'app
import LoginScreen from '../screens/auth/LoginScreen';
import RegisterScreen from '../screens/auth/RegisterScreen';
import HomeScreen from '../screens/home/HomeScreen';
import ReservationListScreen from '../screens/reservation/ReservationListScreen';
import ReservationFormScreen from '../screens/reservation/ReservationFormScreen';
import EvenementsScreen from '../screens/evenements/EvenementsScreen';
import ForumScreen from '../screens/forum/ForumScreen';
import MessagesScreen from '../screens/forum/MessagesScreen';
import GroupeScreen from '../screens/groupe/GroupeScreen';

// Stack = navigation en pile (page A → page B → retour à A)
const Stack = createNativeStackNavigator();

// Tab = la barre de menu en bas avec les icônes
const Tab = createBottomTabNavigator();


// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
//  MENU DU BAS — affiché une fois l'utilisateur connecté
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
function MenuDuBas({ currentUser, onLogout }) {
  return (
    <Tab.Navigator
      screenOptions={({ route }) => ({

        // On cache le titre en haut (chaque page gère son propre header)
        headerShown: false,

        // Couleur de l'icône quand l'onglet EST sélectionné
        tabBarActiveTintColor: colors.primary,

        // Couleur de l'icône quand l'onglet N'EST PAS sélectionné
        tabBarInactiveTintColor: colors.textSecondary,

        // Style visuel de la barre du bas
        tabBarStyle: {
          backgroundColor: colors.surface,
          borderTopColor: colors.border,
          height: 65,
          paddingBottom: 10,
        },

        // Style du texte sous chaque icône
        tabBarLabelStyle: {
          fontSize: 11,
          fontWeight: '600',
        },

        // Cette fonction choisit quelle icône afficher selon l'onglet
        tabBarIcon: ({ focused, color, size }) => {
          let iconName;

          if (route.name === 'Accueil') {
            // focused = true si cet onglet est actif
            iconName = focused ? 'home' : 'home-outline';

          } else if (route.name === 'Réservations') {
            iconName = focused ? 'calendar' : 'calendar-outline';

          } else if (route.name === 'Événements') {
            iconName = focused ? 'star' : 'star-outline';

          } else if (route.name === 'Forum') {
            iconName = focused ? 'chatbubbles' : 'chatbubbles-outline';

          } else if (route.name === 'Groupes') {
            iconName = focused ? 'people' : 'people-outline';
          }

          // On retourne l'icône avec la bonne couleur et taille
          return <Ionicons name={iconName} size={size} color={color} />;
        },
      })}
    >
      {/* Chaque Tab.Screen = un onglet dans le menu du bas */}
      <Tab.Screen name="Accueil">
        {(props) => <HomeScreen {...props} currentUser={currentUser} />}
      </Tab.Screen>
      <Tab.Screen name="Réservations" component={PileReservation} />
      <Tab.Screen name="Événements"   component={EvenementsScreen} />
      <Tab.Screen name="Forum"        component={PileForum} />
      <Tab.Screen name="Groupes"      component={PileGroupe} />
    </Tab.Navigator>
  );
}


// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
//  PILES DE NAVIGATION (Stack)
//  Une "pile" permet d'aller d'une page à une autre
//  et de revenir en arrière (comme un historique)
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

// Pile Réservation : Liste des réservations → Formulaire pour en créer une
function PileReservation() {
  return (
    <Stack.Navigator screenOptions={{ headerShown: false }}>
      <Stack.Screen name="ListeReservations" component={ReservationListScreen} />
      <Stack.Screen name="FormulaireReservation" component={ReservationFormScreen} />
    </Stack.Navigator>
  );
}

// Pile Forum : Liste des forums → Messages d'un forum
function PileForum() {
  return (
    <Stack.Navigator screenOptions={{ headerShown: false }}>
      <Stack.Screen name="ListeForum" component={ForumScreen} />
      <Stack.Screen name="Messages"   component={MessagesScreen} />
    </Stack.Navigator>
  );
}

// Pile Groupe : Liste des groupes → Messages d'un groupe
function PileGroupe() {
  return (
    <Stack.Navigator screenOptions={{ headerShown: false }}>
      <Stack.Screen name="ListeGroupes" component={GroupeScreen} />
      <Stack.Screen name="Messages"     component={MessagesScreen} />
    </Stack.Navigator>
  );
}


// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
//  NAVIGATEUR PRINCIPAL — point d'entrée de l'app
//  Logique : si connecté → menu du bas, sinon → login
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
export default function AppNavigator() {

  // isConnecte = vrai/faux : est-ce que l'utilisateur est connecté ?
  const [isConnecte, setIsConnecte] = useState(false);

  // utilisateurActuel = les infos de l'étudiant connecté (nom, email, etc.)
  const [utilisateurActuel, setUtilisateurActuel] = useState(null);

  // Appelée par LoginScreen quand la connexion réussit
  const quandConnexion = (etudiant) => {
    setUtilisateurActuel(etudiant); // On mémorise l'étudiant
    setIsConnecte(true);            // On passe en mode "connecté"
  };

  // Appelée quand l'utilisateur veut se déconnecter
  const quandDeconnexion = () => {
    setUtilisateurActuel(null); // On efface les infos
    setIsConnecte(false);       // On retourne à l'écran de login
  };

  return (
    // NavigationContainer = le conteneur global obligatoire pour la navigation
    <NavigationContainer>

      <Stack.Navigator screenOptions={{ headerShown: false }}>

        {/* Si PAS connecté → on affiche Login et Register */}
        {!isConnecte ? (
          <>
            <Stack.Screen name="Login">
              {/* On passe "quandConnexion" à LoginScreen pour qu'il puisse l'appeler */}
              {(props) => <LoginScreen {...props} onLogin={quandConnexion} />}
            </Stack.Screen>

            <Stack.Screen name="Register" component={RegisterScreen} />
          </>
        ) : (
          /* Si connecté → on affiche le menu du bas avec toutes les pages */
          <Stack.Screen name="App">
            {(props) => (
              <UserContext.Provider value={{ currentUser: utilisateurActuel, onLogout: quandDeconnexion }}>
                <MenuDuBas
                  {...props}
                  currentUser={utilisateurActuel}
                  onLogout={quandDeconnexion}
                />
              </UserContext.Provider>
            )}
          </Stack.Screen>
        )}

      </Stack.Navigator>
    </NavigationContainer>
  );
}
