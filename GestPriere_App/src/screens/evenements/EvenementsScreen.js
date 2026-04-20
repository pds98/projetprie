import React, { useState, useCallback } from 'react';
import {
  View, Text, StyleSheet, FlatList, ActivityIndicator, Alert,
  RefreshControl, TouchableOpacity, Modal, TextInput,
  KeyboardAvoidingView, Platform,
} from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { useFocusEffect } from '@react-navigation/native';
import { evenementAPI } from '../../api/apiService';
import colors from '../../theme/colors';
import { useUser } from '../../context/UserContext';

const STATUT_COLORS = {
  actif:   { bg: '#d8f3dc', text: '#2d6a4f' },
  termine: { bg: '#e9ecef', text: '#6c757d' },
  annule:  { bg: '#ffe0e0', text: '#e63946' },
};

const EMPTY_FORM = { nom: '', description: '', lieu: '' };

// ─────────────────────────────────────────────────────────────────────────────
// EventModal extrait EN DEHORS du composant principal.
// Si on le laisse DEDANS, chaque keystroke provoque un re-render qui
// recrée la référence du composant → React démonte/remonte le Modal
// → le clavier se ferme à chaque lettre.
// ─────────────────────────────────────────────────────────────────────────────
const EventModal = ({ visible, onClose, title, form, setForm, onSubmit, submitting, submitLabel }) => (
  <Modal visible={visible} animationType="slide" transparent onRequestClose={onClose}>
    <KeyboardAvoidingView
      behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
      style={{ flex: 1, justifyContent: 'flex-end' }}
    >
      <View style={styles.modalCard}>
        <View style={styles.modalHeaderRow}>
          <Text style={styles.modalTitle}>{title}</Text>
          <TouchableOpacity onPress={onClose}>
            <Ionicons name="close" size={22} color={colors.textSecondary} />
          </TouchableOpacity>
        </View>

        <TextInput
          style={styles.modalInput}
          placeholder="Nom de l'événement *"
          placeholderTextColor={colors.textSecondary}
          value={form.nom}
          onChangeText={(v) => setForm(p => ({ ...p, nom: v }))}
        />
        <TextInput
          style={styles.modalInput}
          placeholder="Lieu *"
          placeholderTextColor={colors.textSecondary}
          value={form.lieu}
          onChangeText={(v) => setForm(p => ({ ...p, lieu: v }))}
        />
        <TextInput
          style={[styles.modalInput, { height: 80, textAlignVertical: 'top' }]}
          placeholder="Description (optionnel)"
          placeholderTextColor={colors.textSecondary}
          multiline
          value={form.description}
          onChangeText={(v) => setForm(p => ({ ...p, description: v }))}
        />

        <View style={styles.modalBtns}>
          <TouchableOpacity style={styles.modalBtnCancel} onPress={onClose}>
            <Text style={styles.modalBtnCancelText}>Annuler</Text>
          </TouchableOpacity>
          <TouchableOpacity
            style={[styles.modalBtnConfirm, submitting && { opacity: 0.6 }]}
            onPress={onSubmit}
            disabled={submitting}
          >
            {submitting
              ? <ActivityIndicator color="#fff" size="small" />
              : <Text style={styles.modalBtnConfirmText}>{submitLabel}</Text>}
          </TouchableOpacity>
        </View>
      </View>
    </KeyboardAvoidingView>
  </Modal>
);

