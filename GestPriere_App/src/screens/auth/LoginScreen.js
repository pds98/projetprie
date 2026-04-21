import React, { useState } from 'react';
import {
  View, Text, TextInput, TouchableOpacity, StyleSheet,
  ActivityIndicator, KeyboardAvoidingView, Platform, ScrollView, Alert,
} from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { authAPI } from '../../api/apiService';
import colors from '../../theme/colors';

export default function LoginScreen({ navigation, onLogin }) {
  const [email, setEmail]           = useState('');
  const [motDePasse, setMotDePasse] = useState('');
  const [afficherMdp, setAfficherMdp] = useState(false);
  const [chargement, setChargement]   = useState(false);

  const seConnecter = async () => {
    if (!email.trim()) {
      Alert.alert('Champ vide', 'Veuillez entrer votre email.');
      return;
    }
    if (!motDePasse.trim()) {
      Alert.alert('Champ vide', 'Veuillez entrer votre mot de passe.');
      return;
    }
    setChargement(true);
    try {
      const reponse = await authAPI.login(email.trim().toLowerCase(), motDePasse);
      onLogin(reponse.data);
    } catch (erreur) {
      const status  = erreur.response?.status;
      const message = erreur.response?.data?.message;
      if (status === 401 && message) {
        Alert.alert('Connexion refusée', message);
      } else {
        Alert.alert('Erreur réseau', 'Impossible de joindre le serveur. Vérifiez votre WiFi.');
      }
    } finally {
      setChargement(false);
    }
  };

  return (
    <KeyboardAvoidingView behavior={Platform.OS === 'ios' ? 'padding' : 'height'} style={styles.page}>
      <ScrollView contentContainerStyle={styles.scroll} keyboardShouldPersistTaps="handled">
        <View style={styles.entete}>
          <View style={styles.cercleLune}>
            <Ionicons name="moon" size={40} color="#fff" />
          </View>
          <Text style={styles.titreApp}>GestPrière</Text>
          <Text style={styles.sousTitre}>Bienvenue sur votre espace</Text>
        </View>

        <View style={styles.carte}>
          <Text style={styles.titreCarte}>Connexion</Text>

          <View style={styles.groupeChamp}>
            <Text style={styles.etiquette}>Email</Text>
            <View style={styles.contourChamp}>
              <Ionicons name="mail-outline" size={18} color={colors.textSecondary} style={styles.iconeChamp} />
              <TextInput
                style={styles.champ}
                placeholder="votre@email.com"
                placeholderTextColor={colors.textSecondary}
                keyboardType="email-address"
                autoCapitalize="none"
                value={email}
                onChangeText={setEmail}
              />
            </View>
          </View>

          <View style={styles.groupeChamp}>
            <Text style={styles.etiquette}>Mot de passe</Text>
            <View style={styles.contourChamp}>
              <Ionicons name="lock-closed-outline" size={18} color={colors.textSecondary} style={styles.iconeChamp} />
              <TextInput
                style={styles.champ}
                placeholder="••••••••"
                placeholderTextColor={colors.textSecondary}
                secureTextEntry={!afficherMdp}
                value={motDePasse}
                onChangeText={setMotDePasse}
              />
              <TouchableOpacity onPress={() => setAfficherMdp(!afficherMdp)}>
                <Ionicons name={afficherMdp ? 'eye-off-outline' : 'eye-outline'} size={18} color={colors.textSecondary} />
              </TouchableOpacity>
            </View>
          </View>

          <TouchableOpacity style={[styles.bouton, chargement && styles.boutonDesactive]} onPress={seConnecter} disabled={chargement}>
            {chargement ? <ActivityIndicator color="#fff" /> : <Text style={styles.texteBouton}>Se connecter</Text>}
          </TouchableOpacity>

          <TouchableOpacity style={styles.lienInscription} onPress={() => navigation.navigate('Register')}>
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

const styles = StyleSheet.create({
  page:            { flex: 1, backgroundColor: colors.background },
  scroll:          { flexGrow: 1 },
  entete: {
    backgroundColor: colors.primary, paddingTop: 80, paddingBottom: 50, alignItems: 'center',
  },
  cercleLune: {
    width: 80, height: 80, borderRadius: 40,
    backgroundColor: 'rgba(255,255,255,0.15)',
    justifyContent: 'center', alignItems: 'center', marginBottom: 12,
  },
  titreApp:    { fontSize: 28, fontWeight: 'bold', color: '#fff', letterSpacing: 1 },
  sousTitre:   { fontSize: 14, color: 'rgba(255,255,255,0.75)', marginTop: 4 },
  carte: {
    backgroundColor: colors.surface, borderRadius: 24, margin: 20, marginTop: -24, padding: 24,
    shadowColor: '#000', shadowOffset: { width: 0, height: 4 }, shadowOpacity: 0.1, shadowRadius: 12, elevation: 6,
  },
  titreCarte:   { fontSize: 20, fontWeight: '700', color: colors.text, marginBottom: 24 },
  groupeChamp:  { marginBottom: 16 },
  etiquette: {
    fontSize: 13, fontWeight: '600', color: colors.textSecondary,
    marginBottom: 6, textTransform: 'uppercase',
  },
  contourChamp: {
    flexDirection: 'row', alignItems: 'center', backgroundColor: colors.background,
    borderWidth: 1, borderColor: colors.border, borderRadius: 12, paddingHorizontal: 12, height: 50,
  },
  iconeChamp:     { marginRight: 8 },
  champ:          { flex: 1, fontSize: 15, color: colors.text },
  bouton: {
    backgroundColor: colors.primary, borderRadius: 12, height: 52,
    justifyContent: 'center', alignItems: 'center', marginTop: 8,
  },
  boutonDesactive: { opacity: 0.6 },
  texteBouton:     { color: '#fff', fontSize: 16, fontWeight: '700' },
  lienInscription: { marginTop: 20, alignItems: 'center' },
  texteLien:       { color: colors.textSecondary, fontSize: 14 },
  texteLienGras:   { color: colors.primary, fontWeight: '700' },
});
