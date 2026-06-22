import api from './api';

export const enterpriseService = {
  async create(enterpriseData) {
    const response = await api.post('/enterprise/create', enterpriseData);
    return response.data;
  },

  async update(enterpriseId, enterpriseData) {
    const response = await api.post(`/enterprise/update/${enterpriseId}`, enterpriseData);
    return response.data;
  },

  async getById(enterpriseId) {
    const response = await api.get(`/enterprise/${enterpriseId}`);
    return response.data;
  },

  async getAll() {
    const response = await api.get('/enterprise/all');
    return response.data;
  },
};

