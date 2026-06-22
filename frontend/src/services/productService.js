import api from './api';

export const productService = {
  async create(productData) {
    const response = await api.post('/product/create', productData);
    return response.data;
  },

  async update(productId, productData) {
    const response = await api.patch(`/product/update/${productId}`, productData);
    return response.data;
  },
  async getAll() {
    const response = await api.get('/product/all');
    return response.data;
  },
  
};

