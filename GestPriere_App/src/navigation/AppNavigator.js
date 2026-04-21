import React, { useState } from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import { Ionicons } from '@expo/vector-icons';
import colors from '../theme/colors';
import { UserContext } from '../context/UserContext';

import LoginScreen from '../screens/auth/LoginScreen';
import RegisterScreen from '../screens/auth/RegisterScreen';
import HomeScreen from '../screens/home/HomeScreen';
import ReservationListScreen from '../screens/reservation/ReservationListScreen';
import ReservationFormScreen from '../screens/reservation/ReservationFormScreen';
import EvenementsScreen from '../screens/evenements/EvenementsScreen';
import ForumScreen from '../screens/forum/ForumScreen';
import MessagesScreen from '../screens/forum/MessagesScreen';
import GroupeScreen from '../screens/groupe/GroupeScreen';

const Stack = createNativeStackNavigator();
const Tab   = createBottomTabNavigator();

function MenuDuBas({ currentUser, onLogout }) {
  return (
    <Tab.Navigator
      screenOptions={({ route }) => ({
        headerShown: false,
        tabBarActiveTintColor:   colors.primary,
        tabBarInactiveTintColor: colors.textSecondary,
        tabBarStyle: {
          backgroundColor: colors.surface,
          borderTopColor: colors.border,
          height: 65,
          paddingBottom: 10,
        },
        tabBarLabelStyle: { fontSize: 11, fontWeight: '600' },
        tabBarIcon: ({ focused, color, size }) => {
          let iconName;
          if      (route.name === 'Accueil')      iconName = focused ? 'home'        : 'home-outline';
          else if (route.name === 'Réservations') iconName = focused ? 'calendar'    : 'calendar-outline';
          else if (route.name === 'Événements')   iconName = focused ? 'star'        : 'star-outline';
          else if (route.name === 'Forum')        iconName = focused ? 'chatbubbles' : 'chatbubbles-outline';
          else if (route.name === 'Groupes')      iconName = focused ? 'people'      : 'people-outline';
          return <Ionicons name={iconName} size={size} color={color} />;
        },
      })}
    >
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

function PileReservation() {
  return (
    <Stack.Navigator screenOptions={{ headerShown: false }}>
      <Stack.Screen name="ListeReservations"     component={ReservationListScreen} />
      <Stack.Screen name="FormulaireReservation" component={ReservationFormScreen} />
    </Stack.Navigator>
  );
}

function PileForum() {
  return (
    <Stack.Navigator screenOptions={{ headerShown: false }}>
      <Stack.Screen name="ListeForum" component={ForumScreen} />
      <Stack.Screen name="Messages"   component={MessagesScreen} />
    </Stack.Navigator>
  );
}

function PileGroupe() {
  return (
    <Stack.Navigator screenOptions={{ headerShown: false }}>
      <Stack.Screen name="ListeGroupes" component={GroupeScreen} />
      <Stack.Screen name="Messages"     component={MessagesScreen} />
    </Stack.Navigator>
  );
}

export default function AppNavigator() {
  const [isConnecte, setIsConnecte]               = useState(false);
  const [utilisateurActuel, setUtilisateurActuel] = useState(null);

  const quandConnexion = (etudiant) => {
    setUtilisateurActuel(etudiant);
    setIsConnecte(true);
  };

  const quandDeconnexion = () => {
    setUtilisateurActuel(null);
    setIsConnecte(false);
  };

  return (
    <NavigationContainer>
      <Stack.Navigator screenOptions={{ headerShown: false }}>
        {!isConnecte ? (
          <>
            <Stack.Screen name="Login">
              {(props) => <LoginScreen {...props} onLogin={quandConnexion} />}
            </Stack.Screen>
            <Stack.Screen name="Register" component={RegisterScreen} />
          </>
        ) : (
          <Stack.Screen name="App">
            {(props) => (
              <UserContext.Provider value={{ currentUser: utilisateurActuel, onLogout: quandDeconnexion }}>
                <MenuDuBas {...props} currentUser={utilisateurActuel} onLogout={quandDeconnexion} />
              </UserContext.Provider>
            )}
          </Stack.Screen>
        )}
      </Stack.Navigator>
    </NavigationContainer>
  );
}
