import { useState, useRef } from 'react';
import { Upload, Check, X, Eye } from 'lucide-react';
// 1. Importe o hook
import { useConfig } from '../../contexts/ConfigContext';

const UploadLogo = () => {
  const { updateLogo } = useConfig(); // Pegamos a função de atualizar
  const [preview, setPreview] = useState(null); // Estado local só para preview
  const fileInputRef = useRef(null); // Referência para o input invisível

  // Função chamada quando o usuário seleciona um arquivo
  const handleFileChange = (event) => {
    const file = event.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onloadend = () => {
        setPreview(reader.result); // Salva o base64 no preview
      };
      reader.readAsDataURL(file);
    }
  };

  // Simula o clique no input file escondido
  const handleUploadClick = () => {
    fileInputRef.current.click();
  };

  // Salva a logo no contexto global
  const handleConfirm = () => {
    if (preview) {
      updateLogo(preview);
      alert('Logo atualizada com sucesso!');
    }
  };

  // Limpa o preview e restaura a logo original (opcional, aqui estou limpando tudo)
  const handleDiscard = () => {
    setPreview(null);
    if (fileInputRef.current) {
      fileInputRef.current.value = '';
    }
  };

  return (
    <div className="space-y-6">
      <h2 className="castello-title text-3xl">
        Painel do Administrador &gt; Upload de imagem da logo
      </h2>

      <div className="max-w-2xl space-y-6">
        <div className="castello-card p-6">
          <div className="space-y-4">
            <input
              type="file"
              ref={fileInputRef}
              onChange={handleFileChange}
              accept="image/*"
              className="hidden"
            />

            <div className="flex space-x-2">
              <button 
                onClick={handleUploadClick}
                className="px-4 py-2 bg-gray-200 text-gray-700 rounded-lg hover:bg-gray-300 transition flex items-center space-x-2"
              >
                <Upload size={18} />
                <span>Upload</span>
              </button>
              
              <button 
                onClick={handleConfirm}
                className="px-4 py-2 bg-green-400 text-white rounded-lg hover:bg-green-500 transition flex items-center space-x-2"
              >
                <Check size={18} />
                <span>Confirmar</span>
              </button>
              
              <button 
                onClick={handleDiscard}
                className="px-4 py-2 bg-red-300 text-white rounded-lg hover:bg-red-400 transition flex items-center space-x-2"
              >
                <X size={18} />
                <span>Descartar</span>
              </button>
            </div>

            {/* Área de visualização */}
            {preview && (
              <div className="mt-4 p-4 bg-gray-50 rounded-lg border-2 border-dashed border-gray-300 flex flex-col items-center">
                <p className="text-sm text-gray-500 mb-2">Pré-visualização:</p>
                <img
                  src={preview}
                  alt="Logo preview"
                  className="max-w-xs max-h-48 object-contain"
                />
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default UploadLogo;