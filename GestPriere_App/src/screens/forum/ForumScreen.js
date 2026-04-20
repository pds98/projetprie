import React, { useState, useCallback } from 'react';
import {
  View, Text, StyleSheet, FlatList, TouchableOpacity, ActivityIndicator,
  Alert, RefreshControl, TextInput, Modal, KeyboardAvoidingView, Platform,
} from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { useFocusEffect } from '@react-navigation/native';
import { forumAPI } from '../../api/apiService';
import colors from '../../theme/colors';
import { useUser } from '../../context/UserContext';

export default function ForumScreen({ navigation }) {
  const { currentUser } = useUser() || {};
  const [forums, setForums]           = useState([]);
  const [loading, setLoading]         = useState(true);
  const [refreshing, setRefreshing]   = useState(false);

  // Modal création
  const [createVisible, setCreateVisible] = useState(false);
  const [newSujet, setNewSujet]           = useState('');
  const [creating, setCreating]           = useState(false);

  // Modal édition
  const [editVisible, setEditVisible]   = useState(false);
  const [editItem, setEditItem]         = useState(null);
  const [editSujet, setEditSujet]       = useState('');
  const [editSaving, setEditSaving]     = useState(false);

  // ─── Chargement ────────────────────────────────────────────
  const fetchForums = async () => {
    try {
      const res = await forumAPI.getAll();
      setForums(res.data);
    } catch {
      Alert.alert('Erreur', 'Impossible de charger les forums.');
    } finally {
      setLoading(false);
      setRefreshing(false);
    }
  };

  useFocusEffect(useCallback(() => {
    setLoading(true);
    fetchForums();
  }, []));

  // ─── Créer ─────────────────────────────────────────────────
  const handleCreate = async () => {
    if (!newSujet.trim()) {
      Alert.alert('Requis', 'Le sujet ne peut pas être vide.');
      return;
    }
    setCreating(true);
    try {
      await forumAPI.create({
        sujet:      newSujet.trim(),
        idEtudiant: currentUser?.id || null,
      });
      setNewSujet('');
      setCreateVisible(false);
      fetchForums();
    } catch {
      Alert.alert('Erreur', 'Impossible de créer le forum.');
    } finally {
      setCreating(false);
    }
  };

  // ─── Ouvrir édition ────────────────────────────────────────
  const ouvrirEdition = (item) => {
    setEditItem(item);
    setEditSujet(item.sujet || '');
    setEditVisible(true);
  };

  // ─── Sauvegarder modification ───────────────────────────────
  const sauvegarder = async () => {
    if (!editSujet.trim()) {
      Alert.alert('Requis', 'Le sujet ne peut pas être vide.');
      return;
    }
    setEditSaving(true);
    try {
      await forumAPI.update(editItem.id, { sujet: editSujet.trim() });
      setEditVisible(false);
      setEditItem(null);
      fetchForums();
    } catch {
      Alert.alert('Erreur', 'Modification impossible.');
    } finally {
      setEditSaving(false);
    }
  };

  // ─── Supprimer ─────────────────────────────────────────────
  const supprimerForum = (id) => {
    Alert.alert('Supprimer le sujet', 'Cette action est irréversible.', [
      { text: 'Annuler', style: 'cancel' },
      {
        text: 'Supprimer', style: 'destructive',
        onPress: async () => {
          try {
            await forumAPI.delete(id);
            setForums(prev => prev.filter(f => f.id !== id));
          } catch {
            Alert.alert('Erreur', 'Suppression impossible.');
          }
        },
      },
    ]);
  };

  const formatDate = (dateStr) => {
    if (!dateStr) return '';
    return new Date(dateStr).toLocaleDateString('fr-FR', { day: '2-digit', month: 'short', year: 'numeric' });
  };

  // ─── Render card ───────────────────────────────────────────
  const renderItem = ({ item, index }) => {
    const estCreateur = item.etudiant?.id === currentUser?.id;

    return (
      <View style={styles.card}>
        <View style={styles.indexBadge}>
          <Text style={styles.indexText}>{index + 1}</Text>
        </View>

        <View style={styles.cardContent}>
          <View style={styles.cardTop}>
            <Text style={styles.sujet}>{item.sujet}</Text>
            {/* Boutons edit/delete pour le créateur */}
            {estCreateur && (
              <View style={{ flexDirection: 'row', gap: 10 }}>
                <TouchableOpacity onPress={() => ouvrirEdition(item)}>
                  <Ionicons name="pencil-outline" size={17} color={colors.primary} />
                </TouchableOpacity>
                <TouchableOpacity onPress={() => supprimerForum(item.id)}>
                  <Ionicons name="trash-outline" size={17} color="#e63946" />
                </TouchableOpacity>
              </View>
            )}
          </View>

          <View style={styles.metaRow}>
            <Ionicons name="calendar-outline" size={12} color={colors.textSecondary} />
            <Text style={styles.metaText}>{formatDate(item.dateCreation)}</Text>
          </View>

          <TouchableOpacity
            style={styles.participerBtn}
            onPress={() => navigation.navigate('Messages', { forum: item })}
          >
            <Ionicons name="chatbubble-outline" size={13} color="#fff" />
            <Text style={styles.participerBtnText}>Participer à la discussion</Text>
          </TouchableOpacity>
        </View>
      </View>
    );
  };

  return (
    <View style={styles.container}>
      <View style={styles.header}>
        <Text style={styles.headerTitle}>Forum</Text>
        <TouchableOpacity style={styles.addBtn} onPress={() => setCreateVisible(true)}>
          <Ionicons name="add" size={24} color="#fff" />
        </TouchableOpacity>
      </View>

      {loading ? (
        <View style={styles.centered}><ActivityIndicator size="large" color={colors.primary} /></View>
      ) : forums.length === 0 ? (
        <View style={styles.centered}>
          <Ionicons name="chatbubbles-outline" size={60} color={colors.border} />
          <Text style={styles.emptyText}>Aucun sujet de discussion</Text>
          <TouchableOpacity style={styles.emptyBtn} onPress={() => setCreateVisible(true)}>
            <Text style={styles.emptyBtnText}>Créer le premier sujet</Text>
          </TouchableOpacity>
        </View>
      ) : (
        <FlatList
          data={forums}
          keyExtractor={(item) => String(item?.id ?? Math.random())}
          renderItem={renderItem}
          contentContainerStyle={styles.list}
          refreshControl={
            <RefreshControl refreshing={refreshing}
              onRefresh={() => { setRefreshing(true); fetchForums(); }}
              colors={[colors.primary]} />
          }
        />
      )}

      {/* ── Modal création ── */}
      <Modal visible={createVisible} transparent animationType="slide" onRequestClose={() => setCreateVisible(false)}>
        <KeyboardAvoidingView behavior={Platform.OS === 'ios' ? 'padding' : 'height'} style={{ flex: 1, justifyContent: 'flex-end' }}>
          <View style={styles.modalCard}>
            <View style={styles.modalHeader}>
              <Text style={styles.modalTitle}>Nouveau sujet</Text>
              <TouchableOpacity onPress={() => { setCreateVisible(false); setNewSujet(''); }}>
                <Ionicons name="close" size={22} color={colors.textSecondary} />
              </TouchableOpacity>
            </View>
            <TextInput
              style={styles.modalInput}
              placeholder="Ex: Questions sur la prière du Vendredi..."
              placeholderTextColor={colors.textSecondary}
              multiline
              value={newSujet}
              onChangeText={setNewSujet}
              autoFocus
            />
            <TouchableOpacity
              style={[styles.modalBtn, creating && { opacity: 0.6 }]}
              onPress={handleCreate}
              disabled={creating}
            >
              {creating ? <ActivityIndicator color="#fff" /> : <Text style={styles.modalBtnText}>Publier le sujet</Text>}
            </TouchableOpacity>
          </View>
        </KeyboardAvoidingView>
      </Modal>

      {/* ── Modal édition ── */}
      <Modal visible={editVisible} transparent animationType="slide" onRequestClose={() => setEditVisible(false)}>
        <KeyboardAvoidingView behavior={Platform.OS === 'ios' ? 'padding' : 'height'} style={{ flex: 1, justifyContent: 'flex-end' }}>
          <View style={styles.modalCard}>
            <View style={styles.modalHeader}>
              <Text style={styles.modalTitle}>Modifier le sujet</Text>
              <TouchableOpacity onPress={() => setEditVisible(false)}>
                <Ionicons name="close" size={22} color={colors.textSecondary} />
              </TouchableOpacity>
            </View>
            <TextInput
              style={styles.modalInput}
              placeholder="Nouveau sujet..."
              placeholderTextColor={colors.textSecondary}
              multiline
              value={editSujet}
              onChangeText={setEditSujet}
              autoFocus
            />
            <View style={styles.modalBtns}>
              <TouchableOpacity style={styles.modalBtnCancel} onPress={() => setEditVisible(false)}>
                <Text style={styles.modalBtnCancelText}>Annuler</Text>
              </TouchableOpacity>
              <TouchableOpacity style={[styles.modalBtn, { flex: 1 }, editSaving && { opacity: 0.6 }]} onPress={sauvegarder} disabled={editSaving}>
                {editSaving ? <ActivityIndicator color="#fff" /> : <Text style={styles.modalBtnText}>Enregistrer</Text>}
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
  list: { padding: 16, gap: 10 },
  card: {
    backgroundColor: colors.surface, borderRadius: 14, flexDirection: 'row',
    alignItems: 'flex-start', padding: 14, gap: 12,
    shadowColor: colors.shadow, shadowOffset: { width: 0, height: 2 }, shadowOpacity: 1, shadowRadius: 4, elevation: 2,
  },
  indexBadge: { width: 36, height: 36, borderRadius: 18, backgroundColor: colors.primary + '20', justifyContent: 'center', alignItems: 'center' },
  indexText: { color: colors.primary, fontWeight: '700', fontSize: 14 },
  cardContent: { flex: 1, gap: 4 },
  cardTop: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'flex-start', gap: 8 },
  sujet: { fontSize: 14, fontWeight: '600', color: colors.text, flex: 1 },
  metaRow: { flexDirection: 'row', alignItems: 'center', gap: 4 },
  metaText: { fontSize: 12, color: colors.textSecondary },
  participerBtn: {
    flexDirection: 'row', alignItems: 'center', gap: 6, backgroundColor: colors.primary,
    alignSelf: 'flex-start', paddingHorizontal: 12, paddingVertical: 6, borderRadius: 20, marginTop: 8,
  },
  participerBtnText: { color: '#fff', fontSize: 12, fontWeight: '700' },
  centered: { flex: 1, justifyContent: 'center', alignItems: 'center', gap: 16 },
  emptyText: { fontSize: 15, color: colors.textSecondary },
  emptyBtn: { backgroundColor: colors.primary, paddingHorizontal: 20, paddingVertical: 12, borderRadius: 12 },
  emptyBtnText: { color: '#fff', fontWeight: '600' },
  // Modals
  modalCard: { backgroundColor: colors.surface, borderTopLeftRadius: 24, borderTopRightRadius: 24, padding: 24, gap: 16 },
  modalHeader: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center' },
  modalTitle: { fontSize: 18, fontWeight: '700', color: colors.text },
  modalInput: {
    backgroundColor: colors.background, borderRadius: 12, borderWidth: 1,
    borderColor: colors.border, padding: 14, fontSize: 14, color: colors.text, minHeight: 80, textAlignVertical: 'top',
  },
  modalBtn: { backgroundColor: colors.primary, borderRadius: 12, height: 50, justifyContent: 'center', alignItems: 'center', marginBottom: 8 },
  modalBtnText: { color: '#fff', fontWeight: '700', fontSize: 15 },
  modalBtns: { flexDirection: 'row', gap: 12 },
  modalBtnCancel: { flex: 1, height: 50, borderRadius: 12, borderWidth: 1.5, borderColor: colors.border, justifyContent: 'center', alignItems: 'center', marginBottom: 8 },
  modalBtnCancelText: { color: colors.textSecondary, fontWeight: '600' },
});
