import React, { useState, useCallback } from 'react';
import {
  View, Text, StyleSheet, FlatList, TouchableOpacity,
  ActivityIndicator, Alert, Modal, TextInput, RefreshControl,
  KeyboardAvoidingView, Platform,
} from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { useFocusEffect } from '@react-navigation/native';
import { groupeAPI } from '../../api/apiService';
import { useUser } from '../../context/UserContext';
import colors from '../../theme/colors';

export default function GroupeScreen() {
  const { currentUser } = useUser() || {};
  const [groupes, setGroupes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);
  const [modalVisible, setModalVisible] = useState(false);
  const [form, setForm] = useState({ nom: '', description: '' });
  const [saving, setSaving] = useState(false);
  const [loadingAction, setLoadingAction] = useState(null);

  const fetchGroupes = async () => {
    try {
      const res = await groupeAPI.getAll();
      setGroupes(res.data);
    } catch {
      Alert.alert('Erreur', 'Impossible de charger les groupes.');
    } finally {
      setLoading(false);
      setRefreshing(false);
    }
  };

  useFocusEffect(useCallback(() => {
    setLoading(true);
    fetchGroupes();
  }, []));

  const creerGroupe = async () => {
    if (!form.nom.trim()) {
      Alert.alert('Champ requis', 'Le nom du groupe est obligatoire.');
      return;
    }
    setSaving(true);
    try {
      await groupeAPI.create({
        nom: form.nom,
        description: form.description,
        idEtudiant: currentUser?.id || null,
      });
      setModalVisible(false);
      setForm({ nom: '', description: '' });
      fetchGroupes();
    } catch {
      Alert.alert('Erreur', 'Impossible de créer le groupe.');
    } finally {
      setSaving(false);
    }
  };

  const toggleMembership = async (groupe) => {
    if (!currentUser?.id) return;
    const dejaInscrit = (groupe.membreIds || []).includes(currentUser.id);
    setLoadingAction(groupe.id);
    try {
      if (dejaInscrit) {
        await groupeAPI.quitter(groupe.id, currentUser.id);
      } else {
        await groupeAPI.rejoindre(groupe.id, currentUser.id);
      }
      fetchGroupes();
    } catch {
      Alert.alert('Erreur', 'Action impossible, réessaie.');
    } finally {
      setLoadingAction(null);
    }
  };

  const supprimerGroupe = (id) => {
    Alert.alert('Supprimer', 'Supprimer ce groupe ?', [
      { text: 'Annuler', style: 'cancel' },
      {
        text: 'Supprimer', style: 'destructive', onPress: async () => {
          try {
            await groupeAPI.delete(id);
            setGroupes(g => g.filter(x => x.id !== id));
          } catch {
            Alert.alert('Erreur', 'Suppression impossible.');
          }
        }
      }
    ]);
  };

  const renderItem = ({ item }) => {
    const dejaInscrit = (item.membreIds || []).includes(currentUser?.id);
    const estCreateur = item.createurGroupe?.id === currentUser?.id;
    const enCours = loadingAction === item.id;
    const nbMembres = (item.membreIds || []).length;

    return (
      <View style={styles.card}>
        <View style={styles.cardLeft}>
          <View style={styles.iconBox}>
            <Ionicons name="people" size={22} color={colors.primary} />
          </View>
        </View>
        <View style={styles.cardContent}>
          <Text style={styles.groupeName}>{item.nom}</Text>
          {item.description ? (
            <Text style={styles.description} numberOfLines={2}>{item.description}</Text>
          ) : null}
          <View style={styles.metaRow}>
            <Ionicons name="people-outline" size={13} color={colors.textSecondary} />
            <Text style={styles.metaText}>{nbMembres} membre(s)</Text>
          </View>
          {item.createurGroupe ? (
            <View style={styles.metaRow}>
              <Ionicons name="person-outline" size={13} color={colors.textSecondary} />
              <Text style={styles.metaText}>
                Créé par {item.createurGroupe.prenom} {item.createurGroupe.nom}
              </Text>
            </View>
          ) : null}

          {/* Bouton Rejoindre / Membre */}
          <View style={styles.actionRow}>
            <TouchableOpacity
              style={[styles.rejoindreBtn, dejaInscrit && styles.rejoindreBtnMembre]}
              onPress={() => toggleMembership(item)}
              disabled={enCours}
            >
              {enCours ? (
                <ActivityIndicator size="small" color={dejaInscrit ? colors.primary : '#fff'} />
              ) : (
                <Text style={[styles.rejoindreBtnText, dejaInscrit && styles.rejoindreBtnTextMembre]}>
                  {dejaInscrit ? '✓ Membre' : 'Rejoindre'}
                </Text>
              )}
            </TouchableOpacity>

            {/* Supprimer uniquement pour le créateur */}
            {estCreateur && (
              <TouchableOpacity onPress={() => supprimerGroupe(item.id)}>
                <Ionicons name="trash-outline" size={18} color={colors.error} />
              </TouchableOpacity>
            )}
          </View>
        </View>
      </View>
    );
  };

  return (
    <View style={styles.container}>
      {/* Header */}
      <View style={styles.header}>
        <Text style={styles.headerTitle}>Groupes</Text>
        <Text style={styles.headerSubtitle}>{groupes.length} groupe(s)</Text>
      </View>

      {loading ? (
        <View style={styles.centered}>
          <ActivityIndicator size="large" color={colors.primary} />
        </View>
      ) : groupes.length === 0 ? (
        <View style={styles.centered}>
          <Ionicons name="people-outline" size={60} color={colors.border} />
          <Text style={styles.emptyText}>Aucun groupe pour le moment</Text>
          <TouchableOpacity style={styles.emptyBtn} onPress={() => setModalVisible(true)}>
            <Text style={styles.emptyBtnText}>Créer le premier groupe</Text>
          </TouchableOpacity>
        </View>
      ) : (
        <FlatList
          data={groupes}
          keyExtractor={(item) => item.id.toString()}
          renderItem={renderItem}
          contentContainerStyle={styles.list}
          refreshControl={
            <RefreshControl
              refreshing={refreshing}
              onRefresh={() => { setRefreshing(true); fetchGroupes(); }}
              colors={[colors.primary]}
            />
          }
        />
      )}

      {/* FAB */}
      <TouchableOpacity style={styles.fab} onPress={() => setModalVisible(true)}>
        <Ionicons name="add" size={28} color="#fff" />
      </TouchableOpacity>

      {/* Modal création */}
      <Modal visible={modalVisible} animationType="slide" transparent>
        <KeyboardAvoidingView behavior={Platform.OS === 'ios' ? 'padding' : 'height'} style={{ flex: 1, justifyContent: 'flex-end' }}>
          <View style={styles.modalCard}>
            <Text style={styles.modalTitle}>Nouveau groupe</Text>
            <TextInput
              style={styles.modalInput}
              placeholder="Nom du groupe *"
              value={form.nom}
              onChangeText={(v) => setForm(p => ({ ...p, nom: v }))}
            />
            <TextInput
              style={[styles.modalInput, { height: 80, textAlignVertical: 'top' }]}
              placeholder="Description (optionnel)"
              multiline
              value={form.description}
              onChangeText={(v) => setForm(p => ({ ...p, description: v }))}
            />
            <View style={styles.modalBtns}>
              <TouchableOpacity
                style={styles.modalBtnCancel}
                onPress={() => { setModalVisible(false); setForm({ nom: '', description: '' }); }}
              >
                <Text style={styles.modalBtnCancelText}>Annuler</Text>
              </TouchableOpacity>
              <TouchableOpacity
                style={[styles.modalBtnConfirm, saving && { opacity: 0.6 }]}
                onPress={creerGroupe}
                disabled={saving}
              >
                {saving
                  ? <ActivityIndicator color="#fff" size="small" />
                  : <Text style={styles.modalBtnConfirmText}>Créer</Text>
                }
              </TouchableOpacity>
            </View>
          </View>
        </KeyboardAvoidingView>
      </Modal>
    </View>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: colors.background },
  header: {
    backgroundColor: colors.primary,
    paddingTop: 60,
    paddingBottom: 24,
    paddingHorizontal: 24,
  },
  headerTitle: { fontSize: 24, fontWeight: 'bold', color: '#fff' },
  headerSubtitle: { fontSize: 13, color: 'rgba(255,255,255,0.75)', marginTop: 4 },
  list: { padding: 16, gap: 12 },
  card: {
    backgroundColor: colors.surface,
    borderRadius: 16,
    flexDirection: 'row',
    alignItems: 'center',
    overflow: 'hidden',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.06,
    shadowRadius: 6,
    elevation: 3,
  },
  cardLeft: {
    width: 56,
    backgroundColor: colors.primary + '15',
    alignSelf: 'stretch',
    justifyContent: 'center',
    alignItems: 'center',
  },
  iconBox: {
    width: 40, height: 40, borderRadius: 20,
    backgroundColor: colors.surface,
    justifyContent: 'center', alignItems: 'center',
  },
  cardContent: { flex: 1, padding: 14, gap: 4, paddingRight: 14 },
  groupeName: { fontSize: 15, fontWeight: '700', color: colors.text },
  description: { fontSize: 13, color: colors.textSecondary },
  metaRow: { flexDirection: 'row', alignItems: 'center', gap: 5, marginTop: 2 },
  metaText: { fontSize: 12, color: colors.textSecondary },
  actionRow: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    marginTop: 8,
  },
  rejoindreBtn: {
    backgroundColor: colors.primary,
    paddingHorizontal: 14,
    paddingVertical: 6,
    borderRadius: 20,
    minWidth: 100,
    alignItems: 'center',
  },
  rejoindreBtnMembre: {
    backgroundColor: 'transparent',
    borderWidth: 1.5,
    borderColor: colors.primary,
  },
  rejoindreBtnText: { color: '#fff', fontSize: 12, fontWeight: '700' },
  rejoindreBtnTextMembre: { color: colors.primary },
  centered: { flex: 1, justifyContent: 'center', alignItems: 'center', gap: 12 },
  emptyText: { fontSize: 15, color: colors.textSecondary },
  emptyBtn: {
    backgroundColor: colors.primary, borderRadius: 12,
    paddingHorizontal: 20, paddingVertical: 12, marginTop: 4,
  },
  emptyBtnText: { color: '#fff', fontWeight: '700' },
  fab: {
    position: 'absolute', bottom: 28, right: 24,
    width: 56, height: 56, borderRadius: 28,
    backgroundColor: colors.primary,
    justifyContent: 'center', alignItems: 'center',
    shadowColor: '#000', shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.3, shadowRadius: 6, elevation: 8,
  },
  modalCard: {
    backgroundColor: '#fff', borderTopLeftRadius: 24, borderTopRightRadius: 24,
    padding: 24, gap: 12,
  },
  modalTitle: { fontSize: 18, fontWeight: '700', color: colors.text, marginBottom: 4 },
  modalInput: {
    borderWidth: 1, borderColor: colors.border, borderRadius: 12,
    paddingHorizontal: 14, paddingVertical: 12, fontSize: 14,
    color: colors.text, backgroundColor: colors.background,
  },
  modalBtns: { flexDirection: 'row', gap: 12, marginTop: 4 },
  modalBtnCancel: {
    flex: 1, height: 48, borderRadius: 12,
    borderWidth: 1.5, borderColor: colors.border,
    justifyContent: 'center', alignItems: 'center',
  },
  modalBtnCancelText: { color: colors.textSecondary, fontWeight: '600' },
  modalBtnConfirm: {
    flex: 1, height: 48, borderRadius: 12,
    backgroundColor: colors.primary,
    justifyContent: 'center', alignItems: 'center',
  },
  modalBtnConfirmText: { color: '#fff', fontWeight: '700' },
});
