import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { AlertCircle, CheckCircle, User, Mail, Lock, Phone, Calendar } from 'lucide-react';

// Correção: Componente movido para fora para evitar perda de foco ao digitar
const InputIcon = ({ icon: Icon, ...props }) => (
  <div className="relative">
    <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none text-gray-400">
      <Icon size={18} />
    </div>
    <input
      {...props}
      className="block w-full pl-10 pr-3 py-3 border border-gray-200 rounded-xl leading-5 bg-gray-50 text-gray-900 placeholder-gray-400 focus:outline-none focus:bg-white focus:ring-2 focus:ring-primary-500 focus:border-transparent transition-all duration-200 sm:text-sm"
    />
  </div>
);

const Register = () => {
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    username: '',
    password: '',
    confirmPassword: '',
    phone: '',
    birthday: ''
  });
  
  const [error, setError] = useState('');
  const [success, setSuccess] = useState(false);
  const [loading, setLoading] = useState(false);
  const { register } = useAuth();
  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess(false);

    if (formData.password !== formData.confirmPassword) {
      setError('As senhas não coincidem');
      return;
    }

    setLoading(true);

    let formattedDate = formData.birthday;
    if (formData.birthday) {
        const [year, month, day] = formData.birthday.split('-');
        formattedDate = `${day}-${month}-${year}`;
    }

    const payload = {
        ...formData,
        birthday: formattedDate
    };

    const result = await register(payload);

    if (result.success) {
      setSuccess(true);
      setTimeout(() => {
        navigate('/login');
      }, 2000);
    } else {
      setError(result.error || 'Erro ao criar usuário');
    }

    setLoading(false);
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-gray-50 to-gray-200 py-12 px-4 sm:px-6 lg:px-8">
      <div className="max-w-lg w-full bg-white rounded-2xl shadow-xl overflow-hidden p-8 space-y-8">
        <div className="text-center">
          <h2 className="text-3xl font-bold text-gray-800 tracking-tight">
            Criar Conta
          </h2>
          <p className="mt-2 text-sm text-gray-500">
            Preencha seus dados para começar
          </p>
        </div>

        <form className="space-y-5" onSubmit={handleSubmit}>
          {error && (
            <div className="bg-red-50 border-l-4 border-red-500 text-red-700 p-4 rounded-md flex items-center shadow-sm">
              <AlertCircle size={20} className="mr-2" />
              <span className="text-sm font-medium">{error}</span>
            </div>
          )}
          {success && (
            <div className="bg-green-50 border-l-4 border-green-500 text-green-700 p-4 rounded-md flex items-center shadow-sm">
              <CheckCircle size={20} className="mr-2" />
              <span className="text-sm font-medium">Conta criada com sucesso!</span>
            </div>
          )}

          <div className="space-y-4">
            <InputIcon 
                icon={User}
                id="name" name="name" type="text" placeholder="Nome Completo" required
                value={formData.name} onChange={handleChange}
            />
             <InputIcon 
                icon={User}
                id="username" name="username" type="text" placeholder="Nome de Usuário (Login)" required
                value={formData.username} onChange={handleChange}
            />
            <InputIcon 
                icon={Mail}
                id="email" name="email" type="email" placeholder="Email" required
                value={formData.email} onChange={handleChange}
            />
            <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
                <InputIcon 
                    icon={Phone}
                    id="phone" name="phone" type="tel" placeholder="Telefone" required
                    value={formData.phone} onChange={handleChange}
                />
                <InputIcon 
                    icon={Calendar}
                    id="birthday" name="birthday" type="date" required
                    value={formData.birthday} onChange={handleChange}
                />
            </div>
            
            <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
                <InputIcon 
                    icon={Lock}
                    id="password" name="password" type="password" placeholder="Senha" required
                    value={formData.password} onChange={handleChange}
                />
                <InputIcon 
                    icon={Lock}
                    id="confirmPassword" name="confirmPassword" type="password" placeholder="Confirmar Senha" required
                    value={formData.confirmPassword} onChange={handleChange}
                />
            </div>
          </div>

          <button
            type="submit"
            disabled={loading || success}
            className="w-full flex justify-center py-3 px-4 border border-transparent text-sm font-semibold rounded-xl text-white bg-primary-600 hover:bg-primary-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500 shadow-md transform hover:-translate-y-0.5 transition-all duration-200 disabled:opacity-50 disabled:cursor-not-allowed"
          >
            {loading ? (
              <div className="animate-spin rounded-full h-5 w-5 border-b-2 border-white"></div>
            ) : (
              <>
                Criar Conta
              </>
            )}
          </button>

          <div className="text-center mt-4">
            <p className="text-sm text-gray-600">
              Já tem uma conta?{' '}
              <Link to="/login" className="font-semibold text-primary-600 hover:text-primary-500 transition-colors">
                Faça Login
              </Link>
            </p>
          </div>
        </form>
      </div>
    </div>
  );
};

export default Register;