# Documento Técnico — Dell'Asse

**Sistema de Gestão e Agendamento de Festas**  
**Equipe:** Dell'Asse  
**Versão:** 1.0  
**Data:** Junho/2026

---

## 1. Objetivo do Projeto

O **Dell'Asse** é uma aplicação web para centralizar o fluxo de pesquisa, seleção, orçamento e contratação de festas e serviços relacionados. O sistema permite que clientes montem eventos personalizados, consultem galerias de decoração e que empresas parceiras gerenciem produtos, festas e usuários em um único ambiente.

### Objetivos específicos

- Centralizar cadastro de usuários, empresas, produtos e festas;
- Calcular orçamentos de festas com regras de desconto configuráveis;
- Garantir autenticação segura via JWT e controle de acesso por perfil (`ADMIN`, `FUNCIONARIO`, `BASIC`);
- Padronizar comunicação entre front-end e back-end com DTOs e tratamento de erros unificado (`ApiError`).

---

## 2. Tecnologias Utilizadas

| Camada | Tecnologia | Versão / Observação |
|--------|------------|---------------------|
| Back-end | Java + Spring Boot | Java 21, Spring Boot 3.5 |
| Persistência | Spring Data JPA + PostgreSQL | `ddl-auto=update` em dev |
| Segurança | Spring Security + OAuth2 JWT | Token Bearer / Cookie |
| Mapeamento | MapStruct + Mappers manuais | DTO ↔ Entidade |
| Front-end | React + Vite | React 18, Tailwind CSS |
| HTTP Client | Axios | Interceptors para JWT e erros |
| Build | Maven (back-end) / npm (front-end) | — |
| Banco | PostgreSQL | Script em `database/CriacaoDB.sql` |

---

## 3. Arquitetura em Camadas

O projeto adota **arquitetura em camadas** combinada com o padrão **MVC** no back-end e **componentes + serviços** no front-end.

### 3.1 Visão geral

```
┌─────────────────────────────────────────────────────────────┐
│                      FRONT-END (React)                      │
│  Pages / Components  →  Services (Axios)  →  API REST       │
└──────────────────────────────┬──────────────────────────────┘
                               │ HTTP + JSON
┌──────────────────────────────▼──────────────────────────────┐
│                    BACK-END (Spring Boot)                   │
│                                                             │
│  ┌─────────────┐   ┌─────────────┐   ┌──────────────────┐  │
│  │ Controllers │ → │  Services   │ → │  Repositories    │  │
│  │   (MVC)     │   │ (Negócio)   │   │  (Spring Data)   │  │
│  └──────┬──────┘   └──────┬──────┘   └────────┬─────────┘  │
│         │ DTOs            │ Entidades           │ SQL/JPA   │
│  ┌──────▼──────┐   ┌──────▼──────┐   ┌─────────▼─────────┐  │
│  │  Contracts  │   │   Models    │   │    PostgreSQL     │  │
│  │   (DTOs)    │   │  (Entidades)│   │                   │  │
│  └─────────────┘   └─────────────┘   └───────────────────┘  │
└─────────────────────────────────────────────────────────────┘
```

### 3.2 Como MVC e Camadas se complementam

| Papel MVC | Camada equivalente | Responsabilidade no Dell'Asse |
|-----------|-------------------|-------------------------------|
| **Model** | `models/` + `contracts/` | Entidades JPA representam o domínio; DTOs (records) definem o contrato da API |
| **View** | Front-end React | Renderiza dados e envia requisições; não contém regra de negócio crítica |
| **Controller** | `controllers/` | Recebe HTTP, valida entrada (`@Valid`), delega ao Service e retorna DTO/HTTP status |

**Fluxo típico de uma requisição:**

1. O **Controller** recebe JSON e converte para DTO (`PartyCreateRequest`);
2. O **Service** aplica regras de negócio, chama **Repositories** e **Mappers**;
3. O **Repository** persiste/consulta entidades no PostgreSQL;
4. A resposta retorna como DTO (`PartyResponse`) ou `ApiError` em caso de falha.

Essa separação garante que controllers permaneçam finos, services concentrem regras e repositories isolem o acesso a dados.

### 3.3 Estrutura de pastas do back-end

```
backend/src/main/java/com/dellasse/backend/
├── controllers/      # Endpoints REST (camada de apresentação)
├── service/          # Regras de negócio (camada de aplicação)
├── repositories/     # Acesso a dados (camada de persistência)
├── models/           # Entidades JPA
├── contracts/        # DTOs de entrada e saída
├── mappers/          # Conversão DTO ↔ Entidade
├── pricing/          # Cálculo de orçamento + Strategy de descontos
├── exceptions/       # ApiError, DomainException, GlobalException
├── infrastructure/   # Security, CORS, filtros JWT
├── config/           # Inicialização de dados (seed dev)
└── util/             # Utilitários (datas, status, conversões)
```

