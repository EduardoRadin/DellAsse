import { useState, useEffect } from 'react';
import { ChevronLeft, ChevronRight, Edit, MessageCircle, Copy, Star } from 'lucide-react';
import { useAuth } from '../contexts/AuthContext';
import { partyService } from '../services/partyService';
import { useNavigate } from 'react-router-dom';

const Home = () => {
  const { hasRole, isAuthenticated } = useAuth();
  const navigate = useNavigate();
  
  // Estados para dados reais
  const [parties, setParties] = useState([]);
  const [currentIndex, setCurrentIndex] = useState(0);
  const [loading, setLoading] = useState(true);

  // --- AVALIAÇÕES REAIS DO GOOGLE ---
  const googleReviews = [
    {
      id: 1,
      name: "Daniela Borre",
      rating: 5,
      date: "Há 14 horas",
      text: "Oiii 🌼🌼 Quero registrar aqui o quanto ficamos encantados com a experiência que tivemos no Castelo, onde realizamos a festa de 5 anos da nossa Ísis Mariah. Foi tudo encantador!",
      avatar: "https://ui-avatars.com/api/?name=Daniela+Borre&background=ffe4e6&color=be123c"
    },
    {
      id: 2,
      name: "Caroline Grimaldi",
      rating: 5,
      date: "Há 2 meses",
      text: "Achei maravilhoso do agendamento até o final da festa, decoração montada de acordo com meu gosto, variedade e preço bom nas bebidas e comida maravilhosa. Equipe super dedicada.",
      avatar: "https://ui-avatars.com/api/?name=Caroline+Grimaldi&background=e0f2fe&color=0369a1"
    },
    {
      id: 3,
      name: "User Neca",
      rating: 5,
      date: "Há 1 mês",
      text: "Um ambiente muito aconchegante, acolhedor, pessoas maravilhosas e incríveis no atendimento, com muitas opções de brinquedos e ambientes. Tudo estava perfeito.",
      avatar: "https://ui-avatars.com/api/?name=User+Neca&background=dcfce7&color=15803d"
    },
    {
      id: 4,
      name: "Gisele Dal Magro",
      rating: 5,
      date: "Há 1 mês",
      text: "Atendimento maravilhoso, espaço lindo e com tudo que precisa para realizar o evento. Tudo muito organizado e moderno!",
      avatar: "https://ui-avatars.com/api/?name=Gisele+Dal+Magro&background=f3e8ff&color=7e22ce"
    },
    {
      id: 5,
      name: "Júlia",
      rating: 5,
      date: "Há 2 meses",
      text: "Espaço maravilhoso, atendimento ótimo, tudo perfeito. Comidas muito saborosas, ambiente bacana, não precisei me preocupar com nada, só me arrumei e fui pra festa.",
      avatar: "https://ui-avatars.com/api/?name=Julia&background=ffedd5&color=c2410c"
    },
    {
      id: 6,
      name: "Jean Crespani",
      rating: 5,
      date: "Há 2 meses",
      text: "Lugar maravilhoso, com excelente espaço de festas, diversão para todas as idades, além de ser um lugar bem aconchegante e com boa ventilação, muito bom!",
      avatar: "https://ui-avatars.com/api/?name=Jean+Crespani&background=f1f5f9&color=475569"
    }
  ];

  // Busca as festas ao carregar
  useEffect(() => {
    const fetchParties = async () => {
      try {
        const data = await partyService.getGallery(); 
        
        const completed = Array.isArray(data) 
            ? data.filter(p => p.status === 'COMPLETED') 
            : [];
            
        if (completed.length > 0) {
            setParties(completed);
        }
      } catch (error) {
        console.error("Erro ao carregar festas:", error);
      } finally {
        setLoading(false);
      }
    };
    fetchParties();
  }, []);

  // Navegação do Carrossel
  const nextImage = () => {
    if (parties.length === 0) return;
    setCurrentIndex((prev) => (prev + 1) % parties.length);
  };

  const prevImage = () => {
    if (parties.length === 0) return;
    setCurrentIndex((prev) => (prev - 1 + parties.length) % parties.length);
  };

  const getCoverImage = (party) => {
    if (!party) return null;
    if (party.imgExample) return party.imgExample;
    if (Array.isArray(party.imageUrl) && party.imageUrl.length > 0) {
       return party.imageUrl[0].url || party.imageUrl[0];
    }
    if (typeof party.imageUrl === 'string') return party.imageUrl;
    return "https://placehold.co/600x400?text=Castello+Eventos"; 
  };

  const handleInterest = (party) => {
    if (!party) return;
    const phone = "5549991995065";
    const title = party.title || party.name || "Festa";
    const msg = `Olá! Vi a festa "${title}" na Home do site e tenho interesse.`;
    window.open(`https://wa.me/${phone}?text=${encodeURIComponent(msg)}`, '_blank');
  };

  const handleCopyParty = (party) => {
    navigate('/criar-festa', { state: { partyData: party } });
  };

  if (loading) {
      return <div className="flex justify-center py-20 text-gray-500">Carregando destaques...</div>;
  }

  const displayParties = parties.length > 0 ? parties : [{ 
      id: 0, 
      title: 'Nenhuma festa ainda', 
      theme: 'Aguarde novidades', 
      observations: 'Em breve teremos festas incríveis aqui.', 
      products: [] 
  }];
  
  const currentParty = displayParties[currentIndex];
  const currentImage = getCoverImage(currentParty);

  return (
    <div className="space-y-12 pb-10">
      
      {/* Container Principal (Título + Grid Carrossel) */}
      <div className="space-y-6">
        <h1 className="castello-title text-4xl mb-6">Início</h1>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          
          {/* Left Column */}
          <div className="lg:col-span-2 space-y-6">
            <div className="castello-card p-6">
              <h2 className="castello-title text-3xl mb-4">Sobre nós</h2>
              {/* CORREÇÃO: Nome da empresa ajustado para apenas "Castello" */}
              <p className="text-gray-700 leading-relaxed">
                No Castello, a história encontra os eventos. Somos uma plataforma
                que combina a sofisticação de um castelo histórico com a tecnologia moderna
                para criar eventos inesquecíveis. Nossa missão é tornar o processo de
                planejamento de festas transparente, simples e acessível.
              </p>
            </div>

            <div className="castello-card p-4 relative">
              <div className="relative h-96 bg-gray-200 rounded-lg overflow-hidden group">
                <div className="absolute inset-0 flex items-center justify-center bg-gray-100">
                   {currentImage ? (
                      <img 
                          src={currentImage} 
                          alt={currentParty.title || currentParty.name} 
                          className="w-full h-full object-cover transition-transform duration-700 hover:scale-105"
                      />
                   ) : (
                      <div className="text-center"><span className="text-4xl">🎉</span></div>
                   )}
                   <div className="absolute inset-0 bg-gradient-to-t from-black/70 via-transparent to-transparent"></div>
                </div>

                <div className="absolute bottom-8 left-0 right-0 text-center px-4">
                    <h2 className="text-3xl font-bold text-white drop-shadow-md">
                      {currentParty.title || currentParty.name || "Sem Título"}
                    </h2>
                </div>

                {displayParties.length > 1 && (
                  <>
                      <button onClick={prevImage} className="absolute left-4 top-1/2 -translate-y-1/2 bg-white/30 hover:bg-white/90 text-white hover:text-gray-800 backdrop-blur-sm rounded-full p-2 shadow-lg transition opacity-0 group-hover:opacity-100">
                          <ChevronLeft size={24} />
                      </button>
                      <button onClick={nextImage} className="absolute right-4 top-1/2 -translate-y-1/2 bg-white/30 hover:bg-white/90 text-white hover:text-gray-800 backdrop-blur-sm rounded-full p-2 shadow-lg transition opacity-0 group-hover:opacity-100">
                          <ChevronRight size={24} />
                      </button>
                  </>
                )}
              </div>
            </div>
          </div>

          {/* Right Column - Card de Detalhes */}
          <div className="lg:col-span-1">
            <div className="castello-card p-6 h-full flex flex-col">
              
              <h3 className="text-xl font-bold text-gray-900 mb-1">
                 {currentParty.title || currentParty.name || "Detalhes da Festa"}
              </h3>

              <div className="text-sm text-gray-600 mb-4 line-clamp-3 italic bg-gray-50 p-2 rounded border border-gray-100">
                 "{currentParty.observations || currentParty.observation || "Uma celebração única realizada com a excelência Castello."}"
              </div>

              <div className="flex-1 mb-6">
                  <h4 className="text-xs font-bold text-gray-400 uppercase tracking-wider mb-2">
                      Destaques do Evento:
                  </h4>
                  <ul className="space-y-2 text-sm text-gray-700">
                  {currentParty.products && currentParty.products.length > 0 ? (
                      currentParty.products.slice(0, 5).map((prod, idx) => (
                          <li key={idx} className="flex items-center gap-2">
                              <span className="w-1.5 h-1.5 bg-castello-red rounded-full"></span>
                              {prod.name}
                          </li>
                      ))
                  ) : (
                      <li className="text-gray-400 text-xs">Itens sob consulta</li>
                  )}
                  </ul>
              </div>

              <div className="space-y-3 mt-auto">
                {hasRole('ADMIN') && (
                  <button 
                      onClick={() => navigate(`/admin/festa/${currentParty.id}`)} 
                      className="w-full px-4 py-2 bg-green-500 text-white rounded-lg hover:bg-green-600 transition flex items-center justify-center space-x-2"
                  >
                    <Edit size={18} />
                    <span>Editar Festa</span>
                  </button>
                )}
                
                {isAuthenticated() && (
                  <button 
                      onClick={() => handleCopyParty(currentParty)}
                      className="w-full px-4 py-2 bg-castello-red text-white rounded-lg hover:bg-red-800 transition flex items-center justify-center space-x-2 shadow-md"
                  >
                    <Copy size={18} />
                    <span>Quero Igual</span>
                  </button>
                )}

                <button 
                  onClick={() => handleInterest(currentParty)}
                  className={`w-full px-4 py-2 text-white rounded-lg transition flex items-center justify-center space-x-2 shadow-md ${isAuthenticated() ? 'bg-green-600 hover:bg-green-700' : 'bg-castello-red hover:bg-red-800'}`}
                >
                  <MessageCircle size={18} />
                  <span>Tenho interesse</span>
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* --- AVALIAÇÕES DO GOOGLE REAIS --- */}
      <div className="border-t border-gray-200 pt-10">
        <div className="flex items-center justify-between mb-8">
            <div>
                <h2 className="castello-title text-3xl">O que dizem nossos clientes</h2>
                <div className="flex items-center gap-2 mt-1">
                    <span className="text-yellow-500 font-bold text-lg">5.0</span>
                    <div className="flex text-yellow-400">
                        {[...Array(5)].map((_, i) => <Star key={i} size={16} fill="currentColor" />)}
                    </div>
                    <span className="text-gray-400 text-sm ml-1">no Google Reviews</span>
                </div>
            </div>
            
            <a 
                href="https://www.google.com/search?sca_esv=40968150e120a79c&sxsrf=AE3TifNv3cBWkTbB-vwqT32dEgRv5nyZTw:1763963302859&si=AMgyJEtREmoPL4P1I5IDCfuA8gybfVI2d5Uj7QMwYCZHKDZ-E2_Gg2tkQWQ7EZh6qbu10_d2miJN-DwZOaFKaqznMPuMItgfR9xAkSACaQzRfamPfKEWzmqkl9N6fE7zBTAqZJAO4xugmIU72JKHPfh4jsTOIe234Q%3D%3D&q=Castello+Festas+e+Eventos+Reviews&sa=X&ved=2ahUKEwjygcGwi4qRAxXAKLkGHTqwMhsQ0bkNegQIOhAD&biw=1920&bih=955&dpr=1#" // Coloque o link do maps aqui
                target="_blank" 
                rel="noreferrer"
                className="hidden sm:flex items-center text-blue-600 hover:text-blue-800 text-sm font-semibold transition"
            >
                Ver todas as avaliações →
            </a>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {googleReviews.map((review) => (
                <div key={review.id} className="bg-white p-6 rounded-xl shadow-sm border border-gray-100 hover:shadow-md transition duration-300 flex flex-col">
                    <div className="flex items-center gap-3 mb-4">
                        <img 
                            src={review.avatar} 
                            alt={review.name} 
                            className="w-10 h-10 rounded-full bg-gray-100 shadow-sm"
                        />
                        <div>
                            <h4 className="font-bold text-gray-800 text-sm">{review.name}</h4>
                            <div className="flex items-center gap-2">
                                <div className="flex text-yellow-400">
                                    {[...Array(review.rating)].map((_, i) => (
                                        <Star key={i} size={12} fill="currentColor" />
                                    ))}
                                </div>
                                <span className="text-xs text-gray-400">{review.date}</span>
                            </div>
                        </div>
                    </div>
                    <p className="text-gray-600 text-sm leading-relaxed flex-1 italic">
                        "{review.text}"
                    </p>
                    <div className="mt-4 pt-4 border-t border-gray-50 flex items-center gap-1">
                        {/* CORREÇÃO: URL oficial do logo do Google SVG */}
                        <img src="https://www.svgrepo.com/show/303108/google-icon-logo.svg" alt="Google" className="w-4 h-4" />
                        <span className="text-[10px] text-gray-400 font-medium">Postado no Google</span>
                    </div>
                </div>
            ))}
        </div>
      </div>

    </div>
  );
};

export default Home;