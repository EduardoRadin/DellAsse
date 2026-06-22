import { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { Plus, Minus, Send, Image as ImageIcon, DollarSign, ArrowLeft, Trash2, Search, X, Package } from 'lucide-react';
import { partyService } from '../services/partyService';
import { productService } from '../services/productService';
import { getApiErrorMessage } from '../utils/apiError';

const CreateCustomParty = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const [loading, setLoading] = useState(false);
  const [budgetLoading, setBudgetLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');
  const [budgetPreview, setBudgetPreview] = useState(null);
  
  // Estados para seleção de produtos
  const [availableProducts, setAvailableProducts] = useState([]);
  const [isProductModalOpen, setIsProductModalOpen] = useState(false);
  const [searchTerm, setSearchTerm] = useState('');

  const [showUrlInput, setShowUrlInput] = useState(false);
  const [items, setItems] = useState([]);
  
  const [formData, setFormData] = useState({
    title: 'Minha Festa Personalizada',
    description: '',
    generateBudget: '',
    observations: '',
    imageURL: '',
    products: []
  });

  // Carregar dados iniciais e produtos do banco
  useEffect(() => {
    // Busca produtos do backend
    const fetchProducts = async () => {
        try {
            const data = await productService.getAll();
            setAvailableProducts(data);
        } catch (error) {
            console.error("Erro ao buscar produtos", error);
        }
    };
    fetchProducts();

    if (location.state && location.state.partyData) {
      const sourceParty = location.state.partyData;
      
      setFormData({
        title: `Personalizando: ${sourceParty.title || sourceParty.name}`,
        description: sourceParty.description || '',
        generateBudget: sourceParty.generateBudget || '',
        observations: sourceParty.observations || sourceParty.observation || '', 
        imageURL: getImageUrl(sourceParty), 
        products: []
      });

      if (sourceParty.products && sourceParty.products.length > 0) {
        const mappedItems = sourceParty.products.map(prod => ({
            id: Date.now() + Math.random(),
            dbId: prod.id || null, 
            name: prod.name,
            quantity: 1,
            price: prod.price || 0,
            stockMax: prod.stockQuantity || 999, 
            isDbItem: true // Marca como item de banco
        }));
        setItems(mappedItems);
      }
    }
  }, [location]);

  const getImageUrl = (party) => {
    if (party.imgExample) return party.imgExample;
    if (Array.isArray(party.imageUrl) && party.imageUrl.length > 0) return party.imageUrl[0].url || party.imageUrl[0];
    if (typeof party.imageUrl === 'string') return party.imageUrl;
    return '';
  };

  // Adicionar item do banco de dados
  const handleAddProductFromDb = (product) => {
    const exists = items.find(i => i.dbId === product.id);
    if (exists) {
        alert('Produto já adicionado à lista!');
        return;
    }

    setBudgetPreview(null);
    setItems([
        ...items, 
        { 
            id: Date.now(), 
            dbId: product.id,
            name: product.name, 
            quantity: 1, 
            price: product.price,
            stockMax: product.stockQuantity,
            isDbItem: true 
        }
    ]);
    setIsProductModalOpen(false);
    setSearchTerm('');
  };

  const handleAddManualItem = () => {
    setItems([
        ...items, 
        { id: Date.now(), name: '', quantity: 1, isDbItem: false, stockMax: 999 }
    ]);
  };

  const handleRemoveItem = (id) => {
    setBudgetPreview(null);
    setItems(items.filter(item => item.id !== id));
  };

  const handleQuantityChange = (id, delta) => {
    setItems(items.map(item => {
      if (item.id === id) {
        const newQty = item.quantity + delta;
        if (newQty < 1) return item;
        if (item.isDbItem && newQty > item.stockMax) {
            alert(`Estoque máximo disponível: ${item.stockMax}`);
            return item;
        }
        return { ...item, quantity: newQty };
      }
      return item;
    }));
  };

  const handleNameChange = (id, newName) => {
    setItems(items.map(item => 
      item.id === id ? { ...item, name: newName } : item
    ));
  };

  const getCatalogProductIds = () =>
    items
      .filter((item) => item.isDbItem && item.dbId)
      .map((item) => item.dbId);

  const handleCalculateBudget = async () => {
    const productIds = getCatalogProductIds();

    if (productIds.length === 0) {
      setErrorMessage('Adicione pelo menos um produto do catálogo para calcular o orçamento.');
      setBudgetPreview(null);
      return;
    }

    setBudgetLoading(true);
    setErrorMessage('');

    try {
      const budget = await partyService.previewBudget(productIds);
      setBudgetPreview(budget);
      setFormData((prev) => ({
        ...prev,
        generateBudget: budget.total.toFixed(2),
      }));
    } catch (error) {
      setBudgetPreview(null);
      setErrorMessage(
        error.userMessage || getApiErrorMessage(error, 'Não foi possível calcular o orçamento.')
      );
    } finally {
      setBudgetLoading(false);
    }
  };

  const handleSubmit = async () => {
    setLoading(true);
    setErrorMessage('');
    try {
      const validItems = items.filter(i => i.name.trim() !== '');
      
      let itemsList = "Nenhum item listado.";
      if (validItems.length > 0) {
        itemsList = validItems.map(i => {
            return `${i.quantity}x ${i.name} ${i.isDbItem ? '(Do Estoque)' : '(Personalizado)'}`;
        }).join(', ');
      }

      const descriptionField = `[ITENS SOLICITADOS: ${itemsList}] ${formData.description ? '\n' + formData.description : ''}`;

      // Pega IDs dos produtos do banco
      const productIds = validItems
        .filter(item => item.isDbItem && item.dbId)
        .map(item => item.dbId);

      const payload = {
        ...formData, 
        
        
        description: descriptionField, 
        
        observations: formData.observations, 
        
        generateBudget: formData.generateBudget ? parseFloat(formData.generateBudget) : 0,
        products: productIds, 
        galleryId: location.state?.partyData?.id || null 
      };

      await partyService.create(payload);
      alert('Solicitação enviada com sucesso!');
      navigate('/carrinho');
    } catch (error) {
      console.error(error);
      setErrorMessage(
        error.userMessage || getApiErrorMessage(error, 'Erro ao enviar solicitação.')
      );
    } finally {
      setLoading(false);
    }
  };

  // Filtro do modal
  const filteredProducts = availableProducts.filter(p => 
      p.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
      p.category?.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="space-y-6 relative">
      <div className="flex items-center space-x-4 mb-6">
        <button onClick={() => navigate('/carrinho')} className="p-2 hover:bg-gray-200 rounded-full transition">
            <ArrowLeft size={24} />
        </button>
        <h1 className="castello-title text-3xl">
            {location.state?.partyData ? 'Editar Festa' : 'Solicitar Festa Personalizada'}
        </h1>
      </div>

      {errorMessage && (
        <div className="max-w-5xl mx-auto bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded">
          {errorMessage}
        </div>
      )}

      <div className="castello-card p-8 max-w-5xl mx-auto">
        <div className="flex flex-col lg:flex-row gap-8">
            
          {/* Coluna Esquerda */}
          <div className="flex-1 space-y-6">
            <div>
                <label className="block font-bold text-gray-900 mb-2">Nome da Festa</label>
                <input 
                    type="text" 
                    value={formData.title}
                    onChange={(e) => setFormData({...formData, title: e.target.value})}
                    className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-castello-red outline-none"
                    placeholder="Ex: Aniversário de 15 anos"
                />
            </div>

            {/* Lista de Itens */}
            <div className="bg-gray-50 p-4 rounded-lg border border-gray-200">
                <div className="flex justify-between items-center mb-4">
                    <h3 className="font-bold text-gray-700">Itens Selecionados</h3>
                    <button
                        onClick={handleCalculateBudget}
                        disabled={budgetLoading}
                        className="text-xs text-castello-red hover:underline disabled:opacity-50"
                    >
                        {budgetLoading ? 'Calculando...' : 'Calcular Orçamento (API)'}
                    </button>
                </div>
                
                {items.length === 0 ? (
                    <p className="text-sm text-gray-500 text-center py-4 italic">
                        Sua lista está vazia.
                    </p>
                ) : (
                    <div className="space-y-3">
                        {items.map((item) => (
                            <div key={item.id} className={`flex items-center gap-3 p-2 rounded border shadow-sm ${item.isDbItem ? 'bg-blue-50 border-blue-100' : 'bg-white border-gray-100'}`}>
                                {item.isDbItem && <Package size={16} className="text-blue-500" title="Item do Estoque"/>}
                                
                                <input 
                                    type="text" 
                                    value={item.name}
                                    onChange={(e) => handleNameChange(item.id, e.target.value)}
                                    readOnly={item.isDbItem} // Bloqueia edição de nome se veio do banco
                                    className={`flex-1 px-2 py-1 border-b border-transparent outline-none text-gray-700 bg-transparent ${!item.isDbItem && 'focus:border-castello-red'}`}
                                />
                                
                                <div className="flex items-center space-x-2">
                                    <button onClick={() => handleQuantityChange(item.id, -1)} className="w-6 h-6 bg-gray-200 rounded flex items-center justify-center hover:bg-gray-300 text-gray-600"><Minus size={12} /></button>
                                    <span className="w-6 text-center font-bold text-sm">{item.quantity}</span>
                                    <button onClick={() => handleQuantityChange(item.id, 1)} className="w-6 h-6 bg-gray-200 rounded flex items-center justify-center hover:bg-gray-300 text-gray-600"><Plus size={12} /></button>
                                </div>

                                <button onClick={() => handleRemoveItem(item.id)} className="p-1 text-red-400 hover:text-red-600 rounded"><Trash2 size={16} /></button>
                            </div>
                        ))}
                    </div>
                )}

                <div className="pt-4 flex gap-2 justify-center">
                    {/* Botão Catálogo */}
                    <button 
                        onClick={() => setIsProductModalOpen(true)}
                        className="text-sm bg-castello-dark text-white px-4 py-2 rounded hover:bg-gray-800 transition flex items-center gap-2"
                    >
                        <Search size={14} /> Buscar no Catálogo
                    </button>

                    {/* Botão Manual */}
                    <button 
                        onClick={handleAddManualItem}
                        className="text-sm text-gray-600 border border-dashed border-gray-400 px-4 py-2 rounded hover:border-gray-600 hover:bg-gray-100 transition flex items-center gap-1"
                    >
                        <Plus size={14} /> Item Manual
                    </button>
                </div>
            </div>
          </div>

          {/* Coluna Direita (Mantida igual, só o botão URL input que já existia) */}
          <div className="lg:w-72 flex flex-col space-y-4">
            <button 
                onClick={handleSubmit}
                disabled={loading}
                className="w-full py-3 bg-castello-red text-white rounded-lg hover:bg-red-800 transition flex items-center justify-center space-x-2 font-bold shadow-md"
            >
                {loading ? <span>Enviando...</span> : <><Send size={18} /><span>Enviar Solicitação</span></>}
            </button>
            
            {!showUrlInput && !formData.imageURL && (
                <button onClick={() => setShowUrlInput(true)} className="w-full py-3 bg-white border-2 border-castello-red text-castello-red rounded-lg hover:bg-red-50 transition flex items-center justify-center space-x-2 font-bold shadow-sm">
                    <ImageIcon size={18} /><span>Adicionar imagem</span>
                </button>
            )}

            <div className="bg-white border-2 border-castello-red rounded-lg p-3 shadow-sm">
                <div className="flex items-center justify-between mb-1">
                    <label className="text-xs font-bold text-castello-red uppercase flex items-center gap-1"><DollarSign size={14} /> Orçamento Estimado</label>
                </div>
                <input 
                    type="number" 
                    value={formData.generateBudget}
                    onChange={(e) => setFormData({...formData, generateBudget: e.target.value})}
                    className="w-full text-2xl font-bold text-gray-800 border-b border-gray-300 focus:border-castello-red outline-none py-1"
                    placeholder="0,00"
                />
                {budgetPreview && (
                    <div className="mt-3 pt-3 border-t border-gray-100 text-xs text-gray-600 space-y-1">
                        <div className="flex justify-between">
                            <span>Subtotal</span>
                            <span>R$ {budgetPreview.subtotal.toFixed(2)}</span>
                        </div>
                        {budgetPreview.discountAmount > 0 && (
                            <>
                                {budgetPreview.discounts?.map((discount) => (
                                    <div key={discount.name} className="flex justify-between text-green-700">
                                        <span>{discount.name}</span>
                                        <span>- R$ {discount.amount.toFixed(2)}</span>
                                    </div>
                                ))}
                                <div className="flex justify-between font-semibold text-green-800">
                                    <span>Total com desconto</span>
                                    <span>R$ {budgetPreview.total.toFixed(2)}</span>
                                </div>
                            </>
                        )}
                    </div>
                )}
            </div>

            {showUrlInput && (
                <div className="bg-white p-2 border border-gray-300 rounded-lg">
                    <div className="flex gap-2">
                        <input type="text" placeholder="https://..." className="flex-1 border px-2 py-1 text-sm" onBlur={(e) => { if(e.target.value) { setFormData({...formData, imageURL: e.target.value}); setShowUrlInput(false); }}} />
                        <button onClick={() => setShowUrlInput(false)} className="text-gray-400 hover:text-red-500"><X size={16} /></button>
                    </div>
                </div>
            )}

            {formData.imageURL && (
                <div className="relative w-full h-40 rounded-lg overflow-hidden border border-gray-200 group">
                    <img src={formData.imageURL} alt="Inspiração" className="w-full h-full object-cover" />
                    <button onClick={() => setFormData({...formData, imageURL: ''})} className="absolute top-2 right-2 bg-white text-red-500 p-1 rounded-full shadow-md"><Trash2 size={14} /></button>
                </div>
            )}
          </div>
        </div>

        {/* Campo Observações */}
        <div className="mt-8">
            <label className="block font-bold text-gray-900 mb-2">Observações:</label>
            <textarea
                value={formData.observations}
                onChange={(e) => setFormData({...formData, observations: e.target.value})}
                className="w-full h-32 p-4 border border-gray-300 rounded-lg resize-none focus:ring-2 focus:ring-castello-red bg-white"
            />
        </div>
      </div>

      {/* MODAL DE SELEÇÃO DE PRODUTOS */}
      {isProductModalOpen && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
            <div className="bg-white rounded-lg w-full max-w-2xl max-h-[80vh] flex flex-col shadow-2xl animate-in zoom-in-95">
                <div className="p-4 border-b flex justify-between items-center">
                    <h3 className="text-lg font-bold text-gray-800">Catálogo de Produtos</h3>
                    <button onClick={() => setIsProductModalOpen(false)} className="text-gray-500 hover:text-gray-800"><X size={24} /></button>
                </div>
                
                <div className="p-4 bg-gray-50">
                    <div className="relative">
                        <Search className="absolute left-3 top-3 text-gray-400" size={18} />
                        <input 
                            type="text" 
                            placeholder="Buscar produtos (ex: Mesa, Cadeira...)" 
                            className="w-full pl-10 pr-4 py-2 border rounded-lg focus:ring-2 focus:ring-castello-red outline-none"
                            value={searchTerm}
                            onChange={(e) => setSearchTerm(e.target.value)}
                            autoFocus
                        />
                    </div>
                </div>

                <div className="overflow-y-auto flex-1 p-4 space-y-2">
                    {loading ? <p className="text-center">Carregando...</p> : 
                     filteredProducts.length === 0 ? (
                        <p className="text-center text-gray-500 py-8">Nenhum produto encontrado.</p>
                     ) : (
                        filteredProducts.map(product => (
                            <div key={product.id} className="flex items-center justify-between p-3 bg-white border rounded hover:shadow-md transition group">
                                <div className="flex items-center gap-3">
                                    {product.imageUrl ? 
                                        <img src={product.imageUrl} alt={product.name} className="w-12 h-12 object-cover rounded" /> :
                                        <div className="w-12 h-12 bg-gray-200 rounded flex items-center justify-center text-gray-400"><ImageIcon size={20}/></div>
                                    }
                                    <div>
                                        <p className="font-bold text-gray-800">{product.name}</p>
                                        <p className="text-xs text-gray-500">
                                            Estoque: {product.stockQuantity} | Categoria: {product.category}
                                        </p>
                                    </div>
                                </div>
                                <div className="flex items-center gap-4">
                                    <span className="font-semibold text-green-600">
                                        R$ {product.price?.toFixed(2)}
                                    </span>
                                    <button 
                                        onClick={() => handleAddProductFromDb(product)}
                                        disabled={product.stockQuantity <= 0}
                                        className="px-3 py-1 bg-castello-red text-white text-sm rounded hover:bg-red-800 disabled:bg-gray-300 disabled:cursor-not-allowed"
                                    >
                                        {product.stockQuantity > 0 ? 'Adicionar' : 'Esgotado'}
                                    </button>
                                </div>
                            </div>
                        ))
                     )}
                </div>
            </div>
        </div>
      )}
    </div>
  );
};

export default CreateCustomParty;