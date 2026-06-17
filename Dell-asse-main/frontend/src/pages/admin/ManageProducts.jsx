import { useState } from 'react';
import { productService } from '../../services/productService';
import { Save, Package, DollarSign, Image as ImageIcon } from 'lucide-react';

const ManageProducts = () => {
  const [loading, setLoading] = useState(false);
  const [formData, setFormData] = useState({
    name: '',
    description: '',
    price: '',
    stockQuantity: '',
    category: 'Móveis', 
    imageUrl: ''
  });

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      await productService.create({
        ...formData,
        price: parseFloat(formData.price),
        stockQuantity: parseInt(formData.stockQuantity)
      });
      alert('Produto cadastrado com sucesso!');
      setFormData({ name: '', description: '', price: '', stockQuantity: '', category: 'Móveis', imageUrl: '' });
    } catch (error) {
      console.error(error);
      alert('Erro ao cadastrar produto');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="max-w-2xl mx-auto p-6 bg-white rounded-lg shadow-md">
      <h2 className="text-2xl font-bold mb-6 flex items-center gap-2">
        <Package className="text-castello-red" /> Novo Produto
      </h2>

      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="block text-sm font-bold text-gray-700">Nome do Produto</label>
          <input
            required
            type="text"
            className="w-full p-2 border rounded mt-1 focus:border-castello-red outline-none"
            value={formData.name}
            onChange={e => setFormData({...formData, name: e.target.value})}
          />
        </div>

        <div className="grid grid-cols-2 gap-4">
            <div>
                <label className="block text-sm font-bold text-gray-700">Preço (R$)</label>
                <div className="relative">
                    <DollarSign size={16} className="absolute top-3 left-2 text-gray-400"/>
                    <input
                        required
                        type="number"
                        step="0.01"
                        className="w-full pl-8 p-2 border rounded mt-1 focus:border-castello-red outline-none"
                        value={formData.price}
                        onChange={e => setFormData({...formData, price: e.target.value})}
                    />
                </div>
            </div>
            <div>
                <label className="block text-sm font-bold text-gray-700">Quantidade em Estoque</label>
                <input
                    required
                    type="number"
                    className="w-full p-2 border rounded mt-1 focus:border-castello-red outline-none"
                    value={formData.stockQuantity}
                    onChange={e => setFormData({...formData, stockQuantity: e.target.value})}
                />
            </div>
        </div>

        <div>
          <label className="block text-sm font-bold text-gray-700">Categoria</label>
          <select
            className="w-full p-2 border rounded mt-1 bg-white"
            value={formData.category}
            onChange={e => setFormData({...formData, category: e.target.value})}
          >
            <option>Móveis</option>
            <option>Mesas de Jantar</option>
            <option>Mesas de Bolo</option>
            <option>Mesas de Canto/Apoio</option>
            <option>Mesas Bistrô</option>
            <option>Mesas Comunitárias</option>
            <option>Cadeiras Tiffany</option>
            <option>Cadeiras Dior</option>
            <option>Cadeiras de Ferro</option>
            <option>Cadeiras de Madeira</option>
            <option>Banquetas</option>
            <option>Puffs</option>
            <option>Poltronas</option>
            <option>Sofás e Lounges</option>
            <option>Aparadores</option>
            <option>Estantes e Armários</option>
            <option>Carrinhos de Chá/Bar</option>
            <option>Cômodas Bombê</option>
            <option>Vitrines</option>
            
            <option>Painéis Redondos</option>
            <option>Painéis Retangulares</option>
            <option>Painéis de Pallet</option>
            <option>Painéis Ripados</option>
            <option>Painéis Shimmer (Lantejoula)</option>
            <option>Muro Inglês (Folhagem)</option>
            <option>Arco de Ferro</option>
            <option>Arco Romano</option>
            <option>Biombos</option>
            <option>Cortinas e Tecidos de Fundo</option>
            <option>Estruturas Box Truss</option>
            <option>Tendas e Gazebos</option>
            <option>Pista de Dança</option>
            <option>Tapetes e Passadeiras</option>
            <option>Grama Sintética</option>
            
            <option>Louças</option>
            <option>Pratos Rasos</option>
            <option>Pratos de Sobremesa</option>
            <option>Pratos Fundos</option>
            <option>Sousplats de Cristal</option>
            <option>Sousplats de Rattan</option>
            <option>Sousplats de Tecido</option>
            <option>Sousplats MDF</option>
            <option>Talheres Dourados</option>
            <option>Talheres Prateados</option>
            <option>Talheres Rose Gold</option>
            <option>Taças de Água</option>
            <option>Taças de Vinho</option>
            <option>Taças de Espumante</option>
            <option>Taças Bico de Jaca</option>
            <option>Copos de Whisky</option>
            <option>Copos Long Drink</option>
            <option>Jarras e Suqueiras</option>
            <option>Rechauds</option>
            <option>Samovars</option>
            
            <option>Boleiras Altas</option>
            <option>Boleiras Baixas</option>
            <option>Bandejas Espelhadas</option>
            <option>Bandejas de Cerâmica</option>
            <option>Bandejas de Madeira</option>
            <option>Bandejas de Metal</option>
            <option>Torres de Doces</option>
            <option>Compoteiras</option>
            <option>Bombonieres</option>
            <option>Suportes para Pirulitos</option>
            <option>Bancos de Mesa (Elevação)</option>
            
            <option>Toalhas de Mesa Redondas</option>
            <option>Toalhas de Mesa Retangulares</option>
            <option>Caminhos de Mesa (Trilhos)</option>
            <option>Jogos Americanos</option>
            <option>Guardanapos de Tecido</option>
            <option>Anéis de Guardanapo (Porta-guardanapos)</option>
            <option>Almofadas Decorativas</option>
            <option>Mantas para Sofá</option>
            
            <option>Iluminação</option>
            <option>Varal de Luzes (Gambiarra)</option>
            <option>Lustres de Cristal</option>
            <option>Lustres Rústicos</option>
            <option>Pendentes</option>
            <option>Abajures</option>
            <option>Lanternas Marroquinas</option>
            <option>Castiçais de Vidro</option>
            <option>Castiçais de Metal</option>
            <option>Candelabros</option>
            <option>Velas Decorativas</option>
            <option>Velas de Led</option>
            <option>Letreiros Neon</option>
            <option>Letras Iluminadas (Marquee)</option>
            <option>Refletores de Chão</option>
            <option>Cortinas de Led</option>
            
            <option>Vasos de Chão</option>
            <option>Vasos de Mesa</option>
            <option>Ânforas</option>
            <option>Cachepots</option>
            <option>Arranjos de Flores Naturais</option>
            <option>Arranjos de Flores Artificiais</option>
            <option>Flores Secas e Capim dos Pampas</option>
            <option>Heras e Folhagens Soltas</option>
            <option>Árvores Francesas</option>
            
            <option>Balões de Látex (Lisos)</option>
            <option>Balões Cromados</option>
            <option>Balões Metalizados (Números/Letras)</option>
            <option>Balões Bubble (Transparentes)</option>
            <option>Arcos de Balões Desconstruídos</option>
            <option>Acessórios para Balões</option>
            <option>Gás Hélio</option>
            
            <option>Personagens de Feltro</option>
            <option>Personagens de Biscuit</option>
            <option>Personagens de Resina</option>
            <option>Pelúcias</option>
            <option>Maletas Decorativas</option>
            <option>Gaiolas Decorativas</option>
            <option>Espelhos Decorativos</option>
            <option>Quadros e Molduras</option>
            <option>Lousas e Placas Indicativas</option>
            <option>Bicicletas Decorativas</option>
            <option>Escadas para Lembrancinhas</option>
            
            <option>Topo de Bolo</option>
            <option>Forminhas para Doces</option>
            <option>Tags e Toppers</option>
            <option>Caixas Personalizadas</option>
            <option>Embalagens para Lembrancinhas</option>
            <option>Descartáveis de Luxo</option>
          </select>
        </div>

        <div>
            <label className="block text-sm font-bold text-gray-700">URL da Imagem</label>
            <div className="flex gap-2">
                <input
                    type="text"
                    className="flex-1 p-2 border rounded mt-1 focus:border-castello-red outline-none"
                    placeholder="https://..."
                    value={formData.imageUrl}
                    onChange={e => setFormData({...formData, imageUrl: e.target.value})}
                />
                {formData.imageUrl && (
                    <img src={formData.imageUrl} alt="Preview" className="w-10 h-10 rounded object-cover mt-1 border"/>
                )}
            </div>
        </div>

        <div>
          <label className="block text-sm font-bold text-gray-700">Descrição</label>
          <textarea
            className="w-full p-2 border rounded mt-1 focus:border-castello-red outline-none"
            rows="3"
            value={formData.description}
            onChange={e => setFormData({...formData, description: e.target.value})}
          />
        </div>

        <button
          type="submit"
          disabled={loading}
          className="w-full bg-castello-red text-white py-2 rounded hover:bg-red-800 transition font-bold flex justify-center items-center gap-2"
        >
          <Save size={18} /> {loading ? 'Salvando...' : 'Cadastrar Produto'}
        </button>
      </form>
    </div>
  );
};

export default ManageProducts;