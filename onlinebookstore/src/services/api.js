import axios from 'axios';
const API_BASE_URL = 'http://localhost:8080/api';
const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});
// Add auth header to requests
api.interceptors.request.use((config) => {
  const credentials = localStorage.getItem('credentials');
  if (credentials) {
    config.headers.Authorization = `Basic ${credentials}`;
  }
  return config;
});
// Auth API
export const authAPI = {
  login: async (username, password) => {
    const credentials = btoa(`${username}:${password}`);
    const response = await api.get('/auth/me', {
      headers: { Authorization: `Basic ${credentials}` }
    });
    return { user: response.data.data, credentials };
  },
  
  register: async (userData) => {
    const response = await api.post('/auth/register', userData);
    return response.data.data;
  },
  
  getCurrentUser: async () => {
    const response = await api.get('/auth/me');
    return response.data.data;
  }
};
// Books API
export const booksAPI = {
getAll: async () => {
  const response = await api.get('/books');
  return response.data.data;
},
  
  getById: async (id) => {
    const response = await api.get(`/books/${id}`);
    return response.data.data;
  },
  
  search: async (keyword) => {
    const response = await api.get(`/books/search?keyword=${encodeURIComponent(keyword)}`);
    return response.data.data;
  },
  
  getByCategory: async (category) => {
    const response = await api.get(`/books/category/${encodeURIComponent(category)}`);
    return response.data.data;
  },
  
  create: async (bookData) => {
    const response = await api.post('/books', bookData);
    return response.data.data;
  },
  
  update: async (id, bookData) => {
    const response = await api.put(`/books/${id}`, bookData);
    return response.data.data;
  },
  
  delete: async (id) => {
    await api.delete(`/books/${id}`);
  }
};
// Orders API
export const ordersAPI = {
  getMyOrders: async () => {
    const response = await api.get('/orders');
    return response.data.data;
  },
  getAllOrders: async () =>{
    const response=await api.get('/orders/admin/all');
    return response.data.data;
  },
 
  
  getById: async (id) => {
    const response = await api.get(`/orders/${id}`);
    return response.data.data;
  },
  
  create: async (orderData) => {
    const response = await api.post('/orders', orderData);
    return response.data.data;
  },
  
  updateStatus: async (id, status) => {
    const response = await api.patch(`/orders/admin/${id}/status?status=${status}`);
    return response.data.data;
  }
};
export default api;
