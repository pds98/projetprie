import React, { useState, useEffect } from 'react';
import {
  View, Text, TextInput, TouchableOpacity, StyleSheet,
  ScrollView, ActivityIndicator, Alert, Modal, FlatList,
} from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { reservationAPI, salleAPI, priereAPI } from '../../api/apiService';
import colors from '../../theme/colors';
import { useUser } from '../../context/UserContext';

export default function ReservationFormScreen({ navigation }) {
  const { currentUser } = useUser() || {};
  const [salles, setSalles]   = useState([]);
  const [prieres, setPrieres] = useState([]);
  const [loading, setLoading] = useState(false);
  const [priereModalVisible, setPriereModalVisible] = useState(false);

  const [form, setForm] = useState({
    idSalle: '',
    idPriere: '',
    nomPriere: '',   // juste pour afficher le nom choisi
    debut: '',
    fin: '',
    nombrePersonnes: '',
    motif: '',
  });

  useEffect(() => {
    (async () => {
      try {
        const [s, p] = await Promise.all([salleAPI.getAll(), priereAPI.getAll()]);
        setSalles(s.data);
        setPrieres(p.data);
      } catch {
        Alert.alert('Avertissement', 'Impossible de charger les salles / prières.');
      }
    })();
  }, []);

  const handleChange = (field, value) =>
    setForm(prev => ({ ...prev, [field]: value }));

  // Convertit JJ/MM/AAAA HH:MM → ISO
  const parseDate = (str) => {
    const m = str.trim().match(/^(\d{2})\/(\d{2})\/(\d{4})\s+(\d{2}):(\d{2})$/);
    if (!m) return null;
    const [, dd, mm, yyyy, hh, min] = m;
    return `${yyyy}-${mm}-${dd}T${hh}:${min}:00`;
  };

  const handleSubmit = async () => {
    const { idSalle, debut, fin, nombrePersonnes } = form;
    if (!idSalle || !debut || !fin || !nombrePersonnes) {
      Alert.alert('Champs requis', 'Salle, dates et nombre de personnes sont obligatoires.');
      return;
    }
    const debutISO = parseDate(debut);
    const finISO   = parseDate(fin);
    if (!debutISO || !finISO) {
      Alert.alert('Format de date invalide', 'Utilisez le format :\nJJ/MM/AAAA HH:MM\nEx : 25/04/2026 13:00');
      return;
    }
    setLoading(true);
    try {
      await reservationAPI.create({
        idSalle:         parseInt(idSalle),
        idEtudiant:      currentUser?.id || 1,
        idPriere:        form.idPriere ? parseInt(form.idPriere) : null,
        debut:           debutISO,
        fin:             finISO,
        nombrePersonnes: parseInt(nombrePersonnes),
        motif:           form.motif || '',
        estReserver:     true,
      });
      Alert.alert('Succès', 'Réservation créée !', [
        { text: 'OK', onPress: () => navigation.goBack() },
      ]);
    } catch {
      Alert.alert('Erreur', 'Impossible de créer la réservation.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <View style={styles.container}>
      {/* Header */}
      <View style={styles.header}>
        <TouchableOpacity onPress={() => navigation.goBack()} style={styles.backBtn}>
          <Ionicons name="arrow-back" size={22} color="#fff" />
        </TouchableOpacity>
        <Text style={styles.headerTitle}>Nouvelle réservation</Text>
      </View>

      <ScrollView contentContainerStyle={styles.scroll} keyboardShouldPersistTaps="handled">

        {/* ── Choix de la salle ── */}
        <View style={styles.section}>
          <Text style={styles.sectionTitle}>Choisir une salle</Text>
          <View style={styles.choicesRow}>
            {salles.map((s) => (
              <TouchableOpacity
                key={s.idSalle}
                style={[styles.chip, form.idSalle === String(s.idSalle) && styles.chipActive]}
                onPress={() => handleChange('idSalle', String(s.idSalle))}
              >
                <Text style={[styles.chipText, form.idSalle === String(s.idSalle) && styles.chipTextActive]}>
                  Salle {s.idSalle}  ({s.capacite} pl.)
                </Text>
              </TouchableOpacity>
            ))}
            {salles.length === 0 && (
              <Text style={styles.hint}>Chargement des salles…</Text>
            )}
          </View>
        </View>

        {/* ── Type de prière — menu déroulant ── */}
        <View style={styles.section}>
          <Text style={styles.sectionTitle}>Type de prière</Text>

          <TouchableOpacity
            style={styles.dropdown}
            onPress={() => setPriereModalVisible(true)}
            activeOpacity={0.7}
          >
            <Ionicons name="moon-outline" size={18} color={colors.textSecondary} style={{ marginRight: 10 }} />
            <Text style={form.idPriere ? styles.dropdownValue : styles.dropdownPlaceholder}>
              {form.nomPriere || 'Sélectionner une prière…'}
            </Text>
            <Ionicons name="chevron-down" size={18} color={colors.textSecondary} />
          </TouchableOpacity>
        </View>

        {/* ── Horaires ── */}
        <View style={styles.section}>
          <Text style={styles.sectionTitle}>Horaires</Text>

          <Text style={styles.label}>Début</Text>
          <View style={styles.inputRow}>
            <Ionicons name="calendar-outline" size={16} color={colors.textSecondary} style={styles.inputIcon} />
            <TextInput
              style={styles.input}
              placeholder="JJ/MM/AAAA HH:MM   ex : 25/04/2026 13:00"
              placeholderTextColor={colors.textSecondary}
              value={form.debut}
              onChangeText={v => handleChange('debut', v)}
              keyboardType="numbers-and-punctuation"
            />
          </View>

          <Text style={[styles.label, { marginTop: 12 }]}>Fin</Text>
          <View style={styles.inputRow}>
            <Ionicons name="calendar-outline" size={16} color={colors.textSecondary} style={styles.inputIcon} />
            <TextInput
              style={styles.input}
              placeholder="JJ/MM/AAAA HH:MM   ex : 25/04/2026 14:00"
              placeholderTextColor={colors.textSecondary}
              value={form.fin}
              onChangeText={v => handleChange('fin', v)}
              keyboardType="numbers-and-punctuation"
            />
          </View>
        </View>

        {/* ── Détails ── */}
        <View style={styles.section}>
          <Text style={styles.sectionTitle}>Détails</Text>

          <Text style={styles.label}>Nombre de personnes</Text>
          <View style={styles.inputRow}>
            <Ionicons name="people-outline" size={16} color={colors.textSecondary} style={styles.inputIcon} />
            <TextInput
              style={styles.input}
              placeholder="Ex : 20"
              keyboardType="numeric"
              value={form.nombrePersonnes}
              onChangeText={v => handleChange('nombrePersonnes', v)}
            />
          </View>

          <Text style={[styles.label, { marginTop: 12 }]}>Motif</Text>
          <View style={[styles.inputRow, { height: 80, alignItems: 'flex-start', paddingTop: 12 }]}>
            <TextInput
              style={[styles.input, { textAlignVertical: 'top' }]}
              placeholder="Ex : Prière collective du vendredi"
              multiline
              value={form.motif}
              onChangeText={v => handleChange('motif', v)}
            />
          </View>
        </View>

        {/* ── Bouton confirmer ── */}
        <TouchableOpacity
          style={[styles.submitBtn, loading && styles.submitBtnDisabled]}
          onPress={handleSubmit}
          disabled={loading}
        >
          {loading
            ? <ActivityIndicator color="#fff" />
            : <>
                <Ionicons name="checkmark-circle" size={20} color="#fff" />
                <Text style={styles.submitText}>Confirmer la réservation</Text>
              </>
          }
        </TouchableOpacity>
      </ScrollView>

      {/* ── Modal liste déroulante prières ── */}
      <Modal visible={priereModalVisible} transparent animationType="slide">
        <TouchableOpacity
          style={styles.modalOverlay}
          activeOpacity={1}
          onPress={() => setPriereModalVisible(false)}
        >
          <View style={styles.modalSheet} onStartShouldSetResponder={() => true}>
            <View style={styles.modalHandle} />
            <Text style={styles.modalTitle}>Choisir une prière</Text>

            <FlatList
              data={prieres}
              keyExtractor={p => String(p.id)}
              renderItem={({ item }) => (
                <TouchableOpacity
                  style={[
                    styles.priereOption,
                    form.idPriere === String(item.id) && styles.priereOptionActive,
                  ]}
                  onPress={() => {
                    handleChange('idPriere', String(item.id));
                    handleChange('nomPriere', item.nom);
                    setPriereModalVisible(false);
                  }}
                >
                  <View style={styles.priereOptionLeft}>
                    <View style={[
                      styles.priereCircle,
                      form.idPriere === String(item.id) && styles.priereCircleActive,
                    ]}>
                      <Ionicons
                        name="moon"
                        size={16}
                        color={form.idPriere === String(item.id) ? '#fff' : colors.primary}
                      />
                    </View>
                    <View>
                      <Text style={[
                        styles.priereNom,
                        form.idPriere === String(item.id) && styles.priereNomActive,
                      ]}>
                        {item.nom}
                      </Text>
                      {item.description ? (
                        <Text style={styles.priereDesc}>{item.description}</Text>
                      ) : null}
                    </View>
                  </View>
                  {form.idPriere === String(item.id) && (
                    <Ionicons name="checkmark-circle" size={22} color={colors.primary} />
                  )}
                </TouchableOpacity>
              )}
              ListEmptyComponent={
                <Text style={styles.emptyList}>Aucune prière disponible</Text>
              }
            />
          </View>
        </TouchableOpacity>
      </Modal>
    </View>
  );
}

const styles = StyleSheet.create({
  container:  { flex: 1, backgroundColor: colors.background },
  header: {
    backgroundColor: colors.primary, paddingTop: 60, paddingBottom: 20,
    paddingHorizontal: 16, flexDirection: 'row', alignItems: 'center', gap: 12,
  },
  backBtn:     { padding: 4 },
  headerTitle: { fontSize: 20, fontWeight: 'bold', color: '#fff' },
  scroll:      { padding: 20 },
  section:     { marginBottom: 24 },
  sectionTitle:{ fontSize: 15, fontWeight: '700', color: colors.text, marginBottom: 12 },
  label: {
    fontSize: 12, fontWeight: '600', color: colors.textSecondary,
    marginBottom: 6, textTransform: 'uppercase',
  },
  hint: { fontSize: 13, color: colors.textSecondary, fontStyle: 'italic' },

  // Chips de salle
  choicesRow:   { flexDirection: 'row', flexWrap: 'wrap', gap: 8 },
  chip: {
    paddingHorizontal: 14, paddingVertical: 9, borderRadius: 20,
    borderWidth: 1.5, borderColor: colors.border, backgroundColor: colors.surface,
  },
  chipActive:     { borderColor: colors.primary, backgroundColor: colors.primary + '18' },
  chipText:       { fontSize: 13, color: colors.textSecondary },
  chipTextActive: { color: colors.primary, fontWeight: '700' },

  // Dropdown prière
  dropdown: {
    flexDirection: 'row', alignItems: 'center', backgroundColor: colors.surface,
    borderWidth: 1.5, borderColor: colors.border, borderRadius: 12,
    paddingHorizontal: 14, height: 50,
  },
  dropdownPlaceholder: { flex: 1, fontSize: 14, color: colors.textSecondary },
  dropdownValue:       { flex: 1, fontSize: 14, color: colors.text, fontWeight: '600' },

  // Inputs
  inputRow: {
    flexDirection: 'row', alignItems: 'center', backgroundColor: colors.surface,
    borderWidth: 1, borderColor: colors.border, borderRadius: 12,
    paddingHorizontal: 12, height: 48,
  },
  inputIcon: { marginRight: 8 },
  input:     { flex: 1, fontSize: 14, color: colors.text },

  // Bouton soumettre
  submitBtn: {
    backgroundColor: colors.primary, borderRadius: 14, height: 54,
    flexDirection: 'row', justifyContent: 'center', alignItems: 'center',
    gap: 8, marginTop: 8, marginBottom: 40,
  },
  submitBtnDisabled: { opacity: 0.6 },
  submitText: { color: '#fff', fontSize: 16, fontWeight: '700' },

  // Modal bottom sheet
  modalOverlay: { flex: 1, backgroundColor: 'rgba(0,0,0,0.45)', justifyContent: 'flex-end' },
  modalSheet: {
    backgroundColor: '#fff', borderTopLeftRadius: 24, borderTopRightRadius: 24,
    paddingHorizontal: 20, paddingBottom: 40, maxHeight: '70%',
  },
  modalHandle: {
    width: 40, height: 4, backgroundColor: '#ddd',
    borderRadius: 2, alignSelf: 'center', marginVertical: 12,
  },
  modalTitle: { fontSize: 17, fontWeight: '700', color: colors.text, marginBottom: 16 },

  priereOption: {
    flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between',
    paddingVertical: 14, borderBottomWidth: 1, borderBottomColor: colors.border,
  },
  priereOptionActive: { backgroundColor: colors.primary + '08' },
  priereOptionLeft:   { flexDirection: 'row', alignItems: 'center', gap: 14 },
  priereCircle: {
    width: 38, height: 38, borderRadius: 19,
    backgroundColor: colors.primary + '18',
    justifyContent: 'center', alignItems: 'center',
  },
  priereCircleActive: { backgroundColor: colors.primary },
  priereNom:          { fontSize: 15, fontWeight: '600', color: colors.text },
  priereNomActive:    { color: colors.primary },
  priereDesc:         { fontSize: 12, color: colors.textSecondary, marginTop: 2 },
  emptyList:          { textAlign: 'center', color: colors.textSecondary, marginTop: 20 },
});
