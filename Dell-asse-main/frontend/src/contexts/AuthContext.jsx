import { createContext, useContext, useState, useEffect } from 'react';
import { authService } from '../services/authService';
import { useMemo } from 'react';

const AuthContext = createContext(null);

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth deve ser usado dentro de AuthProvider');
  }
  return context;
};

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const storedUser = authService.getUser();
    const token = authService.getToken();

    if (storedUser && token) {
      setUser(storedUser);
    }
    setLoading(false);
  }, []);

  const login = async (username, password) => {
    try {
      const response = await authService.login(username, password);
      const userData = {
        username: response.username || username,
        roles: response.roles || [],
        token: response.token,
      };
      authService.setAuth(response.token, userData);
      setUser(userData);
      return { success: true };
    } catch (error) {
      return {
        success: false,
        error: error.response?.data?.message || 'Erro ao fazer login',
      };
    }
  };

  const register = async (userData) => {
    try {
      const response = await authService.register(userData);
      return { success: true, data: response };
    } catch (error) {
      return {
        success: false,
        error: error.response?.data?.message || 'Erro ao criar usuário',
      };
    }
  };

  const logout = () => {
    authService.logout();
    setUser(null);
  };

  const isAuthenticated = () => {
    return !!user && !!authService.getToken();
  };

  const hasRole = (role) => {
    return user?.roles?.includes(role) || false;
  };

  const value = useMemo(() => ({
    user,
    login,
    register,
    logout,
    isAuthenticated,
    hasRole,
    loading,
  }), [user, loading]);

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

