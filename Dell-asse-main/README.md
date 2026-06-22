# Dell'asse

O **Dell'Asse** é um software criado para facilitar o processo de agendamento e organização de eventos. A aplicação permite que clientes encontrem, selecionem e contratem diferentes tipos de festas em um único lugar, centralizando as informações de eventos, fornecedores e datas disponíveis de forma simples e organizada.

---

## Guia Rapido de Configuração

Se você está configurando o projeto pela primeira vez, confira o  
[Guia Rápido de Configuração](./docs/GuiaRapido.md).

Para a documentação acadêmica completa, acesse o  
[Documento Técnico](./docs/DocumentoTecnico.md).

---

## Objetivo do Projeto

Nosso objetivo é:

- Centralizar o fluxo de pesquisa, seleção e contratação de eventos e serviços;
- Facilitar o agendamento de datas e gerenciamento de disponibilidade;
- Organizar informações de clientes, espaços e pacotes de eventos;
- Reduzir retrabalho e falhas de comunicação entre cliente e organizador.

---

## Requisitos Funcionais

Para ver a lista completa de requisitos funcionais, acesse o 
[documento de Requisitos Funcionais](./docs/ReqFuncionais.md).

## Requisitos Não Funcionais / Regras de Negócio

Para ver os requisitos não funcionais e regras de negócio em detalhes, acesse o 
[documento de Requisitos Não Funcionais](./docs/ReqNaoFuncionais.md).


## Tecnologias Utilizadas

- **Linguagem:** Java  
- **Framework:** Spring Boot   
- **Banco de Dados:** PostgreSQL  
- **Front-end:** React + Vite
- **Ferramentas:** Maven, npm, IntelliJ/VS Code

## Como Inicializar o Projeto

### Pré-requisitos

Antes de iniciar, tenha instalado:

- **Java 21**
- **Maven** ou use o Maven Wrapper já incluído no projeto
- **Node.js** 18+ com **npm**
- **PostgreSQL**

### 1. Configurar o banco de dados

O back-end está configurado para acessar o PostgreSQL em:

```properties
jdbc:postgresql://localhost:5432/postgres
```

Se necessário, ajuste esse valor em `backend/src/main/resources/application.properties`.

### 2. Criar o arquivo `.env` do back-end

Crie um arquivo chamado `.env` dentro da pasta `backend/` com as credenciais do PostgreSQL:

```env
DB_USERNAME=seu_usuario
DB_PASSWORD=sua_senha
```

Observações:

- O arquivo deve ficar em `backend/.env`
- Não commite esse arquivo no repositório

### 3. Criar as tabelas do banco

Execute o script abaixo no PostgreSQL:

```text
database/CriacaoDB.sql
```

Se quiser carregar dados iniciais de demonstração, execute também:

```text
database/SeedDados.sql
```

### 4. Iniciar o back-end

No Windows, abra um terminal na pasta `backend/` e execute:

```powershell
.\mvnw.cmd spring-boot:run
```

Alternativamente, se estiver usando Maven instalado na máquina:

```powershell
mvn spring-boot:run
```

O back-end ficará disponível em:

```text
http://localhost:8080
```

### 5. Iniciar o front-end

Abra outro terminal na pasta `frontend/` e execute:

```powershell
npm install
npm run dev
```

O front-end ficará disponível em:

```text
http://localhost:3000
```

### 6. Acessar o sistema

Após subir os dois lados da aplicação:

- Front-end: `http://localhost:3000`
- Back-end: `http://localhost:8080`

Usuário de exemplo para testes:

- **Usuário:** `teste`
- **Senha:** `123456`


## Execução Rápida

Se o banco já estiver configurado, o fluxo mínimo é:

```powershell
# Terminal 1
cd backend
.\mvnw.cmd spring-boot:run

# Terminal 2
cd frontend
npm install
npm run dev
```

## Observações Importantes

- O sistema depende do PostgreSQL ativo antes de subir o back-end
- O arquivo `.env` deve conter `DB_USERNAME` e `DB_PASSWORD`
- O front-end consome a API local do back-end
- Se as tabelas já existirem, o back-end usa `ddl-auto=update` para atualizar a estrutura
- Para utilizar o usuário teste com permissões de Administrador, basta alterar em `user_role` na base de dados o `role_id` para `1`
