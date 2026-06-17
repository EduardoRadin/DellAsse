# Casos de Uso – Dell’Asse

## Caso 1 – Efetuar cadastro

**Ator principal:** Cliente

Diagrama de Caso de Uso: [Cadastro de Usuário](./CasosDeUso/CadastroDeUsuario.png)  
Diagrama de Atividade: [Efetuar Cadastro](./Atividades/EfetuarCadastro.png)  
Diagrama de Sequência: [Efetuar Cadastro](./Sequencia/CriarConta.png)  
Diagrama de Estados: [Efetuar Cadastro](./Estados/CriarConta.png)

**Fluxo principal (caso perfeito):**
1. Usuário acessa o site.
2. Acessa a aba de **galeria**.
3. Seleciona a **temática** desejada.
4. Demonstra interesse na festa selecionada.
5. É redirecionado para o **carrinho**, na aba da festa, onde pode editar os itens da festa.
6. Envia a **solicitação**.
7. A solicitação fica pendente para o gestor, com status possível de: **aprovado**, **pendente**, **negado**, **cancelado**, etc.
8. A festa é **aprovada**.
9. O cliente é **contatado pelo WhatsApp**.
10. O status da festa é atualizado no carrinho para **Aprovado**.
11. O gestor **gera o contrato** para o cliente.
12. O cliente **assina o contrato**.
13. A **festa é realizada**.

---

## Caso 2 – Cancelar solicitação de festa

**Ator principal:** Cliente

Diagrama de Caso de Uso: [Cancelar Solicitação de Festa](./CasosDeUso/CancelarSolicitação.png)  
Diagrama de Atividade: [Cancelar Pedido](./Atividades/CancelarPedido.png)  
Diagrama de Sequência: [Cancelar Solicitação](./Sequencia/CancelarFesta.png)  
Diagrama de Estados: [Cancelar Solicitação](./Estados/CancelarFesta.png)

**Fluxo principal (caso perfeito):**
1. O Visitante acessa o site.
2. Clica em **“Cadastrar”** ou **“Criar conta”**.
3. Informa os dados obrigatórios (nome, e-mail, senha, telefone, etc.).
4. Confirma o cadastro.
5. O sistema valida os dados e cria o **perfil de Cliente**.
6. O sistema redireciona para a **página de login** ou já entra logado, conforme regra de negócio.

---

## Caso 3 – Contratar festa

**Ator principal:** Cliente

Diagrama de Caso de Uso: [Contratar Festa](./CasosDeUso/ContratarFesta.png)  
Diagrama de Atividade: [Contratar Festa](./Atividades/ContratarFesta.PNG)  
Diagrama de Sequência: [Contratar Festa](./Sequencia/ContratarFesta.png)  
Diagrama de Estados: [Contratar Festa](./Estados/SolicitarFesta.png)

**Fluxo principal (caso perfeito):**
1. O Cliente acessa a área **“Carrinho”**.
2. Seleciona uma festa com status **“Em andamento”** (ou equivalente).
3. Clica em **“Cancelar”**.
4. O sistema solicita **confirmação** do cancelamento.
5. O Cliente confirma.
6. O sistema altera o status da festa para **Cancelado**.

---

## Caso 4 – Cadastrar festas

**Ator principal:** Gestor

Diagrama de Caso de Uso: [Cadastrar Festas](./CasosDeUso/CadastroDeFesta.png)  
Diagrama de Atividade: [Cadastrar Festas](./Atividades/CadastroDeFesta.png)  
Diagrama de Sequência: [Cadastrar Festas](./Sequencia/Criarfesta.png)  
Diagrama de Estados: [Cadastrar Festas](./Estados/CadastratFesta%20(gestor)%20.png)

**Fluxo principal (caso perfeito):**
1. O Gestor acessa o **Painel de Admin**.
2. O Gestor navega até a área **“Adicionar Festa”**.
3. Preenche os campos obrigatórios: **título**, **orçamento base**, **descrição**, **URL da imagem**.
4. O sistema valida os dados e registra a nova festa no catálogo.

---

## Caso 5 – Efetuar login

**Atores principais:** Cliente, Gestor

Diagrama de Caso de Uso: [Efetuar Login](./CasosDeUso/RealizarLogin.png)  
Diagrama de Atividade: [Efetuar Login](./Atividades/EfetuarLogin.png)  
Diagrama de Sequência: [Efetuar Login](./Sequencia/EfetuarLogin.png)  
Diagrama de Estados: [Efetuar Login](./Estados/EfetuarLogin.png)

**Fluxo principal (caso perfeito):**
1. O Usuário acessa a **página inicial** do sistema e seleciona a opção **“Login / Entrar”**.
2. O sistema exibe a **tela de login** com campos de e-mail/usuário e senha.
3. O Usuário preenche as credenciais.
4. O Usuário confirma o login clicando em **“Entrar”**.
5. O sistema valida as credenciais:
   - verifica se usuário e senha são válidos;
   - identifica se o usuário é **Admin** ou **Cliente**.
6. O sistema finaliza o processo de autenticação, redirecionando para:
   - o **painel de Admin**, quando se tratar de administrador; ou  
   - a **tela inicial do cliente**, quando se tratar de cliente.