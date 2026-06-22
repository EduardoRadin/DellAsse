import api from './api';

export const authService = {
  async login(username, password) {
    const response = await api.post('/user/login', { username, password });
    return response.data;
  },

  async getAllUsers() {
    const response = await api.get('/user/all');
    return response.data;
  },
  
  async register(userData) {
    const response = await api.post('/user/create', userData);
    return response.data;
  },

  async updateUser(userId, userData) {
    const response = await api.post(`/user/update/${userId}`, userData);
    return response.data;
  },

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
  },

  getToken() {
    return localStorage.getItem('token');
  },

  getUser() {
    const userStr = localStorage.getItem('user');
    return userStr ? JSON.parse(userStr) : null;
  },

  setAuth(token, user) {
    localStorage.setItem('token', token);
    localStorage.setItem('user', JSON.stringify(user));
  },
};

