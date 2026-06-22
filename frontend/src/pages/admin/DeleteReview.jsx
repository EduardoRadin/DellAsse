import { useState } from 'react';
import { Trash2, Star } from 'lucide-react';

const DeleteReview = () => {
  const [code, setCode] = useState('8983444');
  const [review, setReview] = useState({
    user: 'DuduLoirinho',
    rating: 1,
    text: 'Entrei no banheiro e tinham 2 jovens se peleando, comida estava fria e a empregada era feia uuuuuu',
    date: '17/05/2024',
  });

  const handleDelete = () => {
    // Lógica para apagar avaliação
    console.log('Apagando avaliação:', code);
  };

  return (
    <div className="space-y-6">
      <h2 className="castello-title text-3xl">
        Painel do Administrador &gt; Apagar avaliação
      </h2>

      <div className="max-w-2xl space-y-6">
        {/* Code Input */}
        <div>
          <label className="block font-semibold text-gray-900 mb-2">
            Informe o código da avaliação:
          </label>
          <div className="flex space-x-2">
            <input
              type="text"
              value={code}
              onChange={(e) => setCode(e.target.value)}
              className="flex-1 px-4 py-2 border-2 border-blue-400 rounded-lg bg-white focus:outline-none focus:ring-2 focus:ring-blue-400"
            />
            <button
              onClick={handleDelete}
              className="px-6 py-2 bg-pink-300 text-castello-dark rounded-lg hover:bg-pink-400 transition"
            >
              Apagar
            </button>
          </div>
        </div>

        {/* Review Display */}
        {review && (
          <div className="castello-card p-6 bg-blue-50 border-2 border-blue-200">
            <div className="flex items-start justify-between mb-4">
              <div className="flex items-center space-x-3">
                <div className="w-10 h-10 rounded-full bg-gray-300 flex items-center justify-center">
                  <span className="text-gray-600 font-semibold">
                    {review.user.charAt(0).toUpperCase()}
                  </span>
                </div>
                <div>
                  <h3 className="font-bold text-gray-900">{review.user}</h3>
                  <div className="flex items-center space-x-1 mt-1">
                    {[...Array(5)].map((_, i) => (
                      <Star
                        key={i}
                        size={16}
                        className={
                          i < review.rating
                            ? 'text-yellow-400 fill-yellow-400'
                            : 'text-gray-300'
                        }
                      />
                    ))}
                    <span className="ml-2 text-sm text-gray-600">
                      {[...Array(3)].map((_, i) => (
                        <span key={i} className="text-red-500">
                          X
                        </span>
                      ))}
                    </span>
                  </div>
                </div>
              </div>
            </div>
            <p className="text-gray-700 mb-4">{review.text}</p>
            <div className="text-right text-sm text-gray-600">{review.date}</div>
          </div>
        )}
      </div>
    </div>
  );
};

export default DeleteReview;

