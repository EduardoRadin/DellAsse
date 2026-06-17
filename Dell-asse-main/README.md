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
- **Ferramentas:** Maven, Intellij

# Configuração do Ambiente

## Arquivo .env

### 1. Criar o arquivo .env

Na raiz do projeto, crie um arquivo chamado `.env`:

```bash
# Copie o arquivo de exemplo
cp .env.example .env
```

### 2. Configurar as variáveis

Edite o arquivo `.env` com suas configurações:

```env
# Configurações do Banco de Dados
DB_USERNAME=seu_usuario
DB_PASSWORD=sua_senha
```

### 3. Verificar a dependência

**Maven** (`pom.xml`):

```xml
<dependency>
    <groupId>io.github.cdimascio</groupId>
    <artifactId>dotenv-java</artifactId>
    <version>3.0.0</version>
</dependency>
```

### 4. Usar no código

```java
import io.github.cdimascio.dotenv.Dotenv;

public class Application {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();

        String dbUser = dotenv.get("DB_USERNAME");
        String dbPassword = dotenv.get("DB_PASSWORD");

        // Usar as variáveis na aplicação
    }
}
```

### ⚠️ Importante

- **Nunca commite** o arquivo `.env` no repositório
- O arquivo `.env` já está no `.gitignore`
- Use o arquivo `.env.example` como referência
