import axios from 'axios';

const BASE_URL = 'https://9a91-70-27-223-121.ngrok-free.app/api';


const api = axios.create({
  baseURL: BASE_URL,
  headers: {
    'Content-Type': 'application/json',
    'ngrok-skip-browser-warning': 'true',

  },
  timeout: 10000,
});

// ─── ÉTUDIANTS ────────────────────────────────────────────
export const etudiantAPI = {
  getAll: () => api.get('/etudiants'),
  getById: (id) => api.get(`/etudiants/${id}`),
  create: (data) => api.post('/etudiants', data),
  update: (id, data) => api.put(`/etudiants/${id}`, data),
  delete: (id) => api.delete(`/etudiants/${id}`),
};

// ─── PRIÈRES ──────────────────────────────────────────────
export const priereAPI = {
  getAll: () => api.get('/prieres'),
  getById: (id) => api.get(`/prieres/${id}`),
  create: (data) => api.post('/prieres', data),
  update: (id, data) => api.put(`/prieres/${id}`, data),
  delete: (id) => api.delete(`/prieres/${id}`),
};

// ─── SALLES ───────────────────────────────────────────────
export const salleAPI = {
  getAll: () => api.get('/salles'),
  getById: (id) => api.get(`/salles/${id}`),
  create: (data) => api.post('/salles', data),
  update: (id, data) => api.put(`/salles/${id}`, data),
  delete: (id) => api.delete(`/salles/${id}`),
};

// ─── RÉSERVATIONS ─────────────────────────────────────────
export const reservationAPI = {
  getAll: () => api.get('/reservations'),
  getById: (id) => api.get(`/reservations/${id}`),
  create: (data) => api.post('/reservations', data),
  update: (id, data) => api.put(`/reservations/${id}`, data),
  delete: (id) => api.delete(`/reservations/${id}`),
};

// ─── ÉVÉNEMENTS ───────────────────────────────────────────
export const evenementAPI = {
  getAll: () => api.get('/evenements'),
  getById: (id) => api.get(`/evenements/${id}`),
  create: (data) => api.post('/evenements', data),
  update: (id, data) => api.put(`/evenements/${id}`, data),
  delete: (id) => api.delete(`/evenements/${id}`),
  participer: (id, idEtudiant) => api.post(`/evenements/${id}/participer`, { idEtudiant }),
  quitter: (id, idEtudiant) => api.delete(`/evenements/${id}/quitter/${idEtudiant}`),
};

// ─── GROUPES ──────────────────────────────────────────────
export const groupeAPI = {
  getAll: () => api.get('/groupes'),
  getById: (id) => api.get(`/groupes/${id}`),
  create: (data) => api.post('/groupes', data),
  update: (id, data) => api.put(`/groupes/${id}`, data),
  delete: (id) => api.delete(`/groupes/${id}`),
  rejoindre: (id, idEtudiant) => api.post(`/groupes/${id}/rejoindre`, { idEtudiant }),
  quitter: (id, idEtudiant) => api.delete(`/groupes/${id}/quitter/${idEtudiant}`),
};

// ─── FORUMS ───────────────────────────────────────────────
export const forumAPI = {
  getAll: () => api.get('/forums'),
  getById: (id) => api.get(`/forums/${id}`),
  create: (data) => api.post('/forums', data),
  update: (id, data) => api.put(`/forums/${id}`, data),
  delete: (id) => api.delete(`/forums/${id}`),
};

// ─── MESSAGES ─────────────────────────────────────────────
export const messageAPI = {
  getAll: () => api.get('/messages'),
  getById: (id) => api.get(`/messages/${id}`),
  create: (data) => api.post('/messages', data),
  delete: (id) => api.delete(`/messages/${id}`),
};

// ─── AUTH ─────────────────────────────────────────────────
export const authAPI = {
  login: (email, motDePasse) => api.post('/auth/login', { email, motDePasse }),
};

export default api;













