import { Link, Outlet, useLocation } from 'react-router-dom';
import { Upload, Database, FileText, Trash2, Plus, Package } from 'lucide-react';

const AdminPanel = () => {
  const location = useLocation();

  const adminMenuItems = [
    { path: '/painel-adm/upload-logo', label: 'Upload de imagem da logo', icon: Upload },
    { path: '/painel-adm/base-dados', label: 'Ver base de dados', icon: Database },
    { path: '/painel-adm/solicitacoes', label: 'Ver solicitações', icon: FileText },
    { path: '/painel-adm/apagar-avaliacao', label: 'Apagar avaliação (aguarde...)', icon: Trash2 },
    { path: '/painel-adm/adicionar-festa', label: 'Adicionar festa', icon: Plus },
    { path: '/painel-adm/gerenciar-produtos', label: 'Cadastrar Produtos', icon: Package },
  ];

  // Se estiver em uma subpágina, mostrar apenas o conteúdo da subpágina
  const isSubPage = location.pathname !== '/painel-adm';

  if (isSubPage) {
    return <Outlet />;
  }

  // Se estiver na página principal do admin, mostrar o menu
  return (
    <div className="space-y-6">
      <h1 className="castello-title text-4xl mb-6">Painel do Administrador</h1>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        {adminMenuItems.map((item) => {
          const Icon = item.icon;
          const isActive = location.pathname === item.path;
          return (
            <Link
              key={item.path}
              to={item.path}
              className={`castello-card p-6 hover:shadow-lg transition ${
                isActive ? 'ring-2 ring-castello-red' : ''
              }`}
            >
              <div className="flex items-center space-x-4 mb-4">
                <Icon size={24} className="text-castello-dark" />
                <h3 className="font-semibold text-gray-900">{item.label}</h3>
              </div>
              <button className="w-full px-4 py-2 bg-green-500 text-white rounded-lg hover:bg-green-600 transition text-sm">
                Abrir
              </button>
            </Link>
          );
        })}
      </div>
    </div>
  );
};

export default AdminPanel;

