# Dellasse Backend

## Guia Rápido (para quem nunca viu o código)

- O que é: uma API para gerenciar usuários, empresas, produtos e festas.
- Como protege: usa token JWT para autorizar cada requisição.
- Como rodar: criar arquivo `.env`, abrir o projeto na IDE e executar.

### Rodar em 3 passos

1. Crie um arquivo `.env` na resources do projeto com:
   ```
   DB_USERNAME=<seu_usuario_postgres>
   DB_PASSWORD=<sua_senha_postgres>
   ```
2. Abra o projeto na sua IDE (IntelliJ/Eclipse/VS Code) e rode a classe `BackendApplication`.
3. A API sobe em `http://localhost:8080`.

Se preferir linha de comando (depende do seu ambiente):
- Maven: `mvn spring-boot:run`

### Primeiro acesso (login e token) — Postman

- Usuário de exemplo: `username: teste`, `password: 123456`.
- Crie um ambiente no Postman com a variável `baseUrl = http://localhost:8080`.
- Faça um request `POST {{baseUrl}}/user/login`:
  - Header: `Content-Type: application/json`
  - Body (raw JSON):
    ```json
    { "username": "teste", "password": "123456" }
    ```
  - Em `Tests`, salve o token na coleção:
    ```javascript
    const data = pm.response.json();
    if (data.token) {
      pm.collectionVariables.set('token', data.token);
    }
    ```
- Depois, nas requisições protegidas, use `Authorization` → `Bearer Token` com `{{token}}`.

### Teste rápido de um fluxo — Postman

1) Criar produto
- Método: `POST`
- URL: `{{baseUrl}}/product/create`
- Auth: `Bearer Token` → `{{token}}`
- Header: `Content-Type: application/json`
- Body (raw JSON):
```json
{
  "name": "Caneca",
  "description": "Caneca personalizada",
  "price": 29.9,
  "stockQuantity": 100,
  "category": "utilidades",
  "imageUrl": ""
}
```

2) Atualizar produto
- Método: `PATCH`
- URL: `{{baseUrl}}/product/update/1`
- Auth: `Bearer Token` → `{{token}}`
- Header: `Content-Type: application/json`
- Body (raw JSON):
```json
{
  "name": "Caneca Premium",
  "description": "Melhor acabamento",
  "price": 39.9,
  "stockQuantity": 80,
  "imageUrl": "",
  "category": "utilidades"
}
```

3) Criar festa (party)
- Método: `POST`
- URL: `{{baseUrl}}/party/create`
- Auth: `Bearer Token` → `{{token}}`
- Header: `Content-Type: application/json`
- Body (raw JSON):
```json
{
  "title": "Festa de Aniversário",
  "description": "Tema colorido",
  "products": [1, 2, 3],
  "generateBudget": 500.0,
  "observations": "Mesa central",
  "imageURL": ""
}
```

## Arquitetura de Pastas

- `controllers`: expõe endpoints REST (ex.: usuário, produto, empresa, festa/party).
- `service`: regras de negócio, validações e integração entre repositórios e entidades.
- `repositories`: Spring Data JPA para acesso ao banco.
- `models`: entidades JPA (`User`, `Enterprise`, `Product`, `Party`, `Role`).
- `contracts`: DTOs de entrada/saída (records) organizados por domínio.
- `mappers`: conversão entre DTOs e entidades.
- `infrastructure`: segurança (`SecurityConfig`, filtros), CORS.
- `exceptions`: enum de erros e tratador global.
- `util`: utilitários (ex.: conversões, datas, status).
- `config`: inicialização de dados para desenvolvimento.

## Segurança (em termos simples)

- Você sempre precisa fazer login para obter um token.
- Com esse token, você chama as rotas protegidas.
- A API também aceita o token pelo header `Authorization: Bearer <token>`.
- A empresa vinculada ao usuário deve estar válida (não expirada) — o filtro bloqueia se estiver expirada.
- Rotas públicas: `POST /user/login` e `POST /user/create`.

## Modelos (Entidades)

