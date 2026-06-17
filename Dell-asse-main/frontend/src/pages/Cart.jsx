import { useState, useEffect } from 'react';
import { Loader2, Plus } from 'lucide-react';
import { useAuth } from '../contexts/AuthContext';
import { partyService } from '../services/partyService';
import { useNavigate } from 'react-router-dom'; 

const Cart = () => {
  const { user } = useAuth();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(true);
  const [activeOrders, setActiveOrders] = useState([]);
  const [pastOrders, setPastOrders] = useState([]);

  useEffect(() => {
    const fetchOrders = async () => {
      try {
        const allParties = await partyService.getAll();
      
        const myParties = allParties.filter(party => 
            // Verifica se a festa tem dono e se é o usuário atual
            !party.user || party.user.username === user?.username
        );

        // Ativos: Pendente, Aprovado ou sem status definido
        const active = myParties.filter(p => p.status === 'PENDING' || p.status === 'APPROVED' || !p.status);
        
        // Passados: Rejeitado OU Concluído (Correção Aqui)
        const past = myParties.filter(p => p.status === 'REJECTED' || p.status === 'COMPLETED');

        setActiveOrders(active);
        setPastOrders(past);
      } catch (error) {
        console.error("Erro ao buscar pedidos:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchOrders();
  }, [user]);

  // Função para estilizar os status
  const getStatusStyle = (status) => {
    switch (status) {
      case 'APPROVED': return 'bg-green-100 border-green-400 text-green-700';
      case 'REJECTED': return 'bg-red-100 border-red-400 text-red-700';
      case 'COMPLETED': return 'bg-blue-100 border-blue-400 text-blue-700'; // Novo estilo para Concluído
      default: return 'bg-yellow-100 border-yellow-400 text-yellow-700'; 
    }
  };

  const getStatusLabel = (status) => {
    switch (status) {
      case 'APPROVED': return 'Aprovado';
      case 'REJECTED': return 'Negado';
      case 'COMPLETED': return 'Finalizado'; // Novo texto
      default: return 'Em análise';
    }
  };

  const formatDate = (dateString) => {
    if (!dateString) return new Date().toLocaleDateString('pt-BR');
    return new Date(dateString).toLocaleDateString('pt-BR');
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center h-64">
        <Loader2 className="animate-spin text-castello-red" size={48} />
      </div>
    );
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex justify-between items-center">
        <h1 className="castello-title text-4xl">Carrinho</h1>
        <button 
          onClick={() => navigate('/criar-festa')}
          className="px-6 py-2 bg-castello-red text-white rounded-lg hover:bg-red-800 transition"
        >
          Fazer pedido
        </button>
      </div>

      {/* Pedidos em andamento */}
      <div>
        <h2 className="text-xl font-bold text-gray-900 mb-4">Pedidos em andamento</h2>
        {activeOrders.length === 0 ? (
            <p className="text-gray-500 italic">Nenhum pedido em andamento.</p>
        ) : (
            <div className="space-y-4">
            {activeOrders.map((order) => (
                <div
                key={order.id}
                className={`castello-card p-6 border ${getStatusStyle(order.status)}`}
                >
                <div className="flex justify-between items-start mb-4">
                    <div>
                    <h3 className="font-bold text-lg mb-1">{order.name || order.title || "Festa sem título"}</h3>
                    <p className="text-sm opacity-80">{order.observation || order.description}</p>
                    
                    {/* Lista de Produtos */}
                    {order.products && order.products.length > 0 && (
                        <ul className="mt-2 text-xs list-disc list-inside opacity-80">
                            {order.products.map((p, idx) => (
                                <li key={p.id || idx}>{p.name}</li>
                            ))}
                        </ul>
                    )}
                    </div>
                    <div className="text-right">
                    <p className="text-sm opacity-80">
                        Última atualização: {formatDate(order.lastAtualization)}
                    </p>
                    <p className="text-sm font-bold mt-1">
                        Status: {getStatusLabel(order.status)}
                    </p>
                    {order.generateBudget > 0 && (
                       <p className="text-sm font-bold mt-1">
                           R$ {order.generateBudget?.toFixed(2)}
                       </p>
                    )}
                    </div>
                </div>
                
                {/* Botão extra se estiver aprovado */}
                {order.status === 'APPROVED' && (
                    <button className="mt-4 px-4 py-2 bg-castello-red text-white rounded-lg hover:bg-red-800 transition text-sm">
                    Acessar Gestão de Convite
                    </button>
                )}
                </div>
            ))}
            </div>
        )}
      </div>

      {/* Pedidos passados (Concluídos ou Rejeitados) */}
      <div>
        <h2 className="text-xl font-bold text-gray-900 mb-4">Histórico de Pedidos</h2>
        {pastOrders.length === 0 ? (
            <p className="text-gray-500 italic">Nenhum pedido finalizado.</p>
        ) : (
            <div className="space-y-4">
            {pastOrders.map((order) => (
                <div key={order.id} className={`castello-card p-6 border ${getStatusStyle(order.status)} opacity-90`}>
                <div className="flex justify-between items-start">
                    <div>
                    <h3 className="font-bold text-lg mb-1">{order.name || order.title || "Festa sem título"}</h3>
                    <p className="text-sm opacity-80">{order.observation || order.description}</p>
                    </div>
                    <div className="text-right">
                    <p className="text-sm opacity-80">
                        {formatDate(order.lastAtualization)}
                    </p>
                    <p className="text-sm font-bold mt-1">
                        Status: {getStatusLabel(order.status)}
                    </p>
                    </div>
                </div>
                </div>
            ))}
            </div>
        )}
      </div>

      {/* Botão Grande para Solicitar Nova Festa */}
      <div className="mt-8">
        <button 
            onClick={() => navigate('/criar-festa')}
            className="w-full py-4 border-2 border-dashed border-gray-300 rounded-xl flex items-center justify-center space-x-2 text-gray-500 hover:border-castello-red hover:text-castello-red hover:bg-red-50 transition group"
        >
            <div className="bg-gray-200 p-2 rounded-full group-hover:bg-red-100 transition">
                <Plus size={24} />
            </div>
            <span className="text-lg font-semibold">Solicitar nova festa personalizada</span>
        </button>
      </div>

    </div>
  );
};

export default Cart;