import React, { useState, useCallback } from 'react';
import {
  View, Text, StyleSheet, FlatList, TouchableOpacity, ActivityIndicator,
  Alert, RefreshControl, Modal, TextInput, KeyboardAvoidingView, Platform,
} from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { useFocusEffect } from '@react-navigation/native';
import { reservationAPI } from '../../api/apiService';
import colors from '../../theme/colors';

export default function ReservationListScreen({ navigation }) {
  const [reservations, setReservations] = useState([]);
  const [loading, setLoading]           = useState(true);
  const [refreshing, setRefreshing]     = useState(false);

  // Modal modification
  const [editVisible, setEditVisible]   = useState(false);
  const [editItem, setEditItem]         = useState(null);
  const [editMotif, setEditMotif]       = useState('');
  const [editNombre, setEditNombre]     = useState('');
  const [editSaving, setEditSaving]     = useState(false);

  const fetchReservations = async () => {
    try {
      const res = await reservationAPI.getAll();
      setReservations(res.data);
    } catch {
      Alert.alert('Erreur', 'Impossible de charger les réservations.');
    } finally {
      setLoading(false);
      setRefreshing(false);
    }
  };

  useFocusEffect(useCallback(() => {
    setLoading(true);
    fetchReservations();
  }, []));

  // ─── Supprimer ─────────────────────────────────────────────
  const handleDelete = (id) => {
    Alert.alert('Annuler la réservation', 'Confirmer l\'annulation ?', [
      { text: 'Non', style: 'cancel' },
      {
        text: 'Confirmer', style: 'destructive',
        onPress: async () => {
          try {
            await reservationAPI.delete(id);
            setReservations(prev => prev.filter(r => r.id !== id));
          } catch {
            Alert.alert('Erreur', 'Suppression impossible.');
          }
        },
      },
    ]);
  };

  // ─── Ouvrir modal édition ───────────────────────────────────
  const ouvrirEdition = (item) => {
    setEditItem(item);
    setEditMotif(item.motif || '');
    setEditNombre(String(item.nombrePersonnes || ''));
    setEditVisible(true);
  };

  // ─── Sauvegarder modification ───────────────────────────────
  const sauvegarder = async () => {
    if (!editMotif.trim() || !editNombre) {
      Alert.alert('Champs requis', 'Motif et nombre de personnes sont obligatoires.');
      return;
    }
    setEditSaving(true);
    try {
      await reservationAPI.update(editItem.id, {
        motif: editMotif.trim(),
        nombrePersonnes: parseInt(editNombre),
      });
      setEditVisible(false);
      fetchReservations();
    } catch {
      Alert.alert('Erreur', 'Modification impossible.');
    } finally {
      setEditSaving(false);
    }
  };

  const formatDate = (dateStr) => {
    if (!dateStr) return '—';
    return new Date(dateStr).toLocaleDateString('fr-FR', {
      day: '2-digit', month: 'short', hour: '2-digit', minute: '2-digit',
    });
  };

  const renderItem = ({ item }) => (
    <View style={styles.card}>
      <View style={styles.cardHeader}>
        <View style={styles.badge}>
          <Text style={styles.badgeText}>Salle #{item.salle?.idSalle ?? '—'}</Text>
        </View>
        {/* Boutons modifier + supprimer */}
        <View style={{ flexDirection: 'row', gap: 12 }}>
          <TouchableOpacity onPress={() => ouvrirEdition(item)}>
            <Ionicons name="pencil-outline" size={20} color={colors.primary} />
          </TouchableOpacity>
          <TouchableOpacity onPress={() => handleDelete(item.id)}>
            <Ionicons name="trash-outline" size={20} color={colors.error || '#e63946'} />
          </TouchableOpacity>
        </View>
      </View>

      <Text style={styles.motif}>{item.motif || 'Sans motif'}</Text>

      <View style={styles.infoRow}>
        <Ionicons name="time-outline" size={14} color={colors.textSecondary} />
        <Text style={styles.infoText}>{formatDate(item.debut)} → {formatDate(item.fin)}</Text>
      </View>
      <View style={styles.infoRow}>
        <Ionicons name="people-outline" size={14} color={colors.textSecondary} />
        <Text style={styles.infoText}>{item.nombrePersonnes} personne(s)</Text>
      </View>

      <View style={[styles.statusChip, item.estActif ? styles.statusConfirme : styles.statusEnAttente]}>
        <Text style={styles.statusText}>{item.estActif ? '✓ Confirmée' : '⏳ En attente'}</Text>
      </View>
    </View>
  );

  return (
    <View style={styles.container}>
      <View style={styles.header}>
        <Text style={styles.headerTitle}>Mes réservations</Text>
        <TouchableOpacity style={styles.addBtn} onPress={() => navigation.navigate('FormulaireReservation')}>
          <Ionicons name="add" size={24} color="#fff" />
        </TouchableOpacity>
      </View>

      {loading ? (
        <View style={styles.centered}><ActivityIndicator size="large" color={colors.primary} /></View>
      ) : reservations.length === 0 ? (
        <View style={styles.centered}>
          <Ionicons name="calendar-outline" size={60} color={colors.border} />
          <Text style={styles.emptyText}>Aucune réservation</Text>
          <TouchableOpacity style={styles.emptyBtn} onPress={() => navigation.navigate('FormulaireReservation')}>
            <Text style={styles.emptyBtnText}>Faire une réservation</Text>
          </TouchableOpacity>
        </View>
      ) : (
        <FlatList
          data={reservations}
          keyExtractor={(item) => String(item?.id ?? Math.random())}
          renderItem={renderItem}
          contentContainerStyle={styles.list}
          refreshControl={
            <RefreshControl refreshing={refreshing}
              onRefresh={() => { setRefreshing(true); fetchReservations(); }}
              colors={[colors.primary]} />
          }
        />
      )}

      {/* Modal modification */}
      <Modal visible={editVisible} transparent animationType="slide" onRequestClose={() => setEditVisible(false)}>
        <KeyboardAvoidingView behavior={Platform.OS === 'ios' ? 'padding' : 'height'} style={{ flex: 1, justifyContent: 'flex-end' }}>
          <View style={styles.modalCard}>
            <View style={styles.modalHeaderRow}>
              <Text style={styles.modalTitle}>Modifier la réservation</Text>
              <TouchableOpacity onPress={() => setEditVisible(false)}>
                <Ionicons name="close" size={22} color={colors.textSecondary} />
              </TouchableOpacity>
            </View>

            <Text style={styles.modalLabel}>Motif</Text>
            <TextInput
              style={[styles.modalInput, { height: 80, textAlignVertical: 'top' }]}
              placeholder="Ex : Prière collective du vendredi"
              multiline
              value={editMotif}
              onChangeText={setEditMotif}
            />

            <Text style={styles.modalLabel}>Nombre de personnes</Text>
            <TextInput
              style={styles.modalInput}
              placeholder="Ex : 20"
              keyboardType="numeric"
              value={editNombre}
              onChangeText={setEditNombre}
            />

            <View style={styles.modalBtns}>
              <TouchableOpacity style={styles.modalBtnCancel} onPress={() => setEditVisible(false)}>
                <Text style={styles.modalBtnCancelText}>Annuler</Text>
              </TouchableOpacity>
              <TouchableOpacity style={[styles.modalBtnConfirm, editSaving && { opacity: 0.6 }]} onPress={sauvegarder} disabled={editSaving}>
                {editSaving ? <ActivityIndicator color="#fff" size="small" /> : <Text style={styles.modalBtnConfirmText}>Enregistrer</Text>}
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
    backgroundColor: colors.primary, paddingTop: 60, paddingBottom: 20,
    paddingHorizontal: 24, flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center',
  },
  headerTitle: { fontSize: 22, fontWeight: 'bold', color: '#fff' },
  addBtn: { backgroundColor: 'rgba(255,255,255,0.2)', width: 40, height: 40, borderRadius: 20, justifyContent: 'center', alignItems: 'center' },
  list: { padding: 16, gap: 12 },
  card: {
    backgroundColor: colors.surface, borderRadius: 16, padding: 16,
    shadowColor: colors.shadow, shadowOffset: { width: 0, height: 2 }, shadowOpacity: 1, shadowRadius: 6, elevation: 3, gap: 8,
  },
  cardHeader: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center' },
  badge: { backgroundColor: colors.primaryLight + '30', paddingHorizontal: 10, paddingVertical: 4, borderRadius: 20 },
  badgeText: { color: colors.primary, fontWeight: '700', fontSize: 12 },
  motif: { fontSize: 16, fontWeight: '600', color: colors.text },
  infoRow: { flexDirection: 'row', alignItems: 'center', gap: 6 },
  infoText: { fontSize: 13, color: colors.textSecondary },
  statusChip: { alignSelf: 'flex-start', paddingHorizontal: 10, paddingVertical: 4, borderRadius: 20, marginTop: 4 },
  statusConfirme: { backgroundColor: '#d8f3dc' },
  statusEnAttente: { backgroundColor: '#fff3cd' },
  statusText: { fontSize: 12, fontWeight: '600', color: colors.text },
  centered: { flex: 1, justifyContent: 'center', alignItems: 'center', gap: 16 },
  emptyText: { fontSize: 16, color: colors.textSecondary },
  emptyBtn: { backgroundColor: colors.primary, paddingHorizontal: 20, paddingVertical: 12, borderRadius: 12 },
  emptyBtnText: { color: '#fff', fontWeight: '600' },
  // Modal
  modalCard: { backgroundColor: '#fff', borderTopLeftRadius: 24, borderTopRightRadius: 24, padding: 24, gap: 12 },
  modalHeaderRow: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: 4 },
  modalTitle: { fontSize: 18, fontWeight: '700', color: colors.text },
  modalLabel: { fontSize: 12, fontWeight: '600', color: colors.textSecondary, textTransform: 'uppercase' },
  modalInput: {
    borderWidth: 1, borderColor: colors.border, borderRadius: 12,
    paddingHorizontal: 14, paddingVertical: 12, fontSize: 14, color: colors.text, backgroundColor: colors.background,
  },
  modalBtns: { flexDirection: 'row', gap: 12, marginTop: 4 },
  modalBtnCancel: { flex: 1, height: 48, borderRadius: 12, borderWidth: 1.5, borderColor: colors.border, justifyContent: 'center', alignItems: 'center' },
  modalBtnCancelText: { color: colors.textSecondary, fontWeight: '600' },
  modalBtnConfirm: { flex: 1, height: 48, borderRadius: 12, backgroundColor: colors.primary, justifyContent: 'center', alignItems: 'center' },
  modalBtnConfirmText: { color: '#fff', fontWeight: '700' },
});
