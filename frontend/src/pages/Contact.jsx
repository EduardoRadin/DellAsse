import { Phone, Mail, Instagram, Facebook } from 'lucide-react';

const Contact = () => {
  return (
    <div className="space-y-6">
      <h1 className="castello-title text-4xl mb-8">Contato</h1>

      {/* Grid Layout: 1 coluna no mobile, 2 colunas no desktop (lg) */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 max-w-6xl mx-auto">
        
        {/* COLUNA DA ESQUERDA: Informações de Contato */}
        <div className="space-y-6">
          <div className="castello-card p-8 space-y-6 h-full">
            <h2 className="text-2xl font-bold text-gray-900 mb-4">Fale Conosco</h2>
            
            {/* Phone */}
            <div className="flex items-center space-x-4">
              <div className="w-12 h-12 bg-green-500 rounded-full flex items-center justify-center flex-shrink-0">
                <Phone size={24} className="text-white" />
              </div>
              <div>
                <p className="text-sm text-gray-500 font-semibold">Telefone / WhatsApp</p>
                <a
                  href="tel:+5549991995065"
                  className="text-lg text-gray-700 hover:text-castello-red transition font-medium"
                >
                  (49) 9 9199-5065
                </a>
              </div>
            </div>

            {/* Email */}
            <div className="flex items-center space-x-4">
              <div className="w-12 h-12 bg-red-500 rounded-full flex items-center justify-center flex-shrink-0">
                <Mail size={24} className="text-white" />
              </div>
              <div>
                <p className="text-sm text-gray-500 font-semibold">E-mail</p>
                <a
                  href="mailto:castellodelasse@gmail.com"
                  className="text-lg text-gray-700 hover:text-castello-red transition break-all"
                >
                  castellodelasse@gmail.com
                </a>
              </div>
            </div>

            {/* Instagram */}
            <div className="flex items-center space-x-4">
              <div className="w-12 h-12 bg-gradient-to-br from-purple-500 via-pink-500 to-orange-500 rounded-full flex items-center justify-center flex-shrink-0">
                <Instagram size={24} className="text-white" />
              </div>
              <div>
                <p className="text-sm text-gray-500 font-semibold">Instagram</p>
                <a
                  href="https://www.instagram.com/castellofestaseeventos/"
                  target="_blank"
                  rel="noopener noreferrer"
                  className="text-lg text-gray-700 hover:text-castello-red transition"
                >
                  @castellodelasse
                </a>
              </div>
            </div>

            {/* Facebook */}
            <div className="flex items-center space-x-4">
              <div className="w-12 h-12 bg-blue-600 rounded-full flex items-center justify-center flex-shrink-0">
                <Facebook size={24} className="text-white" />
              </div>
              <div>
                <p className="text-sm text-gray-500 font-semibold">Facebook</p>
                <a
                  href="https://www.facebook.com/p/Castello-Festas-Eventos-100063021201723/"
                  target="_blank"
                  rel="noopener noreferrer"
                  className="text-lg text-gray-700 hover:text-castello-red transition"
                >
                  Castello Del'asse
                </a>
              </div>
            </div>

            {/* Separator */}
            <hr className="border-gray-200 my-6" />

            {/* Operating Hours */}
            <div>
              <h3 className="font-bold text-gray-900 mb-2">Horário de Atendimento</h3>
              <p className="text-gray-600">Segunda a Domingo</p>
              <p className="text-gray-600">08:00 - 11:30 | 13:30 - 17:30</p>
            </div>

            {/* Call to Action Button */}
            <div className="pt-4">
              <a 
                href='https://wa.me/5549991995065' 
                target="_blank" 
                rel="noopener noreferrer"
                className="block w-full bg-castello-red text-white text-center py-3 rounded-lg font-bold hover:bg-red-800 transition shadow-md"
              >
                Agende já uma visita!
              </a>
            </div>
          </div>
        </div>

        {/* COLUNA DA DIREITA: Mapa e Imagem */}
        <div className="space-y-6 flex flex-col h-full">
          
          {/* Map Container */}
          <div className="castello-card overflow-hidden h-80 shadow-md flex-shrink-0">
            <iframe 
              src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d755.3968497566995!2d-53.20117408477768!3d-26.773083081790908!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x94fae5a57f34006d%3A0x775c9c7cddb639d4!2sCastello%20Festas%20e%20Eventos!5e0!3m2!1spt-BR!2sbr!4v1763690511439!5m2!1spt-BR!2sbr" 
              width="100%" 
              height="100%" 
              style={{ border: 0 }} 
              allowFullScreen="" 
              loading="lazy" 
              referrerPolicy="no-referrer-when-downgrade"
              className="w-full h-full"
            ></iframe>
          </div>

          {/* Building Image Container */}
          <div className="castello-card overflow-hidden flex-grow min-h-[320px] shadow-md">
            <img 
              src="imgs/fachada-castello.jpg" 
              alt="Fachada do Castello Del'asse" 
              className="w-full h-full object-cover hover:scale-105 transition-transform duration-700" 
            />
          </div>

        </div>

      </div>
    </div>
  );
};

export default Contact;