- `User`: campos básicos, roles (`@ManyToMany`) e empresa (`@ManyToOne`). Referência: `backend/src/main/java/com/dellasse/backend/models/User.java:32–51`.
- `Enterprise`: dados da empresa e `dateExpiration`. Referência: `backend/src/main/java/com/dellasse/backend/models/Enterprise.java:25–33`.
- `Product`: atributos do produto e relacionamento com `Enterprise` e `User`.
- `Party`: composição com `Product` via `@ManyToMany` e vínculos com `User` e `Enterprise`.
- `Role`: enum interno `Values` com IDs fixos (ADMIN, FUNCIONARIO, BASIC). Referência: `backend/src/main/java/com/dellasse/backend/models/Role.java:21–41`.

## DTOs (Contracts)

- Usuário:
  - `CreateRequest`: validações de nome, email, username e senha; anotação customizada `@PasswordMatches`. `backend/src/main/java/com/dellasse/backend/contracts/user/CreateRequest.java:9–30`.
  - `LoginRequest`: username e password. `backend/src/main/java/com/dellasse/backend/contracts/user/LoginRequest.java:5–6`.
  - `UpdateRequest` e `UpdateResponse`: alteração e resposta de dados.
- Empresa:
  - `CreateRequest`, `UpdateRequest`: dados cadastrais e imagem.
- Produto:
  - `ProductCreateRequest`: validação de nome, preço, estoque. `backend/src/main/java/com/dellasse/backend/contracts/product/ProductCreateRequest.java:9–26`.
  - `ProductUpdateRequest`, `UpdateResponse`.
- Party:
  - `PartyCreateRequest`: título, descrição, lista de produtos, orçamento, etc.

## Mapeadores (Mappers)

- Conversões DTO → Entidade e Entidade → Resposta:
  - Usuário: `backend/src/main/java/com/dellasse/backend/mappers/UserMapper.java:8–24` e `18–27`.
  - Empresa: `backend/src/main/java/com/dellasse/backend/mappers/EnterpriseMapper.java:8–22` e `24–30`.
  - Produto: `backend/src/main/java/com/dellasse/backend/mappers/ProductMapper.java:7–20`, `22–34`, `36–41`.

## Endpoints Principais (o que cada um faz)

- Usuário (`/user`):
  - `POST /user/create` — cria usuário.
  - `POST /user/login` — autentica e retorna `{ token, roles }`.
  - `POST /user/update/{userId}` — atualiza dados do próprio usuário (requer JWT).
- Empresa (`/enterprise`):
  - `POST /enterprise/create` — cria empresa (requer `ADMIN`). `backend/src/main/java/com/dellasse/backend/controllers/EnterpriseController.java:18–24`.
  - `POST /enterprise/update/{enterpriseId}` — atualiza (requer `ADMIN`). `backend/src/main/java/com/dellasse/backend/controllers/EnterpriseController.java:26–33`.
- Produto (`/product`):
  - `POST /product/create` — cria produto para a empresa do usuário. `backend/src/main/java/com/dellasse/backend/controllers/ProductContoller.java:22–25`.
  - `PATCH /product/update/{id}` — atualiza dados do produto. `backend/src/main/java/com/dellasse/backend/controllers/ProductContoller.java:27–33`.
- Party (`/party`):
  - `POST /party/create` — cria festa associada ao usuário/empresa. `backend/src/main/java/com/dellasse/backend/controllers/PartyController.java:19–23`.

## Regras de Negócio (o que a API garante)

- Login e token:
  - Geração de JWT com `scope` das roles do usuário. `backend/src/main/java/com/dellasse/backend/service/UserService.java:93–107`.
  - Bloqueia login se empresa expirada: `backend/src/main/java/com/dellasse/backend/service/UserService.java:81–88`.
- Vínculo com empresa:
  - Validação do vínculo e permissão: `backend/src/main/java/com/dellasse/backend/service/UserService.java:147–165`.
  - Filtro garante que rotas autenticadas só prosseguem se a empresa não estiver expirada: `backend/src/main/java/com/dellasse/backend/infrastructure/UserEnterpriseCheckFilter.java:60–67`.
- Produtos:
  - Verificações de existência, atualização de datas e pertencimento à empresa: `backend/src/main/java/com/dellasse/backend/service/ProductService.java:60–75`.

## Erros comuns (como interpretar)

