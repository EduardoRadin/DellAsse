# Guia Rápido — Tratamento de ApiError no Front-end

Este guia mostra como capturar o padrão JSON de erro do back-end (`ApiError`) e exibir o campo `message` para o usuário no React.

---

## Formato de erro do back-end

Toda falha tratada pelo `GlobalException` retorna este corpo:

```json
{
  "timestamp": "2026-06-16T14:30:00-03:00",
  "status": 404,
  "error": "Not Found",
  "code": "PARTY_NOT_FOUND",
  "message": "Party not found",
  "path": "/party/99",
  "fieldErrors": null
}
```

Em erros de validação (`VALIDATION_ERROR`), `fieldErrors` traz um mapa `{ "campo": "mensagem" }`.

---

## Passo 1 — Helper para extrair a mensagem

Crie `frontend/src/utils/apiError.js`:

```javascript
/**
 * Extrai a mensagem amigável de uma resposta ApiError do Axios.
 */
export function getApiErrorMessage(error, fallback = 'Ocorreu um erro inesperado.') {
  const data = error?.response?.data;

  if (!data) {
    return error?.message || fallback;
  }

  // Erros de validação: junta mensagens dos campos
  if (data.code === 'VALIDATION_ERROR' && data.fieldErrors) {
    const fields = Object.values(data.fieldErrors);
    if (fields.length > 0) {
      return fields.join(' · ');
    }
  }

  return data.message || fallback;
}
```

---

## Passo 2 — Interceptor global (opcional, recomendado)

Em `frontend/src/services/api.js`, importe o helper e enriqueça o erro rejeitado:

```javascript
import axios from 'axios';
import { getApiErrorMessage } from '../utils/apiError';

const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: { 'Content-Type': 'application/json' },
  withCredentials: true,
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      window.location.href = '/login';
      return Promise.reject(error);
    }

    // Anexa mensagem legível para uso nos componentes
    error.userMessage = getApiErrorMessage(error);
    return Promise.reject(error);
  }
);

export default api;
```

---

## Passo 3 — Usar em um serviço

Exemplo em `partyService.js` com pré-visualização de orçamento:

```javascript
import api from './api';
import { getApiErrorMessage } from '../utils/apiError';

export const partyService = {
  async previewBudget(productIds) {
    try {
      const response = await api.post('/party/budget-preview', { products: productIds });
      return response.data;
    } catch (error) {
      throw new Error(getApiErrorMessage(error, 'Não foi possível calcular o orçamento.'));
    }
  },

  async create(partyData) {
    const response = await api.post('/party/create', partyData);
    return response.data;
  },
};
```

---

## Passo 4 — Exibir no componente

Exemplo em `CreateCustomParty.jsx`:

```jsx
import { useState } from 'react';
import { partyService } from '../services/partyService';
import { getApiErrorMessage } from '../utils/apiError';

export default function CreateCustomParty() {
  const [errorMessage, setErrorMessage] = useState('');
  const [budget, setBudget] = useState(null);

  const handleCalculateBudget = async (selectedProductIds) => {
    setErrorMessage('');
    try {
      const result = await partyService.previewBudget(selectedProductIds);
      setBudget(result);
    } catch (error) {
      // Se o interceptor já definiu userMessage, use-o; senão, o helper
      setErrorMessage(error.userMessage || getApiErrorMessage(error));
    }
  };

  const handleSubmit = async (formData) => {
    setErrorMessage('');
    try {
      await partyService.create(formData);
      // sucesso: redirecionar ou limpar formulário
    } catch (error) {
      setErrorMessage(error.userMessage || getApiErrorMessage(error));
    }
  };

  return (
    <div>
      {errorMessage && (
        <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
          {errorMessage}
        </div>
      )}

      {budget && (
        <p className="text-green-700">
          Orçamento: R$ {budget.total.toFixed(2)}
          {budget.discountAmount > 0 && (
            <span className="text-sm text-gray-600">
              {' '}(desconto de R$ {budget.discountAmount.toFixed(2)})
            </span>
          )}
        </p>
      )}

      {/* ...resto do formulário */}
    </div>
  );
}
```

---

## Passo 5 — Alternativa com `fetch` (sem Axios)

```javascript
async function createParty(partyData) {
  const token = localStorage.getItem('token');

  const response = await fetch('http://localhost:8080/party/create', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify(partyData),
  });

  if (!response.ok) {
    const apiError = await response.json();
    throw new Error(apiError.message || 'Erro ao criar festa.');
  }

  return response.json();
}
```

---

## Resumo

| Onde | O que fazer |
|------|-------------|
| `utils/apiError.js` | Helper `getApiErrorMessage()` |
| `services/api.js` | Interceptor que define `error.userMessage` |
| Serviços | `try/catch` + relançar ou tratar |
| Componentes | Estado `errorMessage` + alerta visual |

Com isso, qualquer `DomainException` (`PARTY_NOT_FOUND`, `USER_FORBIDDEN`, etc.) ou erro de validação aparece de forma clara para o usuário final.
