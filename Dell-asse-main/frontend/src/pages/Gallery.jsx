import { useState, useEffect } from 'react';
import { Filter, ChevronLeft, ChevronRight, Copy, MessageCircle, LogIn, Heart } from 'lucide-react';
import { useAuth } from '../contexts/AuthContext';
import { partyService } from '../services/partyService'; 
import { useNavigate } from 'react-router-dom';

const Gallery = () => {
  const navigate = useNavigate();
  const { isAuthenticated } = useAuth();
  const [currentImageIndex, setCurrentImageIndex] = useState({});
  
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(false);

  useEffect(() => {
    const fetchParties = async () => {
      try {
        const data = await partyService.getGallery();        
        // FILTRAGEM NO FRONTEND: Apenas festas "COMPLETED"
        const completedParties = Array.isArray(data) 
            ? data.filter(p => p.status === 'COMPLETED') 
            : [];
            
        console.log("Festas carregadas (Filtradas):", completedParties);
        setItems(completedParties);
      } catch (error) {
        console.error("Erro ao buscar festas:", error);
        setError(true);
      } finally {
        setLoading(false);
      }
    };

    fetchParties();
  }, []); 

  // Funções de manipulação de imagem
  const getImageCount = (item) => {
    if (Array.isArray(item.imageUrl)) return item.imageUrl.length;
    return 1; 
  };

  const nextImage = (itemId, totalImages) => {
    if (totalImages <= 1) return;
    setCurrentImageIndex((prev) => ({
      ...prev,
      [itemId]: ((prev[itemId] || 0) + 1) % totalImages,
    }));
  };

  const prevImage = (itemId, totalImages) => {
    if (totalImages <= 1) return;
    setCurrentImageIndex((prev) => ({
      ...prev,
      [itemId]: ((prev[itemId] || 0) - 1 + totalImages) % totalImages,
    }));
  };

  const getItemImage = (item, index = 0) => {
    if (item.imgExample) return item.imgExample;
    if (Array.isArray(item.imageUrl) && item.imageUrl.length > 0) {
      const validIndex = index % item.imageUrl.length;
      return item.imageUrl[validIndex].url || item.imageUrl[validIndex];
    }
    if (typeof item.imageUrl === 'string') return item.imageUrl;
    return null;
  };

  // --- Ação: Tenho Interesse (WHATSAPP) ---
  const handleInterest = (item) => {
    const phoneNumber = "5549991995065"; // Código do país (55) + DDD + Número
    const title = item.title || item.name || "Sem Título";
    const id = item.id || "";
    
    const message = `Tenho interesse na festa ${id} ${title}`;
    const whatsappUrl = `https://wa.me/${phoneNumber}?text=${encodeURIComponent(message)}`;
    
    window.open(whatsappUrl, '_blank');
  };

  // --- Ação: Gostaria de uma parecida ---
  const handleCopyParty = (item) => {
    if (!isAuthenticated()) {
      navigate('/login', { state: { from: '/criar-festa', partyData: item } });
    } else {
      navigate('/criar-festa', { state: { partyData: item } });
    }
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center h-64">
        <div className="text-castello-red font-bold animate-pulse">Carregando galeria...</div>
      </div>
    );
  }

  return (
    <div className="space-y-6 container mx-auto px-4 py-8">
      {/* Header */}
      <div className="flex flex-col md:flex-row justify-between items-center gap-4">
        <div>
          <h1 className="castello-title text-4xl">
             Galeria de Inspirações
          </h1>
          <p className="text-gray-600 mt-2">
            Veja as festas incríveis que já realizamos
          </p>
        </div>
        
        <button className="flex items-center space-x-2 px-4 py-2 bg-gray-100 border border-gray-200 rounded-lg hover:bg-gray-200 transition text-gray-600">
          <Filter size={18} />
          <span>Filtro</span>
        </button>
      </div>

      {/* Estado Vazio ou Erro */}
      {items.length === 0 ? (
        <div className="text-center py-20 bg-gray-50 rounded-xl border border-dashed border-gray-300">
          <div className="inline-block p-4 bg-white rounded-full shadow-sm mb-4">
             {error ? <LogIn size={32} className="text-gray-400"/> : <Heart size={32} className="text-gray-400"/>}
          </div>
          <h3 className="text-xl font-bold text-gray-800 mb-2">
            {error ? "Acesso Restrito" : "Nenhuma festa encontrada"}
          </h3>
          <p className="text-gray-500 max-w-md mx-auto mb-6">
            {error 
              ? "Não foi possível carregar a galeria pública." 
              : "No momento não há festas concluídas na galeria."}
          </p>
          
          {!isAuthenticated() && (
             <button 
                onClick={() => navigate('/login')}
                className="px-6 py-2 bg-castello-red text-white rounded-lg hover:bg-red-800 transition font-bold shadow-md"
             >
                Fazer Login
             </button>
          )}
        </div>
      ) : (
        /* Grid de Cards */
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
          {items.map((item) => {
            const itemId = item.id || item.title || item.name;
            const currentIndex = currentImageIndex[itemId] || 0;
            const totalImages = getImageCount(item);
            const currentImageUrl = getItemImage(item, currentIndex);
            const displayTitle = item.title || item.name || "Sem Título";

            return (
              <div key={itemId} className="castello-card overflow-hidden flex flex-col h-full bg-white rounded-xl shadow-lg hover:shadow-xl transition-all duration-300 group/card">
                
                {/* Área da Imagem */}
                <div className="relative h-64 bg-gray-100 flex items-center justify-center overflow-hidden">
                  {currentImageUrl ? (
                     <img 
                       src={currentImageUrl} 
                       alt={displayTitle} 
                       className="absolute inset-0 w-full h-full object-cover transition-transform duration-700 group-hover/card:scale-105"
                       onError={(e) => {
                         e.target.onerror = null;
                         e.target.src = "https://placehold.co/400x300?text=Sem+Imagem";
                       }}
                     />
                  ) : (
                    <div className="text-center z-10 p-4">
                      <span className="text-4xl block mb-2">🎉</span>
                      <span className="text-xs text-gray-500 font-medium">Sem prévia</span>
                    </div>
                  )}

                  <div className="absolute top-3 right-3 bg-green-500 text-white text-[10px] font-bold px-2 py-1 rounded-full uppercase tracking-wider shadow-sm">
                    Concluída
                  </div>

                  {/* Controles de Carrossel */}
                  {totalImages > 1 && (
                    <>
                      <button onClick={() => prevImage(itemId, totalImages)} className="absolute left-2 top-1/2 -translate-y-1/2 bg-white/80 hover:bg-white rounded-full p-2 shadow-sm opacity-0 group-hover/card:opacity-100 transition-opacity">
                        <ChevronLeft size={16} />
                      </button>
                      <button onClick={() => nextImage(itemId, totalImages)} className="absolute right-2 top-1/2 -translate-y-1/2 bg-white/80 hover:bg-white rounded-full p-2 shadow-sm opacity-0 group-hover/card:opacity-100 transition-opacity">
                        <ChevronRight size={16} />
                      </button>
                      <div className="absolute bottom-2 left-1/2 -translate-x-1/2 bg-black/50 px-2 py-0.5 rounded-full text-white text-[10px] backdrop-blur-sm">
                        {currentIndex + 1}/{totalImages}
                      </div>
                    </>
                  )}
                </div>

                {/* Conteúdo */}
                <div className="p-5 flex flex-col flex-1">
                  <div className="mb-4">
                    <h3 className="font-bold text-xl text-gray-800 leading-tight mb-2">
                      {displayTitle}
                    </h3>
                    <p className="text-sm text-gray-600 line-clamp-2">
                      {item.observation || item.observations || "Uma festa inesquecível realizada pela Castello."}
                    </p>
                  </div>

                  {item.products && item.products.length > 0 && (
                      <div className="mb-4 p-3 bg-gray-50 rounded-lg border border-gray-100">
                          <span className="text-[10px] font-bold text-gray-400 uppercase tracking-wider mb-1 block">Destaques:</span>
                          <div className="flex flex-wrap gap-1">
                              {item.products.slice(0, 3).map((p, i) => (
                                  <span key={i} className="text-xs bg-white border border-gray-200 px-2 py-0.5 rounded text-gray-600 truncate max-w-[100px]">
                                    {p.name}
                                  </span>
                              ))}
                              {item.products.length > 3 && <span className="text-xs text-gray-400 pl-1">+{item.products.length - 3}</span>}
                          </div>
                      </div>
                  )}

                  <div className={`mt-auto grid gap-3 pt-2 ${isAuthenticated() ? 'grid-cols-2' : 'grid-cols-1'}`}>
                    
                    {/* Botão Tenho Interesse (WhatsApp) - SEMPRE VISÍVEL */}
                    <button 
                      onClick={() => handleInterest(item)}
                      className="flex items-center justify-center gap-2 px-3 py-2.5 bg-green-600 text-white rounded-lg hover:bg-green-700 transition text-xs font-bold uppercase tracking-wide shadow-sm"
                      title="Falar no WhatsApp"
                    >
                      <MessageCircle size={16} />
                      <span>Tenho Interesse</span>
                    </button>

                    {/* Botão Quero uma Parecida - SÓ VISÍVEL SE LOGADO */}
                    {isAuthenticated() && (
                      <button 
                        onClick={() => handleCopyParty(item)}
                        className="flex items-center justify-center gap-2 px-3 py-2.5 bg-castello-red text-white rounded-lg hover:bg-red-800 transition text-xs font-bold uppercase tracking-wide shadow-sm"
                        title="Usar como base para minha festa"
                      >
                        <Copy size={16} />
                        <span>Quero Igual</span>
                      </button>
                    )}
                  </div>
                </div>
              </div>
            );
          })}
        </div>
      )}
    </div>
  );
};

export default Gallery;