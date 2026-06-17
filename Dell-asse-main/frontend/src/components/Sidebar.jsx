import { Link, useLocation } from 'react-router-dom';
import { Home, Image, Mail, ShoppingCart, Settings } from 'lucide-react';
import { useAuth } from '../contexts/AuthContext';
import { useConfig } from '../contexts/ConfigContext';

const Sidebar = () => {
  const location = useLocation();
  const { isAuthenticated, hasRole } = useAuth();
  const { logo } = useConfig();

  const menuItems = [
    { path: '/', label: 'Início', icon: Home, show: true },
    { path: '/galeria', label: 'Galeria', icon: Image, show: true },
    { path: '/contato', label: 'Contato', icon: Mail, show: true },
    { path: '/carrinho', label: 'Carrinho', icon: ShoppingCart, show: isAuthenticated() },
    { path: '/painel-adm', label: 'Painel do ADM', icon: Settings, show: hasRole('ADMIN') },
  ];

  const isActive = (path) => {
    if (path === '/') return location.pathname === '/';
    return location.pathname.startsWith(path);
  };

  return (
    <div className="castello-sidebar w-64 min-h-screen fixed left-0 top-0 flex flex-col">
      
      <div className="bg-castello-darker w-full flex flex-col items-center justify-center py-8 transition-all duration-300">
        {logo ? (
          <img 
            src={logo} 
            alt="Logo Castello Del'asse" 
            className="w-32 h-auto object-cover rounded-xl shadow-lg hover:scale-105 transition-transform duration-300"
          />
        ) : (
          <div className="flex items-center space-x-3">
            <div className="text-white text-2xl">🏰</div>
            <div className="text-white">
              <div className="font-bold text-lg">Castello</div>
              <div className="text-sm">Del'asse</div>
            </div>
          </div>
        )}
      </div>

      {/* Navegação */}
      <nav className="flex-1 px-4 py-6 space-y-2">
        {menuItems
          .filter((item) => item.show)
          .map((item) => {
            const Icon = item.icon;
            const active = isActive(item.path);
            return (
              <Link
                key={item.path}
                to={item.path}
                className={`flex items-center space-x-3 px-4 py-3 rounded-lg transition-colors ${
                  active
                    ? 'bg-castello-darker text-white'
                    : 'text-white hover:bg-castello-darker/50'
                }`}
              >
                <Icon size={20} />
                <span className="font-medium">{item.label}</span>
              </Link>
            );
          })}
      </nav>
    </div>
  );
};

export default Sidebar;