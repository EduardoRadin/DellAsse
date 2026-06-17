import { useState } from 'react';
import { Check, X, Link as LinkIcon, Image as ImageIcon } from 'lucide-react'; 
import { useNavigate } from 'react-router-dom';
import { partyService } from '../../services/partyService';

const AddParty = () => {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);

  // Estado do formulário
  const [formData, setFormData] = useState({
    title: '',
    description: '',
    generateBudget: '',
    observations: '',
    imageURL: '',
    products: []
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  // Enviar para o Backend
  const handleSubmit = async () => {
    if (!formData.title || !formData.description || !formData.generateBudget) {
      alert("Por favor, preencha os campos obrigatórios.");
      return;
    }

    setLoading(true);

    try {
      const payload = {
        ...formData,
        generateBudget: parseFloat(formData.generateBudget),
        products: [] 
      };

      await partyService.create(payload);
      
      alert('Festa criada com sucesso!');
      navigate('/galeria');
    } catch (error) {
      console.error(error);
      // Se der erro, provavelmente a URL ainda é muito longa ou o backend rejeitou
      alert('Erro ao criar festa. Verifique se os dados estão corretos.');
    } finally {
      setLoading(false);
    }
  };

  const handleDiscard = () => {
    if (window.confirm("Deseja limpar o formulário?")) {
        setFormData({
            title: '',
            description: '',
            generateBudget: '',
            observations: '',
            imageURL: '',
            products: []
        });
    }
  };

  return (
    <div className="space-y-6">
      <h2 className="castello-title text-3xl">
        Painel do Administrador &gt; Adicionar festa
      </h2>

      <div className="max-w-4xl space-y-6">
        
        {/* Título */}
        <div>
          <label className="block font-semibold text-gray-900 mb-2">
            Insira um título *
          </label>
          <input
            type="text"
            name="title"
            value={formData.title}
            onChange={handleChange}
            className="w-full px-4 py-2 border border-gray-300 rounded-lg bg-gray-50 focus:outline-none focus:ring-2 focus:ring-castello-red"
            placeholder="Ex: Festa do Homem Aranha"
          />
        </div>

        {/* Orçamento */}
        <div>
          <label className="block font-semibold text-gray-900 mb-2">
            Orçamento Base (R$) *
          </label>
          <input
            type="number"
            name="generateBudget"
            value={formData.generateBudget}
            onChange={handleChange}
            className="w-full px-4 py-2 border border-gray-300 rounded-lg bg-gray-50 focus:outline-none focus:ring-2 focus:ring-castello-red"
            placeholder="Ex: 1500.00"
          />
        </div>

        {/* URL da Imagem (Substituindo o Upload) */}
        <div>
          <label className="block font-semibold text-gray-900 mb-2">
            Link da Imagem (URL)
          </label>
          <div className="flex gap-4">
            <div className="flex-1 relative">
                <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                    <LinkIcon className="h-5 w-5 text-gray-400" />
                </div>
                <input
                    type="url"
                    name="imageURL"
                    value={formData.imageURL}
                    onChange={handleChange}
                    className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg bg-gray-50 focus:outline-none focus:ring-2 focus:ring-castello-red"
                    placeholder="https://exemplo.com/foto-da-festa.jpg"
                />
            </div>
          </div>
          <p className="text-xs text-gray-500 mt-1">
            Cole o link de uma imagem da internet. O link deve ser curto.
          </p>

          {/* Preview da URL */}
          {formData.imageURL && (
            <div className="mt-4">
                <p className="text-sm font-semibold mb-2">Pré-visualização:</p>
                <div className="h-48 w-full max-w-xs border rounded-lg overflow-hidden bg-gray-100 flex items-center justify-center">
                    <img 
                        src={formData.imageURL} 
                        alt="Preview" 
                        className="w-full h-full object-cover"
                        onError={(e) => {
                            e.target.onerror = null;
                            e.target.src = "https://via.placeholder.com/150?text=Erro+na+Imagem";
                        }}
                    />
                </div>
            </div>
          )}
        </div>

        {/* Descrição */}
        <div>
          <label className="block font-semibold text-gray-900 mb-2">
            Insira a descrição *
          </label>
          <div className="relative">
            <textarea
              name="description"
              value={formData.description}
              onChange={handleChange}
              placeholder="Descreva os detalhes da festa..."
              maxLength={500}
              rows={6}
              className="w-full px-4 py-2 border border-gray-300 rounded-lg bg-gray-50 focus:outline-none focus:ring-2 focus:ring-castello-red resize-none"
            />
            <div className="absolute bottom-2 right-2 text-xs text-gray-500">
              {formData.description.length}/500
            </div>
          </div>
        </div>

        {/* Observações */}
        <div>
          <label className="block font-semibold text-gray-900 mb-2">
            Observações
          </label>
          <textarea
            name="observations"
            value={formData.observations}
            onChange={handleChange}
            placeholder="Observações internas..."
            rows={3}
            className="w-full px-4 py-2 border border-gray-300 rounded-lg bg-gray-50 focus:outline-none focus:ring-2 focus:ring-castello-red resize-none"
          />
        </div>

        {/* Botões */}
        <div className="flex justify-end space-x-4">
          <button 
            onClick={handleSubmit}
            disabled={loading}
            className="px-8 py-3 bg-green-400 text-white rounded-lg hover:bg-green-500 transition flex items-center space-x-2 disabled:opacity-50"
          >
            {loading ? (
                <span>Salvando...</span>
            ) : (
                <>
                    <Check size={18} />
                    <span>Confirmar e Salvar</span>
                </>
            )}
          </button>
          
          <button 
            onClick={handleDiscard}
            className="px-8 py-3 bg-red-300 text-white rounded-lg hover:bg-red-400 transition flex items-center space-x-2"
          >
            <X size={18} />
            <span>Descartar</span>
          </button>
        </div>
      </div>
    </div>
  );
};

export default AddParty;