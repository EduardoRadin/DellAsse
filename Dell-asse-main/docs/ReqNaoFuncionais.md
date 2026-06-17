- **RNF01 – Galeria de fotos de decorações**  
  O sistema deve disponibilizar uma galeria de fotos das decorações, armazenadas em Blobs ou por meio de API de armazenamento (ex.: Drive), exibidas em formato de carrossel. Cada decoração deve possuir um título que possa ser utilizado para pesquisa (busca parcial).

- **RNF02 – Botão de interesse com integração ao WhatsApp**  
  Cada item da galeria deve possuir um botão de interesse que, ao ser clicado, redireciona o usuário para o WhatsApp, enviando automaticamente a mensagem:  
  “Olá, tenho interesse na decoração {{nome_da_decoração}} #{{código_da_decoração}}”.

- **RNF03 – Cadastro e autenticação de usuários**  
  O registro e login dos usuários devem ser armazenados em banco de dados, não sendo permitidos usuários duplicados. O cadastro deve conter obrigatoriamente: CPF, número de celular, data de nascimento, endereço, foto de perfil, login e senha. A senha deve ser armazenada criptografada em SHA-256 e seguir a política de senha forte, contendo no mínimo 8 caracteres, com ao menos uma letra maiúscula, uma letra minúscula e um caractere especial.

- **RNF04 – Avaliação de imagens**  
  Usuários autenticados (clientes logados) devem poder avaliar as imagens das decorações, sendo essas avaliações persistidas em banco de dados.

- **RNF05 – Funcionalidades disponíveis ao usuário autenticado na galeria**  
  Após autenticação, o usuário deve poder ser redirecionado para a página da galeria de fotos, visualizar a descrição da decoração, solicitar orçamento, realizar avaliações, registrar comentários e visualizar a agenda de datas disponíveis.

- **RNF06 – Gestão de itens pelo funcionário**  
  Funcionários devem poder criar novos itens (decorações) no sistema, indicar um código para o item e garantir que cada item receba um código identificador.

- **RNF07 – Administração do sistema (ADM)**  
  O administrador deve poder gerenciar o sistema, incluindo deletar itens, editar itens, atribuir cargos/perfis a usuários, inativar usuários e apagar avaliações registradas.