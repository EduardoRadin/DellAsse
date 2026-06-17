import api from './api';

export const galleryService = {
  async getAll() {
    const response = await api.get('/gallery');
    return response.data;
  },

  async create(galleryData) {
    const response = await api.post('/gallery', galleryData);
    return response.data;
  },

  async delete(id) {
    const response = await api.delete(`/gallery/${id}`);
    return response.data;
  }
};