- `401 USER_NOT_AUTHENTICATED`: faltou token ou token inválido.
- `403 ENTERPRISE_EXPIRED`: sua empresa está expirada; renove antes de usar.
- `403 USER_NOT_ADMIN`: rota exige perfil `ADMIN`.
- `404 PRODUCT_NOT_FOUND` ou `ENTERPRISE_NOT_FOUND`: recurso não existe ou não pertence à sua empresa.
- `409 USER_ALREADY_EXISTS`/`ENTERPRISE_EXISTS`: tentativa de criar duplicado.

## Validações

- Bean Validation em DTOs (`@NotBlank`, `@NotNull`, `@DecimalMin`, etc.).
- Validação customizada de senha confirmada: anotação `@PasswordMatches` e validador `PasswordMatchesValidator`. `backend/src/main/java/com/dellasse/backend/validation/PasswordMatchesValidator.java:9–28`.

## Erros e Tratamento

- Enum `DomainError` padroniza mensagens e status: `backend/src/main/java/com/dellasse/backend/exceptions/DomainError.java:5–30`.
- Exceção `DomainException` carrega o erro e expõe `getStatus()`: `backend/src/main/java/com/dellasse/backend/exceptions/DomainException.java:7–14`.
- `GlobalException` mapeia para respostas HTTP: `backend/src/main/java/com/dellasse/backend/exceptions/GlobalException.java:41–44`.

## Configuração (onde ajustar)

- `application.properties`:
  - Banco: `spring.datasource.url=jdbc:postgresql://localhost:5432/postgres`.
  - Credenciais: `spring.datasource.username=${DB_USERNAME}`, `spring.datasource.password=${DB_PASSWORD}`.
  - Console H2 habilitado para diagnóstico (`/h2-console`), mesmo com Postgres configurado.
  - Logs detalhados (Hibernate, Security) para desenvolvimento.
  - JWT: caminhos das chaves.
- Variáveis de ambiente:
  - `BackendApplication` carrega `.env` e injeta em `System.properties`: `backend/src/main/java/com/dellasse/backend/BackendApplication.java:10–15`.
  - Crie um `.env` na raiz com:
    ```
    DB_USERNAME=<usuario>
    DB_PASSWORD=<senha>
    ```

## CORS

- Configuração global permitindo localhost com portas diversas, métodos e headers: `backend/src/main/java/com/dellasse/backend/infrastructure/CorsGlobalConfig.java:14–30`.

## Seed de Dados (Dev)

- `DataInitializer`:
  - Roles: `ADMIN`, `FUNCIONARIO`, `BASIC`.
  - Empresa exemplo `dellasse`.
  - Usuário exemplo `teste` com role `BASIC` e vínculo à empresa. `backend/src/main/java/com/dellasse/backend/config/DataInitializer.java:34–54`.

## Execução (como subir)

- IDE (recomendado): Executar `BackendApplication`.
- Linha de comando:
  - Se o projeto estiver com Maven Wrapper: `mvnw spring-boot:run` (Windows) ou `./mvnw spring-boot:run`.
  - Caso não haja wrapper, importe no IDE com suporte a Maven/Gradle e configure o build.

## Convenções & Boas Práticas

- Camadas bem separadas e validações no `service`.
- DTOs (records) como contratos estáveis.
- Mapeadores isolam conversões.
- Filtros de segurança sem acoplamento a endpoints públicos.
- Erros de domínio centralizados, respostas consistentes.

## Fluxos Críticos (resumo do funcionamento)

- Login:
  - Valida usuário e senha, verifica expiração da empresa, gera JWT, devolve `token` e `roles`. `backend/src/main/java/com/dellasse/backend/service/UserService.java:67–113`.
- Requisições autenticadas:
  - `JwtCookieAuthenticationFilter` coloca `Authorization: Bearer <token>` se houver.
  - `UserEnterpriseCheckFilter` valida autenticação, vínculo à empresa e expiração.

## Glossário

- `JWT`: token que comprova sua identidade para usar a API.
- `Role`: perfil de acesso (ex.: `ADMIN`, `BASIC`).
- `Enterprise`: empresa vinculada ao usuário; precisa estar válida (não expirada).
- `DTO`: objeto simples que representa a entrada/saída das rotas.