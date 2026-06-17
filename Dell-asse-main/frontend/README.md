# Dell'Asse Frontend

Frontend React para o sistema Dell'Asse

## Tecnologias

- **React 18** - Biblioteca JavaScript para construção de interfaces
- **Vite** - Build tool e dev server
- **React Router** - Roteamento
- **Axios** - Cliente HTTP
- **Tailwind CSS** - Framework CSS utilitário
- **Lucide React** - Ícones

## Pré-requisitos

- Node.js 18+ 
- npm ou yarn

## Instalação

1. Instale as dependências:

```bash
npm install
```

2. Configure as variáveis de ambiente (opcional):

Crie um arquivo `.env` na raiz do frontend:

```env
VITE_API_URL=http://localhost:8080
```

3. Inicie o servidor de desenvolvimento:

```bash
npm run dev
```

O frontend estará disponível em `http://localhost:3000`

## Scripts Disponíveis

- `npm run dev` - Inicia o servidor de desenvolvimento
- `npm run build` - Cria build de produção
- `npm run preview` - Preview do build de produção
- `npm run lint` - Executa o linter

## Estrutura do Projeto

```
src/
├── components/      # Componentes reutilizáveis
│   ├── Header.jsx
│   ├── Footer.jsx
│   ├── Layout.jsx
│   └── ProtectedRoute.jsx
├── contexts/        # Contextos React (Auth)
│   └── AuthContext.jsx
├── pages/           # Páginas da aplicação
│   ├── Home.jsx
│   ├── Login.jsx
│   ├── Register.jsx
│   ├── Products.jsx
│   ├── Parties.jsx
│   └── Enterprises.jsx
├── services/        # Serviços de API
│   ├── api.js
│   ├── authService.js
│   ├── productService.js
│   ├── partyService.js
│   └── enterpriseService.js
├── App.jsx          # Componente principal
├── main.jsx         # Entry point
└── index.css        # Estilos globais
```

## Funcionalidades

- ✅ Autenticação (Login/Registro)
- ✅ Gerenciamento de Produtos
- ✅ Gerenciamento de Festas
- ✅ Gerenciamento de Empresas (Admin)
- ✅ Proteção de rotas
- ✅ UI responsiva

## Integração com Backend

O frontend está configurado para se comunicar com o backend Spring Boot na porta 8080. Certifique-se de que o backend está rodando antes de usar o frontend.

A autenticação utiliza JWT tokens armazenados no localStorage e cookies.


