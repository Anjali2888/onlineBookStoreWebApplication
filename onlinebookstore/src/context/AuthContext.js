import React, { createContext, useState, useContext, useEffect } from 'react';
import { authAPI } from '../services/api';

const AuthContext = createContext(null);
export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  useEffect(() => {
    const credentials = localStorage.getItem('credentials');
    if (credentials) {
      authAPI.getCurrentUser()
        .then(setUser)
        .catch(() => {
          localStorage.removeItem('credentials');
        })
        .finally(() => setLoading(false));
    } else {
      setLoading(false);
    }
  }, []);
  const login = async (username, password) => {
    const { user, credentials } = await authAPI.login(username, password);
    localStorage.setItem('credentials', credentials);
    setUser(user);
    return user;
  };
  const register = async (userData) => {
    await authAPI.register(userData);
    return login(userData.username, userData.password);
  };
  const logout = () => {
    localStorage.removeItem('credentials');
    setUser(null);
  };
  const isAdmin = user?.role === 'ADMIN';
  
  return (
    <AuthContext.Provider value={{ user, login, register, logout, loading, isAdmin }}>
      {children}
    </AuthContext.Provider>
  );
};
export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within AuthProvider');
  }
  return context;
};