// ─────────────────────────────────────────────────────────────────────────────
export default function EvenementsScreen() {
  const { currentUser } = useUser() || {};
  const [evenements, setEvenements]       = useState([]);
  const [loading, setLoading]             = useState(true);
  const [refreshing, setRefreshing]       = useState(false);
  const [loadingAction, setLoadingAction] = useState(null);

  const [createVisible, setCreateVisible] = useState(false);
  const [newEvent, setNewEvent]           = useState(EMPTY_FORM);
  const [saving, setSaving]               = useState(false);

  const [editVisible, setEditVisible]     = useState(false);
  const [editEvent, setEditEvent]         = useState(null);
  const [editForm, setEditForm]           = useState(EMPTY_FORM);
  const [editSaving, setEditSaving]       = useState(false);

  // ─── Chargement ──────────────────────────────────────────────
  const fetchEvenements = async () => {
    try {
      const res = await evenementAPI.getAll();
      setEvenements(res.data);
    } catch {
      Alert.alert('Erreur', 'Impossible de charger les événements.');
    } finally {
      setLoading(false);
      setRefreshing(false);
    }
  };

  useFocusEffect(useCallback(() => {
    setLoading(true);
    fetchEvenements();
  }, []));

  // ─── Participer / Quitter ─────────────────────────────────────
  const toggleParticipation = async (evenement) => {
    if (!currentUser?.id) return;
    const dejaInscrit = (evenement.participantIds || []).includes(currentUser.id);
    setLoadingAction(evenement.id);
    try {
      if (dejaInscrit) {
        await evenementAPI.quitter(evenement.id, currentUser.id);
      } else {
        await evenementAPI.participer(evenement.id, currentUser.id);
      }
      fetchEvenements();
    } catch {
      Alert.alert('Erreur', 'Action impossible, réessaie.');
    } finally {
      setLoadingAction(null);
    }
  };

  // ─── Créer ───────────────────────────────────────────────────
  const creerEvenement = async () => {
    if (!newEvent.nom || !newEvent.lieu) {
      Alert.alert('Champs requis', 'Nom et lieu sont obligatoires.');
      return;
    }
    setSaving(true);
    try {
      // On envoie idCreateurEvenement (Long) au lieu de l'objet Etudiant complet.
      // L'objet partiel { id } causait une erreur Hibernate (champs NOT NULL manquants).
      await evenementAPI.create({
        nom:                  newEvent.nom,
        description:          newEvent.description || '',
        lieu:                 newEvent.lieu,
        idCreateurEvenement:  currentUser?.id || null,
      });
      setCreateVisible(false);
      setNewEvent(EMPTY_FORM);
      fetchEvenements();
    } catch (e) {
      Alert.alert('Erreur', 'Impossible de créer l\'événement.');
      console.error('creerEvenement error:', e?.response?.data || e?.message);
    } finally {
      setSaving(false);
    }
  };

  // ─── Édition ─────────────────────────────────────────────────
  const ouvrirEdition = (item) => {
    setEditEvent(item);
    setEditForm({ nom: item.nom || '', description: item.description || '', lieu: item.lieu || '' });
    setEditVisible(true);
  };

  const sauvegarderModification = async () => {
    if (!editForm.nom || !editForm.lieu) {
      Alert.alert('Champs requis', 'Nom et lieu sont obligatoires.');
      return;
    }
    setEditSaving(true);
    try {
      await evenementAPI.update(editEvent.id, {
        nom:         editForm.nom,
        description: editForm.description,
        lieu:        editForm.lieu,
      });
      setEditVisible(false);
      setEditEvent(null);
      fetchEvenements();
    } catch {
      Alert.alert('Erreur', 'Impossible de modifier l\'événement.');
    } finally {
      setEditSaving(false);
    }
  };

  // ─── Supprimer ───────────────────────────────────────────────
  const supprimerEvenement = (id) => {
    Alert.alert('Supprimer', 'Confirmer la suppression ?', [
      { text: 'Annuler', style: 'cancel' },
      {
        text: 'Supprimer', style: 'destructive',
        onPress: async () => {
          try {
            await evenementAPI.delete(id);
            setEvenements(prev => prev.filter(e => e.id !== id));
          } catch {
            Alert.alert('Erreur', 'Suppression impossible.');
          }
        },
      },
    ]);
  };

  const formatDate = (dateStr) => {
    if (!dateStr) return '—';
    return new Date(dateStr).toLocaleDateString('fr-FR', {
      weekday: 'long', day: '2-digit', month: 'long', year: 'numeric',
    });
  };

  const getStatutStyle = (statut = '') =>
    STATUT_COLORS[statut.toLowerCase()] || { bg: '#e9ecef', text: '#6c757d' };

  // ─── Render card ─────────────────────────────────────────────
  const renderItem = ({ item }) => {
    const statutStyle    = getStatutStyle(item.statut);
    const dejaInscrit    = (item.participantIds || []).includes(currentUser?.id);
    const enCours        = loadingAction === item.id;
    const nbParticipants = (item.participantIds || []).length;
    const estCreateur    = item.createurEvenement?.id === currentUser?.id;

    return (
      <View style={styles.card}>
        <View style={styles.cardLeft}>
          <View style={styles.iconBox}>
            <Ionicons name="star" size={22} color={colors.primary} />
          </View>
        </View>

        <View style={styles.cardContent}>
          <View style={styles.cardTop}>
            <Text style={styles.eventName}>{item.nom}</Text>
            <View style={{ flexDirection: 'row', alignItems: 'center', gap: 8 }}>
              <View style={[styles.statusChip, { backgroundColor: statutStyle.bg }]}>
                <Text style={[styles.statusText, { color: statutStyle.text }]}>
                  {item.statut || 'N/A'}
                </Text>
              </View>
              {estCreateur && (
                <>
                  <TouchableOpacity onPress={() => ouvrirEdition(item)}>
                    <Ionicons name="pencil-outline" size={17} color={colors.primary} />
                  </TouchableOpacity>
                  <TouchableOpacity onPress={() => supprimerEvenement(item.id)}>
                    <Ionicons name="trash-outline" size={17} color="#e63946" />
                  </TouchableOpacity>
                </>
              )}
            </View>
          </View>

          {item.description ? (
            <Text style={styles.description} numberOfLines={2}>{item.description}</Text>
          ) : null}

          <View style={styles.metaRow}>
            <Ionicons name="location-outline" size={13} color={colors.textSecondary} />
            <Text style={styles.metaText}>{item.lieu || 'Lieu non précisé'}</Text>
          </View>
          <View style={styles.metaRow}>
            <Ionicons name="calendar-outline" size={13} color={colors.textSecondary} />
            <Text style={styles.metaText}>{formatDate(item.dateCreation)}</Text>
          </View>

          <View style={styles.actionRow}>
            <View style={styles.metaRow}>
              <Ionicons name="people-outline" size={13} color={colors.textSecondary} />
              <Text style={styles.metaText}>{nbParticipants} participant(s)</Text>
            </View>
            <TouchableOpacity
              style={[styles.participerBtn, dejaInscrit && styles.participerBtnInscrit]}
              onPress={() => toggleParticipation(item)}
              disabled={enCours}
            >
              {enCours ? (
                <ActivityIndicator size="small" color={dejaInscrit ? colors.primary : '#fff'} />
              ) : (
                <Text style={[styles.participerBtnText, dejaInscrit && styles.participerBtnTextInscrit]}>
                  {dejaInscrit ? '✓ Inscrit · Quitter' : 'Participer'}
                </Text>
              )}
            </TouchableOpacity>
          </View>
        </View>
      </View>
    );
  };

  // ─── Render ──────────────────────────────────────────────────
  return (
    <View style={styles.container}>
      <View style={styles.header}>
        <Text style={styles.headerTitle}>Événements</Text>
        <Text style={styles.headerSubtitle}>{evenements.length} événement(s) disponible(s)</Text>
      </View>

      {loading ? (
        <View style={styles.centered}><ActivityIndicator size="large" color={colors.primary} /></View>
      ) : evenements.length === 0 ? (
        <View style={styles.centered}>
          <Ionicons name="star-outline" size={60} color={colors.border} />
          <Text style={styles.emptyText}>Aucun événement pour le moment</Text>
        </View>
      ) : (
        <FlatList
          data={evenements}
          keyExtractor={(item) => item.id.toString()}
          renderItem={renderItem}
          contentContainerStyle={styles.list}
          refreshControl={
            <RefreshControl
              refreshing={refreshing}
              onRefresh={() => { setRefreshing(true); fetchEvenements(); }}
              colors={[colors.primary]}
            />
          }
        />
      )}

      <TouchableOpacity style={styles.fab} onPress={() => setCreateVisible(true)}>
        <Ionicons name="add" size={28} color="#fff" />
      </TouchableOpacity>

      <EventModal
        visible={createVisible}
        onClose={() => { setCreateVisible(false); setNewEvent(EMPTY_FORM); }}
        title="Nouvel événement"
        form={newEvent}
        setForm={setNewEvent}
        onSubmit={creerEvenement}
        submitting={saving}
        submitLabel="Créer"
      />

      <EventModal
        visible={editVisible}
        onClose={() => { setEditVisible(false); setEditEvent(null); }}
        title="Modifier l'événement"
        form={editForm}
        setForm={setEditForm}
        onSubmit={sauvegarderModification}
        submitting={editSaving}
        submitLabel="Enregistrer"
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container:    { flex: 1, backgroundColor: colors.background },
  header: {
    backgroundColor: colors.primary, paddingTop: 60, paddingBottom: 24, paddingHorizontal: 24,
  },
  headerTitle:    { fontSize: 24, fontWeight: 'bold', color: '#fff' },
  headerSubtitle: { fontSize: 13, color: 'rgba(255,255,255,0.75)', marginTop: 4 },
  list:           { padding: 16, gap: 12 },
  card: {
    backgroundColor: colors.surface, borderRadius: 16, flexDirection: 'row',
    overflow: 'hidden', shadowColor: colors.shadow, shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 1, shadowRadius: 6, elevation: 3,
  },
  cardLeft: {
    width: 56, backgroundColor: colors.primary + '15',
    justifyContent: 'center', alignItems: 'center',
  },
  iconBox: {
    width: 40, height: 40, borderRadius: 20,
    backgroundColor: colors.surface, justifyContent: 'center', alignItems: 'center',
  },
  cardContent:  { flex: 1, padding: 14, gap: 6 },
  cardTop:      { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'flex-start', gap: 8 },
  eventName:    { fontSize: 15, fontWeight: '700', color: colors.text, flex: 1 },
  statusChip:   { paddingHorizontal: 8, paddingVertical: 3, borderRadius: 10 },
  statusText:   { fontSize: 11, fontWeight: '600' },
  description:  { fontSize: 13, color: colors.textSecondary, lineHeight: 18 },
  metaRow:      { flexDirection: 'row', alignItems: 'center', gap: 5 },
  metaText:     { fontSize: 12, color: colors.textSecondary },
  actionRow:    { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginTop: 4 },
  participerBtn: {
    backgroundColor: colors.primary, paddingHorizontal: 14, paddingVertical: 6,
    borderRadius: 20, minWidth: 90, alignItems: 'center',
  },
  participerBtnInscrit:     { backgroundColor: 'transparent', borderWidth: 1.5, borderColor: colors.primary },
  participerBtnText:        { color: '#fff', fontSize: 12, fontWeight: '700' },
  participerBtnTextInscrit: { color: colors.primary },
  centered:   { flex: 1, justifyContent: 'center', alignItems: 'center', gap: 12 },
  emptyText:  { fontSize: 15, color: colors.textSecondary },
  fab: {
    position: 'absolute', bottom: 28, right: 24, width: 56, height: 56, borderRadius: 28,
    backgroundColor: colors.primary, justifyContent: 'center', alignItems: 'center',
    shadowColor: '#000', shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.3, shadowRadius: 6, elevation: 8,
  },
  // Modal
  modalCard: {
    backgroundColor: '#fff', borderTopLeftRadius: 24, borderTopRightRadius: 24,
    padding: 24, gap: 12,
  },
  modalHeaderRow: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: 4 },
  modalTitle:     { fontSize: 18, fontWeight: '700', color: colors.text },
  modalInput: {
    borderWidth: 1, borderColor: colors.border, borderRadius: 12,
    paddingHorizontal: 14, paddingVertical: 12, fontSize: 14,
    color: colors.text, backgroundColor: colors.background,
  },
  modalBtns:           { flexDirection: 'row', gap: 12, marginTop: 4 },
  modalBtnCancel: {
    flex: 1, height: 48, borderRadius: 12, borderWidth: 1.5,
    borderColor: colors.border, justifyContent: 'center', alignItems: 'center',
  },
  modalBtnCancelText:  { color: colors.textSecondary, fontWeight: '600' },
  modalBtnConfirm: {
    flex: 1, height: 48, borderRadius: 12,
    backgroundColor: colors.primary, justifyContent: 'center', alignItems: 'center',
  },
  modalBtnConfirmText: { color: '#fff', fontWeight: '700' },
});