---

## 4. Design Patterns Aplicados

### 4.1 Repository Pattern

**Onde:** `repositories/` — interfaces que estendem `JpaRepository<Entity, Id>`.

**Por quê:** Abstrai o acesso ao banco. Os services dependem de interfaces (`ProductRepository`, `PartyRepository`), não de SQL direto.

**Exemplo:**

```java
public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByIdAndEnterprise_Id(Long id, UUID enterpriseId);
}
```

O `ProductService` usa `productRepository.save()`, `findById()` e `existsByIdAndEnterprise_Id()` sem conhecer detalhes de implementação JPA.

---

### 4.2 Service Layer Pattern

**Onde:** `service/` — classes anotadas com `@Service`.

**Por quê:** Centraliza regras de negócio fora dos controllers. Valida permissões, orquestra repositórios e lança `DomainException` quando necessário.

**Exemplo:** `PartyService.create()` valida roles do usuário, associa galeria, aplica valores padrão, **recalcula o orçamento** via `PartyBudgetCalculator` e persiste a festa.

---

### 4.3 DTO (Data Transfer Object)

**Onde:** `contracts/` — records Java com validações Bean Validation.

**Por quê:** Desacopla a API das entidades JPA. Evita expor senhas, lazy-loading e estrutura interna do banco.

**Exemplos:**

| DTO | Uso |
|-----|-----|
| `PartyCreateRequest` | Entrada para `POST /party/create` |
| `PartyResponse` | Saída padronizada de festas |
| `PartyBudgetResponse` | Detalhamento de orçamento com descontos |
| `ProductCreateRequest` | Entrada para criação de produtos |

---

### 4.4 Dependency Injection (Injeção de Dependência)

**Onde:** Spring Framework — `@Autowired` / construtor em services, controllers e strategies.

**Por quê:** O Spring instancia e injeta dependências automaticamente. Facilita testes (mocks) e baixo acoplamento.

**Exemplo em `PartyBudgetCalculator`:**

```java
@Service
public class PartyBudgetCalculator {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private List<DiscountStrategy> discountStrategies; // Spring injeta todas as @Component
}
```

O Spring descobre automaticamente `VolumeDiscountStrategy` e `CategoryDiscountStrategy` e as injeta na lista.

---

### 4.5 Strategy Pattern *(padrão extra — orçamento de festas)*

**Onde:** `pricing/strategy/` + `PartyBudgetCalculator`.

**Problema:** Diferentes regras de desconto (volume, categoria, sazonalidade) não devem ficar em um único `if/else` no service.

**Solução:** Interface `DiscountStrategy` com implementações intercambiáveis. O calculador percorre a cadeia e aplica apenas as estratégias cujo `supports()` retorna `true`.

#### Interface

```java
public interface DiscountStrategy {
    boolean supports(PartyBudgetContext context);
    double calculateDiscount(PartyBudgetContext context);
    String getName();
}
```

#### Implementações

| Classe | Regra |
|--------|-------|
| `VolumeDiscountStrategy` | 5% de desconto quando a festa tem ≥ 3 produtos |
| `CategoryDiscountStrategy` | 10% sobre itens da categoria `decoracao` |

#### Orquestração

```java
for (DiscountStrategy strategy : discountStrategies) {
    if (strategy.supports(context)) {
        double amount = strategy.calculateDiscount(context);
        // acumula desconto e registra detalhe
    }
}
```

#### Integração

- `POST /party/budget-preview` — pré-visualiza orçamento sem persistir;
- `PartyService.create()` — recalcula `generateBudget` no back-end antes de salvar (fonte única de verdade).

**Benefício acadêmico:** Novas regras (ex.: `SeasonalDiscountStrategy`) são adicionadas criando uma classe `@Component` sem alterar código existente — princípio **Open/Closed**.

---

## 5. Estrutura da API

### 5.1 Convenções

- Base URL: `http://localhost:8080`
- Autenticação: `Authorization: Bearer <token>` (exceto rotas públicas)
- Entrada/saída: JSON
- Erros: corpo padronizado `ApiError`

### 5.2 Rotas principais

