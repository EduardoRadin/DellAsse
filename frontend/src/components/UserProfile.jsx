import { useState, useRef, useEffect } from 'react';
import { useAuth } from '../contexts/AuthContext';
import { LogOut, User, ChevronDown, ShieldCheck } from 'lucide-react';
import { useNavigate } from 'react-router-dom';

const UserProfile = () => {
  const { user, logout, isAuthenticated } = useAuth();
  const navigate = useNavigate();
  const [isOpen, setIsOpen] = useState(false);
  const dropdownRef = useRef(null);

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
        setIsOpen(false);
      }
    };

    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  if (!isAuthenticated() || !user) {
    return (
      <div className="flex items-center space-x-4">
        <button
          onClick={() => navigate('/login')}
          className="px-4 py-2 font-medium text-gray-600 hover:text-castello-red transition"
        >
          Entrar
        </button>
        <button
           onClick={() => navigate('/register')}
           className="px-4 py-2 bg-castello-red text-white rounded-lg hover:bg-red-700 transition font-bold text-sm"
        >
           Cadastrar
        </button>
      </div>
    );
  }

  // Verificação segura de Admin (verifica se roles contém 'ADMIN')
  const isAdmin = user.roles?.some(role => {
    const r = typeof role === 'string' ? role : role.name;
    return r === 'ADMIN' || r === 'ROLE_ADMIN';
  });

  return (
    <div className="relative" ref={dropdownRef}>
      {/* BOTÃO PRINCIPAL (TRIGGER DO DROPDOWN) */}
      <button
        onClick={() => setIsOpen(!isOpen)}
        className={`flex items-center gap-3 px-3 py-1.5 rounded-lg transition-all duration-200 border shadow-sm group ${
             isAdmin
               ? 'bg-green-50 border-green-200 hover:bg-green-100 hover:border-green-300'
               : 'bg-white border-gray-200 hover:bg-gray-50 hover:border-gray-300'
           }`}
      >
        {/* Ícone / Avatar */}
        <div className={`flex items-center justify-center w-8 h-8 rounded-full shadow-inner ${
            isAdmin ? 'bg-green-100 text-green-600' : 'bg-gray-100 text-gray-500'
        }`}>
           {isAdmin ? <ShieldCheck size={18} /> : <User size={18} />}
        </div>

        {/* Texto: Nome e Cargo */}
        <div className="flex flex-col text-left mr-1">
             <span className={`text-sm font-bold leading-none ${isAdmin ? 'text-green-800' : 'text-gray-700'}`}>
                {user.username || user.name || 'Usuário'}
             </span>
             <span className={`text-[10px] uppercase tracking-wider mt-0.5 font-semibold ${isAdmin ? 'text-green-600' : 'text-gray-400'}`}>
                {isAdmin ? 'Administrador' : 'Cliente'}
             </span>
        </div>

        {/* Setinha indicando dropdown */}
        <ChevronDown 
            size={16} 
            className={`transition-transform duration-200 ${isOpen ? 'rotate-180' : ''} ${isAdmin ? 'text-green-600' : 'text-gray-400 group-hover:text-gray-600'}`} 
        />
      </button>

      {/* CONTEÚDO DO DROPDOWN */}
      {isOpen && (
        <div className="absolute right-0 mt-2 w-56 bg-white rounded-xl shadow-xl border border-gray-100 py-2 z-50 animate-in fade-in slide-in-from-top-2 overflow-hidden">
          
          {/* Cabeçalho do Dropdown (Mobile visual helper) */}
          <div className="px-4 py-2 border-b border-gray-100 bg-gray-50 mb-1">
            <p className="text-xs text-gray-500">Logado como</p>
            <p className="text-sm font-bold text-gray-800 truncate">{user.username || user.name}</p>
          </div>

          <button
            onClick={() => {
              setIsOpen(false);
              // Adicione a navegação para perfil aqui se tiver
              // navigate('/perfil'); 
            }}
            className="w-full text-left px-4 py-2.5 text-sm text-gray-700 hover:bg-gray-50 hover:text-castello-red flex items-center gap-2 transition-colors"
          >
            <User size={16} />
            <span>Minha Conta</span>
          </button>

          <div className="my-1 border-t border-gray-100"></div>

          <button
            onClick={handleLogout}
            className="w-full text-left px-4 py-2.5 text-sm text-red-600 hover:bg-red-50 flex items-center gap-2 transition-colors font-medium"
          >
            <LogOut size={16} />
            <span>Sair</span>
          </button>
        </div>
      )}
    </div>
  );
};

export default UserProfile;