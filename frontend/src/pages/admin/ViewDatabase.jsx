import { useState, useEffect } from 'react';
import { Download, Search, User, MessageCircle } from 'lucide-react';
import { authService } from '../../services/authService';
import { partyService } from '../../services/partyService';

const ViewDatabase = () => {
  const [clients, setClients] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [usersData, partiesData] = await Promise.all([
          authService.getAllUsers(),
          partyService.getAll()
        ]);

        const processedClients = usersData.map(user => {
          const userParties = partiesData.filter(p => p.user && (p.user.id === user.id || p.user.uuid === user.uuid));
          
          return {
            id: user.id,
            uuid: user.uuid,
            nome: user.name || user.username,
            email: user.email,
            telefone: user.phone || 'Não informado', 
            // Mapeia a data de aniversário vinda do backend
            dataAniversario: user.birthday, 
            qtdFestas: userParties.length,
            role: user.roles && user.roles[0] ? user.roles[0].name : 'USER'
          };
        });
        
        setClients(processedClients);
      } catch (error) {
        console.error("Erro ao carregar base de dados:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  const handleWhatsAppContact = (client) => {
    const phone = client.telefone;

    if (!phone || phone === 'Não informado' || phone === '-') {
        alert(`O cliente ${client.nome} não possui um telefone cadastrado.`);
        return;
    }

    const cleanPhone = phone.toString().replace(/\D/g, '');
    let finalPhone = cleanPhone;
    if (cleanPhone.length >= 10 && cleanPhone.length <= 11) {
        finalPhone = `55${cleanPhone}`;
    }

    const message = `Olá ${client.nome}, somos da Castello Eventos! Entramos em contato referente ao seu cadastro.`;
    const url = `https://wa.me/${finalPhone}?text=${encodeURIComponent(message)}`;
    
    window.open(url, '_blank');
  };

  // Função auxiliar para formatar data (YYYY-MM-DD -> DD/MM/YYYY)
  const formatDate = (dateString) => {
    if (!dateString) return '-';
    try {
        const date = new Date(dateString);
        // Ajuste de fuso horário simples para datas puras (evita o dia anterior)
        const userTimezoneOffset = date.getTimezoneOffset() * 60000;
        const adjustedDate = new Date(date.getTime() + userTimezoneOffset);
        
        return adjustedDate.toLocaleDateString('pt-BR');
    } catch (e) {
        return dateString;
    }
  };

  const filteredClients = clients.filter(client => 
    client.nome.toLowerCase().includes(searchTerm.toLowerCase()) ||
    client.email.toLowerCase().includes(searchTerm.toLowerCase())
  );

  if (loading) {
    return <div className="p-8 text-center">Carregando base de clientes...</div>;
  }

  return (
    <div className="space-y-6">
      <div className="flex flex-col md:flex-row justify-between items-center gap-4">
        <h2 className="castello-title text-3xl">
          Painel do Administrador &gt; Ver base de dados
        </h2>
        
        <div className="relative">
            <input 
                type="text" 
                placeholder="Buscar por nome ou email..." 
                className="pl-10 pr-4 py-2 border border-gray-300 rounded-lg w-64 focus:outline-none focus:ring-2 focus:ring-castello-red"
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
            />
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" size={18} />
        </div>
      </div>

      <div className="castello-card p-6 overflow-x-auto min-h-[400px]">
        <table className="w-full min-w-[1000px]">
          <thead>
            <tr className="border-b border-gray-300 bg-gray-50">
              <th className="text-left py-3 px-4 font-semibold text-gray-900">Cliente</th>
              <th className="text-left py-3 px-4 font-semibold text-gray-900">Contato</th>
              <th className="text-left py-3 px-4 font-semibold text-gray-900 text-center">
                Festas Feitas
              </th>
              {/* COLUNA ALTERADA AQUI */}
              <th className="text-left py-3 px-4 font-semibold text-gray-900">
                Aniversário
              </th>
              <th className="text-left py-3 px-4 font-semibold text-gray-900">
                Nível
              </th>
              <th className="text-left py-3 px-4 font-semibold text-gray-900">Ações</th>
            </tr>
          </thead>
          <tbody>
            {filteredClients.length === 0 ? (
                <tr><td colSpan="6" className="p-6 text-center text-gray-500">Nenhum cliente encontrado.</td></tr>
            ) : (
                filteredClients.map((client) => (
                <tr key={client.id || client.uuid} className="border-b border-gray-200 hover:bg-gray-50 transition">
                    <td className="py-4 px-4">
                        <div className="flex items-center gap-3">
                            <div className="w-8 h-8 bg-gray-200 rounded-full flex items-center justify-center text-gray-500">
                                <User size={16}/>
                            </div>
                            <div>
                                <div className="font-bold text-gray-800">{client.nome}</div>
                                <div className="text-xs text-gray-500">ID: {client.id}</div>
                            </div>
                        </div>
                    </td>
                    <td className="py-4 px-4">
                        <div className="flex flex-col">
                            <span className="text-sm text-gray-800">{client.email}</span>
                            <span className="text-xs text-gray-500 font-bold text-green-600">
                                {client.telefone}
                            </span>
                        </div>
                    </td>
                    <td className="py-4 px-4 text-center">
                        <span className={`px-3 py-1 rounded-full text-xs font-bold ${client.qtdFestas > 0 ? 'bg-green-100 text-green-700' : 'bg-gray-100 text-gray-500'}`}>
                            {client.qtdFestas}
                        </span>
                    </td>
                    
                    {/* DADOS DA COLUNA ALTERADA AQUI */}
                    <td className="py-4 px-4 text-sm text-gray-700 font-medium">
                        {formatDate(client.dataAniversario)}
                    </td>

                    <td className="py-4 px-4">
                        <div className="text-xs font-bold uppercase text-gray-500">
                            {client.role}
                        </div>
                    </td>
                    <td className="py-4 px-4">
                    <button 
                        className="px-4 py-2 bg-green-50 text-green-600 border border-green-200 rounded-lg hover:bg-green-100 transition text-sm font-bold flex items-center gap-2"
                        onClick={() => handleWhatsAppContact(client)}
                        title={`Enviar WhatsApp para ${client.telefone}`}
                    >
                        <MessageCircle size={16} />
                        Contatar
                    </button>
                    </td>
                </tr>
                ))
            )}
          </tbody>
        </table>
      </div>

      <div className="flex justify-end">
        <button className="px-6 py-2 bg-gray-800 text-white rounded-lg hover:bg-gray-900 transition flex items-center space-x-2 shadow-lg">
          <Download size={18} />
          <span>Exportar CSV</span>
        </button>
      </div>
    </div>
  );
};

export default ViewDatabase;