| Método | Rota | Auth | Descrição |
|--------|------|------|-----------|
| POST | `/user/login` | Não | Autentica e retorna JWT |
| POST | `/user/create` | Não | Cadastra usuário |
| GET | `/user/all` | Sim | Lista usuários |
| POST | `/enterprise/create` | ADMIN | Cria empresa |
| GET | `/enterprise/{id}` | Sim | Busca empresa |
| POST | `/product/create` | Sim | Cria produto |
| GET | `/product/all` | Não* | Lista produtos |
| PATCH | `/product/update/{id}` | Sim | Atualiza produto |
| DELETE | `/product/{id}` | Sim | Remove produto |
| POST | `/party/create` | Sim | Cria festa (orçamento recalculado) |
| POST | `/party/budget-preview` | Sim | Pré-visualiza orçamento |
| GET | `/party/all` | Sim | Lista festas |
| GET | `/party/{id}` | Sim | Busca festa por ID |
| PATCH | `/party/{id}` | Sim | Atualiza festa |
| PATCH | `/party/{id}/status` | Sim | Atualiza status |
| DELETE | `/party/{id}` | Sim | Remove festa |
| GET | `/party/gallery` | Não | Galeria pública de festas |
| POST | `/gallery/create` | Sim | Cria galeria |

\* Conforme `SecurityConfig`.

### 5.3 Formato de erro (`ApiError`)

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

| Campo | Descrição |
|-------|-----------|
| `code` | Identificador estável (`PARTY_NOT_FOUND`, `VALIDATION_ERROR`, etc.) |
| `message` | Mensagem legível para o usuário |
| `fieldErrors` | Mapa campo → mensagem (validação Bean Validation) |

Tratamento centralizado em `GlobalException` (`@RestControllerAdvice`).

### 5.4 Exemplo — pré-visualização de orçamento

**Request:** `POST /party/budget-preview`

```json
{ "products": [1, 2, 3] }
```

**Response:**

```json
{
  "subtotal": 1219.90,
  "discountAmount": 114.89,
  "total": 1105.01,
  "discounts": [
    { "name": "Desconto por volume (5%)", "amount": 60.99 },
    { "name": "Desconto em decoração (10%)", "amount": 53.99 }
  ]
}
```

---

## 6. Banco de Dados

### 6.1 Modelo relacional (resumo)

```
enterprise ──┬── users ──┬── user_roles ── role
             │           │
             ├── product │
             │           │
             ├── party ──┴── party_products ── product
             │
             └── gallery ── image

cart ── (user, party, enterprise, total_price)
product_price_audit ── (log de alteração de preços)
```

### 6.2 Tabelas principais

| Tabela | Descrição | Chave |
|--------|-----------|-------|
| `enterprise` | Empresas parceiras | `id` (UUID) |
| `users` | Usuários do sistema | `uuid` (UUID) |
| `role` / `user_roles` | Perfis de acesso | `role_id` / N:N |
| `product` | Itens vendáveis (decoração, alimentação…) | `id` (serial) |
| `party` | Festas/eventos | `id` (serial) |
| `party_products` | Produtos vinculados à festa | N:N |
| `gallery` / `image` | Galerias de decoração | `id` |
| `cart` | Carrinho (estrutura preparada) | `id` |

### 6.3 Scripts SQL

| Arquivo | Conteúdo |
|---------|----------|
| `database/CriacaoDB.sql` | DDL completo, índices, views, procedures e triggers |
| `database/SeedDados.sql` | Dados de demonstração (admin, 3 produtos, 2 festas) |

### 6.4 View de relatório

A view `vw_relatorio_orcamento` compara `party.generate_budget` com a soma dos preços dos produtos vinculados — útil para auditoria de orçamentos.

---

## 7. Segurança

- **JWT** gerado no login com `scope` das roles;
- **Filtros:** `JwtCookieAuthenticationFilter`, `UserEnterpriseCheckFilter` (bloqueia empresa expirada);
- **Senhas:** BCrypt (`BCryptPasswordEncoder`);
- **Autorização:** `@PreAuthorize` em rotas administrativas;
- **CORS:** configurado para `localhost` (dev).

---

## 8. Front-end (resumo)

- **React + Vite** com rotas protegidas (`ProtectedRoute`);
- **Axios** centralizado em `src/services/api.js` com interceptors;
- Serviços por domínio: `authService`, `productService`, `partyService`, etc.;
- Tratamento de `ApiError` documentado em `docs/GuiaFrontApiError.md`.

---

## 9. Como Executar

1. Criar `.env` com `DB_USERNAME` e `DB_PASSWORD`;
2. Executar `database/CriacaoDB.sql` no PostgreSQL;
3. (Opcional) Executar `database/SeedDados.sql` para dados de demo;
4. Subir back-end: `BackendApplication` (porta 8080);
5. Subir front-end: `npm install && npm run dev` (porta 5173);
6. Login: `admin` / `123456` ou `teste` / `123456`.

---

## 10. Referências Internas

- [Guia Rápido](./GuiaRapido.md)
- [Requisitos Funcionais](./ReqFuncionais.md)
- [Requisitos Não Funcionais](./ReqNaoFuncionais.md)
- [Guia Front-end — ApiError](./GuiaFrontApiError.md)
