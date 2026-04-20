import React, { useState } from 'react';
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  StyleSheet,
  ActivityIndicator,
  KeyboardAvoidingView,
  Platform,
  ScrollView,
  Alert,
} from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { etudiantAPI } from '../../api/apiService';
import colors from '../../theme/colors';

export default function RegisterScreen({ navigation })


{
  const [form, setForm] = useState({
    nom: '',
    prenom: '',
    telephone: '',
    email: '',
    motDePasse: '',
  });
  const [afficherMdp, setAfficherMdp] = useState(false);
  const [loading, setLoading] = useState(false);

  const handleChange = (field, value) =>
  {
    setForm((prev) => ({ ...prev, [field]: value }));
  };

  const handleRegister = async () => {
    const { nom, prenom, telephone, email, motDePasse } = form;
    if (!nom || !prenom || !email) {
      Alert.alert('Champs requis', 'Nom, prénom et email sont obligatoires.');
      return;
    }
    if (!motDePasse || motDePasse.length < 4) {
      Alert.alert('Mot de passe requis', 'Le mot de passe doit faire au moins 4 caractères.');
      return;
    }
    setLoading(true);
    try {
      await etudiantAPI.create({ nom, prenom, telephone, email, motDePasse });
      Alert.alert('Succès !', 'Votre compte a été créé.', [
        { text: 'OK', onPress: () => navigation.navigate('Login') },
      ]);
    } catch (error) {
      const msg = error.response?.data?.message || "Impossible de créer le compte.";
      Alert.alert('Erreur', msg);
    } finally {
      setLoading(false);
    }
  };
  const fields = [
    { key: 'nom', label: 'Nom', icon: 'person-outline', placeholder: 'Ex: Diallo' },
    { key: 'prenom', label: 'Prénom', icon: 'person-outline', placeholder: 'Ex: Mamadou' },
    { key: 'telephone', label: 'Téléphone', icon: 'call-outline', placeholder: '+221 77 000 00 00', keyboardType: 'phone-pad' },
    { key: 'email', label: 'Email', icon: 'mail-outline', placeholder: 'votre@email.com', keyboardType: 'email-address' },
    // motDePasse géré séparément pour afficher/cacher avec l'œil
  ];

  return (
    <KeyboardAvoidingView
      behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
      style={styles.container}
    >
      <ScrollView contentContainerStyle={styles.scroll} keyboardShouldPersistTaps="handled">
        {/* Header */}
        <View style={styles.header}>
          <TouchableOpacity style={styles.backBtn} onPress={() => navigation.goBack()}>
            <Ionicons name="arrow-back" size={24} color={colors.textLight} />
          </TouchableOpacity>
          <View style={styles.iconCircle}>
            <Ionicons name="person-add" size={36} color={colors.textLight} />
          </View>
          <Text style={styles.headerTitle}>Créer un compte</Text>
          <Text style={styles.headerSubtitle}>Rejoignez la communauté</Text>
        </View>

        {/* Formulaire */}
        <View style={styles.card}>
          {fields.map((field) => (
            <View key={field.key} style={styles.inputGroup}>
              <Text style={styles.label}>{field.label}</Text>
              <View style={styles.inputWrapper}>
                <Ionicons name={field.icon} size={18} color={colors.textSecondary} style={styles.inputIcon} />
                <TextInput
                  style={styles.input}
                  placeholder={field.placeholder}
                  placeholderTextColor={colors.textSecondary}
                  keyboardType={field.keyboardType || 'default'}
                  autoCapitalize={field.key === 'email' ? 'none' : 'words'}
                  value={form[field.key]}
                  onChangeText={(val) => handleChange(field.key, val)}
                />
              </View>
            </View>
          ))}

          {/* Champ Mot de passe (séparé pour le bouton œil) */}
          <View style={styles.inputGroup}>
            <Text style={styles.label}>Mot de passe</Text>
            <View style={styles.inputWrapper}>
              <Ionicons name="lock-closed-outline" size={18} color={colors.textSecondary} style={styles.inputIcon} />
              <TextInput
                style={styles.input}
                placeholder="••••••••"
                placeholderTextColor={colors.textSecondary}
                secureTextEntry={!afficherMdp}
                autoCapitalize="none"
                value={form.motDePasse}
                onChangeText={(val) => handleChange('motDePasse', val)}
              />
              <TouchableOpacity onPress={() => setAfficherMdp(!afficherMdp)}>
                <Ionicons
                  name={afficherMdp ? 'eye-off-outline' : 'eye-outline'}
                  size={18}
                  color={colors.textSecondary}
                />
              </TouchableOpacity>
            </View>
          </View>

          <TouchableOpacity
            style={[styles.button, loading && styles.buttonDisabled]}
            onPress={handleRegister}
            disabled={loading}
          >
            {loading ? (
              <ActivityIndicator color={colors.textLight} />
            ) : (
              <Text style={styles.buttonText}>S'inscrire</Text>
            )}
          </TouchableOpacity>

          <TouchableOpacity style={styles.linkRow} onPress={() => navigation.navigate('Login')}>
            <Text style={styles.linkText}>
              Déjà un compte ? <Text style={styles.linkBold}>Se connecter</Text>
            </Text>
          </TouchableOpacity>
        </View>
      </ScrollView>
    </KeyboardAvoidingView>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: colors.background },
  scroll: { flexGrow: 1 },
  header: {
    backgroundColor: colors.primary,
    paddingTop: 60,
    paddingBottom: 50,
    alignItems: 'center',
  },
  backBtn: {
    position: 'absolute',
    top: 55,
    left: 20,
    padding: 8,
  },
  iconCircle: {
    width: 72,
    height: 72,
    borderRadius: 36,
    backgroundColor: 'rgba(255,255,255,0.15)',
    justifyContent: 'center',
    alignItems: 'center',
    marginBottom: 12,
  },
  headerTitle: {
    fontSize: 24,
    fontWeight: 'bold',
    color: colors.textLight,
  },
  headerSubtitle: {
    fontSize: 13,
    color: 'rgba(255,255,255,0.75)',
    marginTop: 4,
  },
  card: {
    backgroundColor: colors.surface,
    borderRadius: 24,
    margin: 20,
    marginTop: -24,
    padding: 24,
    shadowColor: colors.shadow,
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 1,
    shadowRadius: 12,
    elevation: 6,
  },
  inputGroup: { marginBottom: 16 },
  label: {
    fontSize: 12,
    fontWeight: '600',
    color: colors.textSecondary,
    marginBottom: 6,
    textTransform: 'uppercase',
    letterSpacing: 0.5,
  },
  inputWrapper: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: colors.background,
    borderWidth: 1,
    borderColor: colors.border,
    borderRadius: 12,
    paddingHorizontal: 12,
    height: 50,
  },
  inputIcon: { marginRight: 8 },
  input: { flex: 1, fontSize: 15, color: colors.text },
  button: {
    backgroundColor: colors.primary,
    borderRadius: 12,
    height: 52,
    justifyContent: 'center',
    alignItems: 'center',
    marginTop: 8,
  },
  buttonDisabled: { opacity: 0.6 },
  buttonText: { color: colors.textLight, fontSize: 16, fontWeight: '700' },
  linkRow: { marginTop: 20, alignItems: 'center' },
  linkText: { color: colors.textSecondary, fontSize: 14 },
  linkBold: { color: colors.primary, fontWeight: '700' },
});
